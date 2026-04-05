package com.nbp.cinemaapp.dto.response;

import com.nbp.cinemaapp.entity.Ticket;
import com.nbp.cinemaapp.enums.TicketStatus;

import java.time.Instant;
import java.util.UUID;

public class TicketResponse {
    private final UUID id;
    private final Integer price;
    private final TicketStatus status;
    private final Instant bookingDate;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final UUID userId;
    private final UUID screeningId;

    public TicketResponse(final Ticket ticket) {
        this.id = ticket.getId();
        this.price = ticket.getPrice();
        this.status = ticket.getStatus();
        this.bookingDate = ticket.getBookingDate();
        this.createdAt = ticket.getCreatedAt();
        this.updatedAt = ticket.getUpdatedAt();
        this.userId = ticket.getUser().getId();
        this.screeningId = ticket.getScreening().getId();
    }

    public UUID getId() {
        return id;
    }

    public Integer getPrice() {
        return price;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public Instant getBookingDate() {
        return bookingDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getScreeningId() {
        return screeningId;
    }
}
