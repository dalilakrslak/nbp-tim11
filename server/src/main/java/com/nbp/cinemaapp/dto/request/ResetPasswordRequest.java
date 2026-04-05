package com.nbp.cinemaapp.dto.request;

public class ResetPasswordRequest {
    private String token;
    private String newPassword;

    public String getToken() { return token; }

    public void setToken(final String token) { this.token = token; }

    public String getNewPassword() { return newPassword; }

    public void setNewPassword(final String newPassword) { this.newPassword = newPassword; }
}
