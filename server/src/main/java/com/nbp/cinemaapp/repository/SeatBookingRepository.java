package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Seat;
import com.nbp.cinemaapp.entity.SeatBooking;
import com.nbp.cinemaapp.entity.Ticket;
import com.nbp.cinemaapp.enums.TicketStatus;
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

@Repository
public class SeatBookingRepository {

    private static final String FIND_BY_SCREENING_AND_STATUS_SQL = """
        SELECT RAWTOHEX(SB.ID) AS ID,
               RAWTOHEX(SB.SEAT_ID) AS SEAT_ID,
               RAWTOHEX(SB.TICKET_ID) AS TICKET_ID,
               SB.CREATED_AT,
               SB.UPDATED_AT
        FROM SEAT_BOOKING SB
        JOIN TICKETS T ON SB.TICKET_ID = T.ID
        WHERE RAWTOHEX(T.SCREENING_ID) = ?
          AND T.STATUS = ?
        """;

    private static final String EXISTS_SQL = """
        SELECT COUNT(*)
        FROM SEAT_BOOKING SB
        JOIN TICKETS T ON SB.TICKET_ID = T.ID
        WHERE RAWTOHEX(SB.SEAT_ID) = ?
          AND RAWTOHEX(T.SCREENING_ID) = ?
          AND T.STATUS = ?
        """;

    private static final String INSERT_SQL = """
        INSERT INTO SEAT_BOOKING (
            ID,
            TICKET_ID,
            SEAT_ID,
            CREATED_AT,
            UPDATED_AT
        )
        VALUES (
            HEXTORAW(?),
            HEXTORAW(?),
            HEXTORAW(?),
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP
        )
        """;

    private final DataSource dataSource;

    public SeatBookingRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<SeatBooking> findAllByScreeningIdAndTicketStatus(final UUID screeningId, final TicketStatus status) {
        final List<SeatBooking> bookings = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_SCREENING_AND_STATUS_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(screeningId));
            preparedStatement.setString(2, status.name());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapSeatBooking(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch seat bookings", e);
        }

        return bookings;
    }

    public boolean existsBySeatIdAndTicketScreeningIdAndTicketStatus(final UUID seatId,
                                                                     final UUID screeningId,
                                                                     final TicketStatus status) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(EXISTS_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(seatId));
            preparedStatement.setString(2, UuidUtil.toRawHex(screeningId));
            preparedStatement.setString(3, status.name());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check seat booking existence", e);
        }

        return false;
    }

    public void saveAll(final Set<SeatBooking> seatBookings) {
        if (seatBookings == null || seatBookings.isEmpty()) {
            return;
        }

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)
        ) {
            for (SeatBooking seatBooking : seatBookings) {
                final UUID id = UUID.randomUUID();
                seatBooking.setId(id);

                preparedStatement.setString(1, UuidUtil.toRawHex(id));
                preparedStatement.setString(2, UuidUtil.toRawHex(seatBooking.getTicket().getId()));
                preparedStatement.setString(3, UuidUtil.toRawHex(seatBooking.getSeat().getId()));
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save seat bookings", e);
        }
    }

    private SeatBooking mapSeatBooking(final ResultSet rs) throws SQLException {
        final Ticket ticket = new Ticket();
        ticket.setId(UuidUtil.fromRawHex(rs.getString("TICKET_ID")));

        final Seat seat = new Seat();
        seat.setId(UuidUtil.fromRawHex(rs.getString("SEAT_ID")));

        return new SeatBooking(
                UuidUtil.fromRawHex(rs.getString("ID")),
                ticket,
                seat,
                ResultSetUtil.getLocalDate(rs, "CREATED_AT"),
                ResultSetUtil.getLocalDate(rs, "UPDATED_AT")
        );
    }
}