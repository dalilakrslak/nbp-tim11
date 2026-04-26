package com.nbp.cinemaapp.dto.response;

import com.nbp.cinemaapp.enums.SystemRole;

import java.util.UUID;

public class UserProfileResponse {

    private UUID id;
    private String email;
    private SystemRole role;
    private boolean hasProfilePicture;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public SystemRole getRole() {
        return role;
    }

    public void setRole(final SystemRole role) {
        this.role = role;
    }

    public boolean getHasProfilePicture() {
        return hasProfilePicture;
    }

    public void setHasProfilePicture(final boolean hasProfilePicture) {
        this.hasProfilePicture = hasProfilePicture;
    }
}
