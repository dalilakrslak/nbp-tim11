package com.nbp.cinemaapp.dto.response;

public class ChargeResponse {
    private String id;
    private String status;
    private String balanceTransaction;

    public ChargeResponse(final String id, final String status, final String balanceTransaction) {
        this.id = id;
        this.status = status;
        this.balanceTransaction = balanceTransaction;
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
}
