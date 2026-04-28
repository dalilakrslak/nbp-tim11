package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.entity.Ticket;
import com.nbp.cinemaapp.entity.User;
import com.nbp.cinemaapp.enums.TicketStatus;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TicketRepositoryTest {

    @Test
    void savePersistsTicketWithPdf() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        TicketRepository repository = new TicketRepository(dataSource);
        Ticket ticket = ticket(new byte[]{1, 2, 3});

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        Ticket saved = repository.save(ticket);

        assertEquals(ticket.getPrice(), saved.getPrice());
        assertEquals(ticket.getStatus(), saved.getStatus());
        verify(statement).setBytes(5, new byte[]{1, 2, 3});
    }

    @Test
    void savePersistsTicketWithNullPdf() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        TicketRepository repository = new TicketRepository(dataSource);
        Ticket ticket = ticket(null);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.save(ticket);

        verify(statement).setNull(5, java.sql.Types.BLOB);
    }

    @Test
    void findTicketPdfByIdReturnsRecordWhenPresent() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        TicketRepository repository = new TicketRepository(dataSource);
        UUID ticketId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("ID")).thenReturn(ticketId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("USER_ID")).thenReturn(userId.toString().replace("-", "").toUpperCase());
        when(resultSet.getBytes("TICKET_PDF")).thenReturn(new byte[]{9});
        when(resultSet.getString("STATUS")).thenReturn("PURCHASED");
        when(resultSet.getDate("BOOKING_DATE")).thenReturn(Date.valueOf(LocalDate.of(2026, 1, 10)));

        Optional<TicketRepository.TicketPdfRecord> record = repository.findTicketPdfById(ticketId);

        assertTrue(record.isPresent());
        assertEquals(ticketId, record.get().ticketId());
        assertEquals(userId, record.get().userId());
        assertArrayEquals(new byte[]{9}, record.get().pdfBytes());
    }

    @Test
    void findTicketPdfByIdReturnsEmptyWhenMissing() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        TicketRepository repository = new TicketRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<TicketRepository.TicketPdfRecord> record = repository.findTicketPdfById(UUID.randomUUID());

        assertTrue(record.isEmpty());
    }

    private Ticket ticket(final byte[] pdf) {
        User user = new User();
        user.setId(UUID.randomUUID());
        Screening screening = new Screening();
        screening.setId(UUID.randomUUID());

        Ticket ticket = new Ticket();
        ticket.setPrice(15);
        ticket.setStatus(TicketStatus.PURCHASED);
        ticket.setBookingDate(LocalDate.of(2026, 1, 10));
        ticket.setTicketPdf(pdf);
        ticket.setUser(user);
        ticket.setScreening(screening);
        return ticket;
    }
}
