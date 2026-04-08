package com.nbp.cinemaapp.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

public final class ResultSetUtil {

    private ResultSetUtil() {
    }

    public static LocalDate getLocalDate(final ResultSet rs, final String column) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(column);
        return timestamp != null ? timestamp.toLocalDateTime().toLocalDate() : null;
    }
}