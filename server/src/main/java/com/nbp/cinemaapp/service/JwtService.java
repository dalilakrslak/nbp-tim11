package com.nbp.cinemaapp.service;

import com.nbp.cinemaapp.enums.SystemRole;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Duration;
import java.time.Instant;

public class JwtService {

    private final String issuer;
    private final Duration accessTokenTtl;
    private final Duration refreshTokenTtl;
    private final JwtEncoder jwtEncoder;

    public JwtService(final String issuer,
                      final Duration accessTokenTtl,
                      final Duration refreshTokenTtl,
                      final JwtEncoder jwtEncoder) {
        this.issuer = issuer;
        this.accessTokenTtl = accessTokenTtl;
        this.refreshTokenTtl = refreshTokenTtl;
        this.jwtEncoder = jwtEncoder;
    }

    public String generateAccessToken(final String email, final SystemRole role) {
        final JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(email)
                .issuer(issuer)
                .claim("role", role.name())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(accessTokenTtl))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet))
                .getTokenValue();
    }

    public String generateRefreshToken(final String email) {
        final JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(email)
                .issuer(issuer)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(refreshTokenTtl))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet))
                .getTokenValue();
    }
}
