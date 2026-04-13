package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Hall;
import com.nbp.cinemaapp.entity.Seat;
import com.nbp.cinemaapp.enums.SeatType;
import com.nbp.cinemaapp.util.ResultSetUtil;
import com.nbp.cinemaapp.util.UuidUtil;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class SeatRepository {

    private static final String BASE_SELECT = """
        SELECT RAWTOHEX(S.ID) AS ID,
               S.SEAT_CODE,
               S.SEAT_TYPE,
               S.CREATED_AT,
               S.UPDATED_AT,
               RAWTOHEX(S.HALL_ID) AS HALL_ID
        FROM SEATS S
        """;

    private final DataSource dataSource;

    public SeatRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Seat> findAllById(final Set<UUID> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            return List.of();
        }

        final String placeholders = seatIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        final String sql = BASE_SELECT + """
            WHERE RAWTOHEX(S.ID) IN (
            """ + placeholders + ")";

        final List<Seat> seats = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            int index = 1;
            for (UUID seatId : seatIds) {
                preparedStatement.setString(index++, UuidUtil.toRawHex(seatId));
            }

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapSeat(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch seats by ids", e);
        }

        return seats;
    }

    public List<Seat> findBySeatCodeInAndHallId(final Set<String> seatCodes, final UUID hallId) {
        if (seatCodes == null || seatCodes.isEmpty()) {
            return List.of();
        }

        final String placeholders = seatCodes.stream()
                .map(code -> "?")
                .collect(Collectors.joining(", "));

        final String sql = BASE_SELECT + """
            WHERE RAWTOHEX(S.HALL_ID) = ?
              AND S.SEAT_CODE IN (
            """ + placeholders + ")";

        final List<Seat> seats = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(hallId));

            int index = 2;
            for (String seatCode : seatCodes) {
                preparedStatement.setString(index++, seatCode);
            }

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapSeat(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch seats by seat codes and hall id", e);
        }

        return seats;
    }

    public List<Seat> findAllByHallId(final UUID hallId) {
        final String sql = BASE_SELECT + """
            WHERE RAWTOHEX(S.HALL_ID) = ?
            ORDER BY S.SEAT_CODE
            """;

        final List<Seat> seats = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(hallId));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapSeat(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch seats by hall id: " + hallId, e);
        }

        return seats;
    }

    private Seat mapSeat(final ResultSet rs) throws SQLException {
        final Hall hall = new Hall();
        hall.setId(UuidUtil.fromRawHex(rs.getString("HALL_ID")));

        return new Seat(
                UuidUtil.fromRawHex(rs.getString("ID")),
                rs.getString("SEAT_CODE"),
                SeatType.valueOf(rs.getString("SEAT_TYPE")),
                ResultSetUtil.getLocalDate(rs, "CREATED_AT"),
                ResultSetUtil.getLocalDate(rs, "UPDATED_AT"),
                hall
        );
    }
}