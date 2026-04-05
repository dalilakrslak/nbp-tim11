package com.nbp.cinemaapp.dto.request;

import java.util.Set;
import java.util.UUID;

public class CreateTicketRequest {
    private UUID userId;
    private UUID screeningId;
    private Set<UUID> seatIds;
    private Integer price;

    public CreateTicketRequest() {}

    public CreateTicketRequest(final UUID userId, final UUID screeningId, final Set<UUID> seatIds, final Integer price) {
        this.userId = userId;
        this.screeningId = screeningId;
        this.seatIds = seatIds;
        this.price = price;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(final UUID userId) {
        this.userId = userId;
    }

    public UUID getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(final UUID screeningId) {
        this.screeningId = screeningId;
    }

    public Set<UUID> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(final Set<UUID> seatIds) {
        this.seatIds = seatIds;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(final Integer price) {
        this.price = price;
    }
}

