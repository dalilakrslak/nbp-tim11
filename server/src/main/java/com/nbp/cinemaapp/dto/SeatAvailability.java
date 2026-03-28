package com.nbp.cinemaapp.dto;

import com.nbp.cinemaapp.enums.SeatType;

import java.util.UUID;

public class SeatAvailability {
    private final UUID seatId;
    private final String seatCode;
    private final SeatType seatType;
    private final Boolean taken;

    public SeatAvailability(final UUID seatId, final String seatCode, final SeatType seatType, final Boolean taken) {
        this.seatId = seatId;
        this.seatCode = seatCode;
        this.seatType = seatType;
        this.taken = taken;
    }

    public UUID getSeatId() {
        return seatId;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public Boolean getTaken() {
        return taken;
    }
}
