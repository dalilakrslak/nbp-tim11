package com.nbp.cinemaapp.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PgRating {

    G("G"),
    PG("PG"),
    PG_13("PG-13"),
    R("R");

    private final String value;

    PgRating(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PgRating fromValue(String value) {
        for (PgRating rating : values()) {
            if (rating.value.equalsIgnoreCase(value)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Invalid PgRating: " + value);
    }
}
