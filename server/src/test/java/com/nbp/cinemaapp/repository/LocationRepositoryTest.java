package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Location;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocationRepositoryTest {

    @Test
    void findAllReturnsMappedLocations() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        LocationRepository repository = new LocationRepository(dataSource);

        stubLocationQuery(dataSource, connection, statement, resultSet, UUID.randomUUID(), "Sarajevo");
        when(resultSet.next()).thenReturn(true, false);

        List<Location> locations = repository.findAll();

        assertEquals(1, locations.size());
        assertEquals("Sarajevo", locations.get(0).getCity());
    }

    @Test
    void findByIdReturnsLocationWhenPresent() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        LocationRepository repository = new LocationRepository(dataSource);
        UUID id = UUID.randomUUID();

        stubLocationQuery(dataSource, connection, statement, resultSet, id, "Tuzla");
        when(resultSet.next()).thenReturn(true);

        Optional<Location> location = repository.findById(id);

        assertTrue(location.isPresent());
        assertEquals(id, location.get().getId());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        LocationRepository repository = new LocationRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<Location> location = repository.findById(UUID.randomUUID());

        assertTrue(location.isEmpty());
    }

    private void stubLocationQuery(final DataSource dataSource,
                                   final Connection connection,
                                   final PreparedStatement statement,
                                   final ResultSet resultSet,
                                   final UUID id,
                                   final String city) throws Exception {
        Timestamp timestamp = Timestamp.valueOf(LocalDate.of(2026, 1, 10).atStartOfDay());
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getString("ID")).thenReturn(id.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("CITY")).thenReturn(city);
        when(resultSet.getString("COUNTRY")).thenReturn("Bosnia and Herzegovina");
        when(resultSet.getTimestamp("CREATED_AT")).thenReturn(timestamp);
        when(resultSet.getTimestamp("UPDATED_AT")).thenReturn(timestamp);
    }
}
