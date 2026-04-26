package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Ticket;
import com.nbp.cinemaapp.enums.TicketStatus;
import com.nbp.cinemaapp.util.UuidUtil;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Optional;
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
            TICKET_PDF,
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
            ?,
            HEXTORAW(?),
            HEXTORAW(?)
        )
        """;

    private static final String FIND_PDF_BY_ID_SQL = """
        SELECT
            RAWTOHEX(ID) AS ID,
            RAWTOHEX(USER_ID) AS USER_ID,
            TICKET_PDF,
            STATUS,
            BOOKING_DATE
        FROM TICKETS
        WHERE ID = HEXTORAW(?)
        """;

    private final DataSource dataSource;

    public TicketRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Ticket save(final Ticket ticket) {
        final UUID id = ticket.getId() != null ? ticket.getId() : UUID.randomUUID();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));
            preparedStatement.setInt(2, ticket.getPrice());
            preparedStatement.setString(3, ticket.getStatus().name());
            preparedStatement.setDate(4, java.sql.Date.valueOf(ticket.getBookingDate()));
            if (ticket.getTicketPdf() == null) {
                preparedStatement.setNull(5, Types.BLOB);
            } else {
                preparedStatement.setBytes(5, ticket.getTicketPdf());
            }
            preparedStatement.setString(6, UuidUtil.toRawHex(ticket.getUser().getId()));
            preparedStatement.setString(7, UuidUtil.toRawHex(ticket.getScreening().getId()));

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
                ticket.getTicketPdf(),
                ticket.getUser(),
                ticket.getScreening()
        );
    }

    public Optional<TicketPdfRecord> findTicketPdfById(final UUID ticketId) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_PDF_BY_ID_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(ticketId));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(new TicketPdfRecord(
                        UuidUtil.fromRawHex(resultSet.getString("ID")),
                        UuidUtil.fromRawHex(resultSet.getString("USER_ID")),
                        resultSet.getBytes("TICKET_PDF"),
                        TicketStatus.valueOf(resultSet.getString("STATUS")),
                        resultSet.getDate("BOOKING_DATE").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load ticket pdf", e);
        }
    }

    public record TicketPdfRecord(
            UUID ticketId,
            UUID userId,
            byte[] pdfBytes,
            TicketStatus status,
            LocalDate bookingDate
    ) {
    }
}
