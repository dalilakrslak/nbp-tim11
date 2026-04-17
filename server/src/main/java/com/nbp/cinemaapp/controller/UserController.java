package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.dto.projection.UserProjection;
import com.nbp.cinemaapp.dto.request.AuthenticationRequestDto;
import com.nbp.cinemaapp.dto.request.ChangePasswordRequest;
import com.nbp.cinemaapp.dto.request.PasswordResetRequest;
import com.nbp.cinemaapp.dto.request.RegistrationRequestDto;
import com.nbp.cinemaapp.dto.request.ResetPasswordRequest;
import com.nbp.cinemaapp.dto.request.UserRequest;
import com.nbp.cinemaapp.dto.response.AuthenticationResponseDto;
import com.nbp.cinemaapp.dto.response.RegistrationResponseDto;
import com.nbp.cinemaapp.dto.response.UserResponse;
import com.nbp.cinemaapp.service.UserService;
import com.nbp.cinemaapp.util.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users & Authentication", description = "Operacije za autentikaciju, registraciju, upravljanje korisnicima i lozinkama")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Prijava korisnika",
            description = "Autentificira korisnika na osnovu email adrese i lozinke te vraća pristupni i refresh token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Korisnik uspješno prijavljen"),
            @ApiResponse(responseCode = "400", description = "Neispravan zahtjev za prijavu"),
            @ApiResponse(responseCode = "401", description = "Neispravni kredencijali")
    })
    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody final AuthenticationRequestDto authenticationRequestDto) {
        return ResponseEntity.ok(userService.authenticate(authenticationRequestDto));
    }

    @Operation(
            summary = "Registracija korisnika",
            description = "Registruje novog korisnika u sistem."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Korisnik uspješno registrovan"),
            @ApiResponse(responseCode = "400", description = "Neispravni podaci za registraciju")
    })
    @PostMapping("/auth/register")
    public ResponseEntity<RegistrationResponseDto> registerUser(
            @Valid @RequestBody final RegistrationRequestDto registrationDTO) {
        final RegistrationResponseDto response = userService.registerUser(registrationDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obnavljanje tokena",
            description = "Generiše novi pristupni token na osnovu važećeg refresh tokena."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token uspješno osvježen"),
            @ApiResponse(responseCode = "400", description = "Neispravan refresh token"),
            @ApiResponse(responseCode = "401", description = "Refresh token nije validan")
    })
    @PostMapping("/auth/refresh-token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            @RequestBody final Map<String, String> request) {
        final UUID refreshToken = UUID.fromString(request.get("refreshToken"));
        final AuthenticationResponseDto response = userService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Odjava korisnika",
            description = "Poništava refresh token i odjavljuje korisnika iz sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Korisnik uspješno odjavljen"),
            @ApiResponse(responseCode = "400", description = "Neispravan refresh token")
    })
    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@RequestBody final Map<String, String> request) {
        final UUID refreshToken = UUID.fromString(request.get("refreshToken"));
        userService.revokeRefreshToken(refreshToken);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Dohvaća profil prijavljenog korisnika",
            description = "Vraća osnovne podatke o trenutno prijavljenom korisniku."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profil korisnika uspješno vraćen"),
            @ApiResponse(responseCode = "401", description = "Korisnik nije autentificiran")
    })
    @GetMapping("/user/me")
    public ResponseEntity<UserProjection> getUserProfile(
            @Parameter(hidden = true) final Authentication authentication) {
        return ResponseEntity.ok(userService.getProjectedUserByEmail(authentication.getName()));
    }

    @Operation(
            summary = "Zahtjev za reset lozinke",
            description = "Pokreće proces resetovanja lozinke za korisnika na osnovu email adrese."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zahtjev za reset lozinke obrađen")
    })
    @PostMapping("/auth/reset-password")
    public ResponseEntity<String> requestReset(@RequestBody final PasswordResetRequest request) {
        userService.processPasswordResetRequest(request.getEmail());
        return ResponseEntity.ok("If the email is registered, you'll get a reset link");
    }

    @Operation(
            summary = "Validacija tokena za reset lozinke",
            description = "Provjerava da li je token za reset lozinke validan."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token je validan"),
            @ApiResponse(responseCode = "400", description = "Token nije validan ili je istekao")
    })
    @GetMapping("/auth/reset-password")
    public ResponseEntity<String> validateToken(
            @Parameter(description = "Token za reset lozinke", required = true)
            @RequestParam("token") final String token) {
        try {
            final String result = userService.validateToken(token);
            return ResponseEntity.ok(result);
        } catch (final IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Potvrda resetovanja lozinke",
            description = "Postavlja novu lozinku koristeći validan token za reset."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lozinka uspješno ažurirana"),
            @ApiResponse(responseCode = "400", description = "Neispravan token ili neispravna nova lozinka")
    })
    @PostMapping("/auth/reset-password/confirm")
    public ResponseEntity<String> resetPassword(@RequestBody final ResetPasswordRequest request) {
        try {
            if (request.getNewPassword().length() < 6) {
                return ResponseEntity.badRequest().body("Password must be at least 6 characters");
            }
            userService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Password updated");
        } catch (final IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Dohvaća sve korisnike",
            description = "Vraća paginiranu listu svih korisnika. Dostupno samo administratorima."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista korisnika uspješno vraćena"),
            @ApiResponse(responseCode = "403", description = "Pristup dozvoljen samo administratoru")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @ModelAttribute Pagination pagination) {
        return ResponseEntity.ok(userService.getAllUsers(pagination.toPageable()));
    }

    @Operation(
            summary = "Kreira novog korisnika",
            description = "Kreira novog korisnika u sistemu. Dostupno samo administratorima."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Korisnik uspješno kreiran"),
            @ApiResponse(responseCode = "400", description = "Neispravni podaci za kreiranje korisnika"),
            @ApiResponse(responseCode = "403", description = "Pristup dozvoljen samo administratoru")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(
            @RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @Operation(
            summary = "Briše korisnika",
            description = "Briše korisnika iz sistema na osnovu identifikatora. Dostupno samo administratorima."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Korisnik uspješno obrisan"),
            @ApiResponse(responseCode = "403", description = "Pristup dozvoljen samo administratoru"),
            @ApiResponse(responseCode = "404", description = "Korisnik nije pronađen")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "UUID identifikator korisnika", required = true)
            @PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Promjena lozinke prijavljenog korisnika",
            description = "Omogućava prijavljenom korisniku da promijeni vlastitu lozinku."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lozinka uspješno promijenjena"),
            @ApiResponse(responseCode = "400", description = "Nova lozinka nije validna"),
            @ApiResponse(responseCode = "401", description = "Korisnik nije autentificiran")
    })
    @PostMapping("/user/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            @Parameter(hidden = true) Authentication authentication) {
        String username = authentication.getName();
        if (request.newPassword().length() < 6) {
            return ResponseEntity.badRequest().body("Password must be at least 6 characters");
        }
        userService.changePassword(username, request);
        return ResponseEntity.ok().build();
    }
}
