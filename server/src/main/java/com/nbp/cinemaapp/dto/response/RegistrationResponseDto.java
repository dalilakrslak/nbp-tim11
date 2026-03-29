package com.nbp.cinemaapp.dto.response;

import java.util.UUID;

public record RegistrationResponseDto(String email, String accessToken, UUID refreshToken) {}
