package com.nbp.cinemaapp.dto.request;

import com.nbp.cinemaapp.dto.BookingDetails;

import java.util.UUID;

public class ChargeRequest {
    private String token;
    private Integer amount;
    private String email;
    private UUID userId;
    private BookingDetails bookingDetails;

    public ChargeRequest() {}

    public ChargeRequest(final String token, final Integer amount, final String email, final UUID userId, final BookingDetails bookingDetails) {
        this.token = token;
        this.amount = amount;
        this.email = email;
        this.userId = userId;
        this.bookingDetails = bookingDetails;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Integer getAmount() { return amount; }
    public void setAmount(final Integer amount) { this.amount = amount; }

    public String getEmail() { return email; }
    public void setEmail(final String email) { this.email = email; }

    public UUID getUserId() { return userId; }
    public void setUserId(final UUID userId) { this.userId = userId; }

    public BookingDetails getBookingDetails() { return bookingDetails; }
    public void setBookingDetails(final BookingDetails bookingDetails) { this.bookingDetails = bookingDetails; }
}
