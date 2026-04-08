package com.nbp.cinemaapp.util;

import java.util.UUID;

public final class UuidUtil {

    private UuidUtil() {
    }

    public static UUID fromRawHex(final String hex) {
        if (hex == null) {
            return null;
        }

        String value = hex.trim();

        validateRawHex(value);

        return UUID.fromString(formatUuid(value));
    }

    public static String toRawHex(final UUID uuid) {
        if (uuid == null) {
            return null;
        }

        return uuid.toString().replace("-", "").toUpperCase();
    }

    private static void validateRawHex(final String value) {
        if (value.length() != 32) {
            throw new IllegalArgumentException("Invalid RAW(16) hex value: " + value);
        }
    }

    private static String formatUuid(final String value) {
        return value.substring(0, 8) + "-" +
                value.substring(8, 12) + "-" +
                value.substring(12, 16) + "-" +
                value.substring(16, 20) + "-" +
                value.substring(20, 32);
    }
}