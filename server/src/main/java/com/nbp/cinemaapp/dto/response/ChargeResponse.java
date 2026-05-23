package com.nbp.cinemaapp.dto.response;

import java.util.UUID;

public class ChargeResponse {
    private String id;
    private String status;
    private String balanceTransaction;
    private UUID ticketId;

    public ChargeResponse(final String id, final String status, final String balanceTransaction, final UUID ticketId) {
        this.id = id;
        this.status = status;
        this.balanceTransaction = balanceTransaction;
        this.ticketId = ticketId;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getBalanceTransaction() {
        return balanceTransaction;
    }

    public void setBalanceTransaction(final String balanceTransaction) {
        this.balanceTransaction = balanceTransaction;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public void setTicketId(final UUID ticketId) {
        this.ticketId = ticketId;
    }
}
