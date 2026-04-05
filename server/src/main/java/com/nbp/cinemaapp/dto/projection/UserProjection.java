package com.nbp.cinemaapp.dto.projection;

import com.nbp.cinemaapp.enums.SystemRole;

import java.util.UUID;

public interface UserProjection {
    public UUID getId();
    public String getEmail();
    public SystemRole getRole();
}
