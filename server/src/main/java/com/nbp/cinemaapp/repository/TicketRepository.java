package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Ticket;
import com.nbp.cinemaapp.util.UuidUtil;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@Repository
public class TicketRepository {

    private static final String INSERT_SQL = """
        INSERT INTO TICKETS (
            ID,
            PRICE,
            STATUS,
            BOOKING_DATE,
            CREATED_AT,
            UPDATED_AT,
            USER_ID,
            SCREENING_ID
        )
        VALUES (
            HEXTORAW(?),
            ?,
            ?,
            ?,
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP,
            HEXTORAW(?),
            HEXTORAW(?)
        )
        """;

    private final DataSource dataSource;

    public TicketRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Ticket save(final Ticket ticket) {
        final UUID id = UUID.randomUUID();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));
            preparedStatement.setInt(2, ticket.getPrice());
            preparedStatement.setString(3, ticket.getStatus().name());
            preparedStatement.setDate(4, java.sql.Date.valueOf(ticket.getBookingDate()));
            preparedStatement.setString(5, UuidUtil.toRawHex(ticket.getUser().getId()));
            preparedStatement.setString(6, UuidUtil.toRawHex(ticket.getScreening().getId()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert ticket", e);
        }

        return new Ticket(
                id,
                ticket.getPrice(),
                ticket.getStatus(),
                ticket.getBookingDate(),
                null,
                null,
                ticket.getUser(),
                ticket.getScreening()
        );
    }
}