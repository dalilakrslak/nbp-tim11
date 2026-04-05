package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.dto.projection.UserProjection;
import com.nbp.cinemaapp.dto.request.*;
import com.nbp.cinemaapp.dto.request.UserRequest;
import com.nbp.cinemaapp.dto.response.AuthenticationResponseDto;
import com.nbp.cinemaapp.dto.response.RegistrationResponseDto;
import com.nbp.cinemaapp.dto.response.UserResponse;
import com.nbp.cinemaapp.service.UserService;
import com.nbp.cinemaapp.util.Pagination;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody final AuthenticationRequestDto authenticationRequestDto) {

        return ResponseEntity.ok(userService.authenticate(authenticationRequestDto));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody final RegistrationRequestDto registrationDTO) {

        final RegistrationResponseDto response = userService.registerUser(registrationDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(@RequestBody final Map<String, String> request) {

        final UUID refreshToken = UUID.fromString(request.get("refreshToken"));
        final AuthenticationResponseDto response = userService.refreshToken(refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@RequestBody final Map<String, String> request) {

        final UUID refreshToken = UUID.fromString(request.get("refreshToken"));
        userService.revokeRefreshToken(refreshToken);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserProjection> getUserProfile(final Authentication authentication) {

        return ResponseEntity.ok(userService.getProjectedUserByEmail(authentication.getName()));
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<String> requestReset(@RequestBody final PasswordResetRequest request) {
        userService.processPasswordResetRequest(request.getEmail());
        return ResponseEntity.ok("If the email is registered, you'll get a reset link");
    }

    @GetMapping("/auth/reset-password")
    public ResponseEntity<String> validateToken(@RequestParam("token") final String token) {
        try {
            final String result = userService.validateToken(token);
            return ResponseEntity.ok(result);
        } catch (final IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/auth/reset-password/confirm")
    public ResponseEntity<String> resetPassword(@RequestBody final ResetPasswordRequest request) {
        try {
            if(request.getNewPassword().length() < 6) {
                return ResponseEntity.badRequest().body("Password must be at least 6 characters");
            }
            userService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Password updated");
        } catch (final IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @ModelAttribute Pagination pagination
    ) {
        return ResponseEntity.ok(
                userService.getAllUsers(pagination.toPageable())
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(
            @RequestBody @Valid UserRequest request
    ) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/user/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {
        String username = authentication.getName();
        if(request.newPassword().length() < 6) {
            return ResponseEntity.badRequest().body("Password must be at least 6 characters");
        }
        userService.changePassword(username, request);
        return ResponseEntity.ok().build();
    }
}
