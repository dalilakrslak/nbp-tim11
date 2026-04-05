package com.nbp.cinemaapp.dto;

import java.util.Set;
import java.util.UUID;

public class BookingDetails {
    private UUID screeningId;
    private Set<String> selectedSeatCodes;
    private UUID userId;

    public UUID getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(final UUID screeningId) {
        this.screeningId = screeningId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(final UUID userId) {
        this.userId = userId;
    }

    public Set<String> getSelectedSeatCodes() {
        return selectedSeatCodes;
    }

    public void setSelectedSeatCodes(final Set<String> selectedSeatCodes) {
        this.selectedSeatCodes = selectedSeatCodes;
    }
}
