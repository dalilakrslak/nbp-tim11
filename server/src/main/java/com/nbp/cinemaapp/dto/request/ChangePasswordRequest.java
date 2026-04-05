package com.nbp.cinemaapp.dto.request;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword
) {}
