package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Seat;
import com.nbp.cinemaapp.entity.SeatBooking;
import com.nbp.cinemaapp.entity.Ticket;
import com.nbp.cinemaapp.enums.TicketStatus;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SeatBookingRepositoryTest {

    @Test
    void findAllByScreeningIdAndTicketStatusReturnsMappedBookings() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        SeatBookingRepository repository = new SeatBookingRepository(dataSource);
        UUID seatId = UUID.randomUUID();
        UUID ticketId = UUID.randomUUID();
        UUID bookingId = UUID.randomUUID();
        Timestamp timestamp = Timestamp.valueOf(LocalDate.of(2026, 1, 10).atStartOfDay());

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("ID")).thenReturn(bookingId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("SEAT_ID")).thenReturn(seatId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("TICKET_ID")).thenReturn(ticketId.toString().replace("-", "").toUpperCase());
        when(resultSet.getTimestamp("CREATED_AT")).thenReturn(timestamp);
        when(resultSet.getTimestamp("UPDATED_AT")).thenReturn(timestamp);

        List<SeatBooking> bookings = repository.findAllByScreeningIdAndTicketStatus(UUID.randomUUID(), TicketStatus.PURCHASED);

        assertEquals(1, bookings.size());
        assertEquals(bookingId, bookings.get(0).getId());
    }

    @Test
    void existsBySeatIdAndTicketScreeningIdAndTicketStatusReturnsTrueWhenCountPositive() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        SeatBookingRepository repository = new SeatBookingRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        boolean exists = repository.existsBySeatIdAndTicketScreeningIdAndTicketStatus(
                UUID.randomUUID(),
                UUID.randomUUID(),
                TicketStatus.PURCHASED
        );

        assertTrue(exists);
    }

    @Test
    void existsBySeatIdAndTicketScreeningIdAndTicketStatusReturnsFalseWhenCountZero() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        SeatBookingRepository repository = new SeatBookingRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(0L);

        boolean exists = repository.existsBySeatIdAndTicketScreeningIdAndTicketStatus(
                UUID.randomUUID(),
                UUID.randomUUID(),
                TicketStatus.PURCHASED
        );

        assertFalse(exists);
    }

    @Test
    void saveAllReturnsEarlyForEmptyInput() {
        DataSource dataSource = mock(DataSource.class);
        SeatBookingRepository repository = new SeatBookingRepository(dataSource);

        repository.saveAll(Set.of());

        verifyNoInteractions(dataSource);
    }

    @Test
    void saveAllBatchesSeatBookings() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        SeatBookingRepository repository = new SeatBookingRepository(dataSource);
        Set<SeatBooking> bookings = new LinkedHashSet<>();
        bookings.add(seatBooking());
        bookings.add(seatBooking());

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.saveAll(bookings);

        verify(statement).executeBatch();
        verify(statement, org.mockito.Mockito.times(2)).addBatch();
    }

    private SeatBooking seatBooking() {
        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());
        Seat seat = new Seat();
        seat.setId(UUID.randomUUID());
        SeatBooking seatBooking = new SeatBooking();
        seatBooking.setTicket(ticket);
        seatBooking.setSeat(seat);
        return seatBooking;
    }
}
