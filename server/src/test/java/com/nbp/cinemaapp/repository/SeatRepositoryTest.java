package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Seat;
import com.nbp.cinemaapp.enums.SeatType;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SeatRepositoryTest {

    @Test
    void findAllByIdReturnsEmptyForEmptyIds() {
        DataSource dataSource = mock(DataSource.class);
        SeatRepository repository = new SeatRepository(dataSource);

        List<Seat> seats = repository.findAllById(Set.of());

        assertTrue(seats.isEmpty());
        verifyNoInteractions(dataSource);
    }

    @Test
    void findAllByIdReturnsMappedSeats() throws Exception {
        SeatRepository repository = new SeatRepository(mockSeatDataSource("A1"));

        List<Seat> seats = repository.findAllById(Set.of(UUID.randomUUID()));

        assertEquals(1, seats.size());
        assertEquals("A1", seats.get(0).getSeatCode());
    }

    @Test
    void findBySeatCodeInAndHallIdReturnsEmptyForEmptyCodes() {
        DataSource dataSource = mock(DataSource.class);
        SeatRepository repository = new SeatRepository(dataSource);

        List<Seat> seats = repository.findBySeatCodeInAndHallId(Set.of(), UUID.randomUUID());

        assertTrue(seats.isEmpty());
        verifyNoInteractions(dataSource);
    }

    @Test
    void findBySeatCodeInAndHallIdReturnsMappedSeats() throws Exception {
        SeatRepository repository = new SeatRepository(mockSeatDataSource("B2"));

        List<Seat> seats = repository.findBySeatCodeInAndHallId(Set.of("B2"), UUID.randomUUID());

        assertEquals(1, seats.size());
        assertEquals("B2", seats.get(0).getSeatCode());
    }

    @Test
    void findAllByHallIdReturnsMappedSeats() throws Exception {
        SeatRepository repository = new SeatRepository(mockSeatDataSource("C3"));

        List<Seat> seats = repository.findAllByHallId(UUID.randomUUID());

        assertEquals(1, seats.size());
        assertEquals(SeatType.REGULAR, seats.get(0).getSeatType());
    }

    private DataSource mockSeatDataSource(final String seatCode) throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        UUID seatId = UUID.randomUUID();
        UUID hallId = UUID.randomUUID();
        Timestamp timestamp = Timestamp.valueOf(LocalDate.of(2026, 1, 10).atStartOfDay());

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("ID")).thenReturn(seatId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("SEAT_CODE")).thenReturn(seatCode);
        when(resultSet.getString("SEAT_TYPE")).thenReturn("REGULAR");
        when(resultSet.getString("HALL_ID")).thenReturn(hallId.toString().replace("-", "").toUpperCase());
        when(resultSet.getTimestamp("CREATED_AT")).thenReturn(timestamp);
        when(resultSet.getTimestamp("UPDATED_AT")).thenReturn(timestamp);
        return dataSource;
    }
}
