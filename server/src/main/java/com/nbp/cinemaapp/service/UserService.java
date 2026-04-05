package com.nbp.cinemaapp.service;

import com.nbp.cinemaapp.config.JwtConfig;
import com.nbp.cinemaapp.dto.projection.UserProjection;
import com.nbp.cinemaapp.dto.request.AuthenticationRequestDto;
import com.nbp.cinemaapp.dto.request.ChangePasswordRequest;
import com.nbp.cinemaapp.dto.request.RegistrationRequestDto;
import com.nbp.cinemaapp.dto.request.UserRequest;
import com.nbp.cinemaapp.dto.response.AuthenticationResponseDto;
import com.nbp.cinemaapp.dto.response.RegistrationResponseDto;
import com.nbp.cinemaapp.dto.response.UserResponse;
import com.nbp.cinemaapp.entity.RefreshToken;
import com.nbp.cinemaapp.entity.ResetToken;
import com.nbp.cinemaapp.entity.User;
import com.nbp.cinemaapp.exception.ValidationErrorException;
import com.nbp.cinemaapp.mapper.UserMapper;
import com.nbp.cinemaapp.repository.RefreshTokenRepository;
import com.nbp.cinemaapp.repository.ResetTokenRepository;
import com.nbp.cinemaapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.GONE;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final AuthenticationManager authenticationManager;
    private final ResetTokenRepository resetTokenRepository;
    private final MailService mailService;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();
    private final UserMapper userMapper;

    public UserService(final UserRepository userRepository,
                       final RefreshTokenRepository refreshTokenRepository,
                       final PasswordEncoder passwordEncoder,
                       final JwtService jwtService,
                       final JwtConfig jwtConfig,
                       final AuthenticationManager authenticationManager,
                       final ResetTokenRepository resetTokenRepository,
                       final MailService mailService,
                       final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtConfig = jwtConfig;
        this.authenticationManager = authenticationManager;
        this.resetTokenRepository = resetTokenRepository;
        this.mailService = mailService;
        this.userMapper = userMapper;
    }

    public UserProjection getProjectedUserByEmail(final String email) {
        return userRepository.findProjectedByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(GONE,
                        "The user with specified email does not exist"));
    }

    @Transactional
    public RegistrationResponseDto registerUser(final RegistrationRequestDto registrationRequestDto) {

        if (userRepository.existsByEmail(registrationRequestDto.getEmail())) {
            throw new ValidationErrorException(Map.of("email", "Email already exists"));
        }

        final User user = new User(registrationRequestDto.getEmail(),
                passwordEncoder.encode(registrationRequestDto.getPassword()));

        final User savedUser = userRepository.save(user);

        final String accessToken = jwtService.generateAccessToken(savedUser.getEmail() , user.getRole());

        final RefreshToken refreshToken = new RefreshToken(savedUser, Instant.now().plus(jwtConfig.getRefreshTokenTtl()));
        refreshTokenRepository.save(refreshToken);

        return new RegistrationResponseDto(savedUser.getEmail(), accessToken, refreshToken.getId());
    }

    public AuthenticationResponseDto authenticate(final AuthenticationRequestDto request) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(),
                        request.password())
        );

        final Duration refreshTokenTtl = jwtConfig.getRefreshTokenTtl();

        final User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "No user found with the provided email address"));

        final String accessToken = jwtService.generateAccessToken(request.email(), user.getRole());

        final RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plus(refreshTokenTtl));
        final RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        return new AuthenticationResponseDto(accessToken, savedRefreshToken.getId());
    }

    public AuthenticationResponseDto refreshToken(final UUID refreshToken) {

        final RefreshToken refreshTokenEntity = refreshTokenRepository
                .findById(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(
                        BAD_REQUEST, "Invalid or expired refresh token"));

        final User user = refreshTokenEntity.getUser();

        final String newAccessToken = jwtService.generateAccessToken(refreshTokenEntity.getUser().getEmail(), user.getRole());

        return new AuthenticationResponseDto(newAccessToken, refreshToken);
    }

    public void revokeRefreshToken(final UUID refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }

    public void processPasswordResetRequest(final String email) {
        final Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return;

        final User user = userOpt.get();
        final String token = makeResetToken();

        final ResetToken record = new ResetToken();
        record.setToken(token);
        record.setUser(user);
        record.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        resetTokenRepository.save(record);

        mailService.sendEmail(user.getEmail(), token);
    }

    public String validateToken(final String token) {
        final Optional<ResetToken> tokenOpt = resetTokenRepository.findByToken(token);

        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired()) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        return "Token is valid";
    }

    @Transactional
    public void resetPassword(final String token, final String newPassword) {
        final ResetToken tokenRecord = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token is invalid or expired"));

        if (tokenRecord.isExpired()) {
            throw new IllegalArgumentException("Token is invalid or expired");
        }

        final User user = tokenRecord.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetTokenRepository.delete(tokenRecord);
    }

    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("Username not found: " + username));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private String makeResetToken() {
        final byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        return base64Encoder.encodeToString(randomBytes);
    }


    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::entityToDto);
    }

    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationErrorException(
                    Map.of("email", "Email already exists")
            );
        }

        User user = userMapper.dtoToEntity(request);
        return userMapper.entityToDto(userRepository.save(user));
    }

    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found");
        }

        userRepository.deleteById(userId);
    }
}
