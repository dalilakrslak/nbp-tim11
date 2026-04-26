package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Location;
import com.nbp.cinemaapp.entity.Venue;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VenueRepositoryTest {

    @Test
    void findAllReturnsMappedVenues() throws Exception {
        VenueRepository repository = new VenueRepository(mockVenueListDataSource());

        List<Venue> venues = repository.findAll();

        assertEquals(1, venues.size());
        assertEquals("Cinestar", venues.get(0).getName());
    }

    @Test
    void findAllPageableReturnsPageAndCount() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement selectStatement = mock(PreparedStatement.class);
        PreparedStatement countStatement = mock(PreparedStatement.class);
        ResultSet selectResultSet = mock(ResultSet.class);
        ResultSet countResultSet = mock(ResultSet.class);
        VenueRepository repository = new VenueRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(selectStatement, countStatement);
        when(selectStatement.executeQuery()).thenReturn(selectResultSet);
        when(countStatement.executeQuery()).thenReturn(countResultSet);
        when(selectResultSet.next()).thenReturn(true, false);
        when(countResultSet.next()).thenReturn(true);
        when(countResultSet.getLong(1)).thenReturn(1L);
        stubVenueRow(selectResultSet, "Cineplexx");

        Page<Venue> page = repository.findAll(PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("Cineplexx", page.getContent().get(0).getName());
    }

    @Test
    void findByIdReturnsVenueWhenPresent() throws Exception {
        VenueRepository repository = new VenueRepository(mockSingleVenueDataSource(true));

        Optional<Venue> venue = repository.findById(UUID.randomUUID());

        assertTrue(venue.isPresent());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() throws Exception {
        VenueRepository repository = new VenueRepository(mockSingleVenueDataSource(false));

        Optional<Venue> venue = repository.findById(UUID.randomUUID());

        assertTrue(venue.isEmpty());
    }

    @Test
    void saveInsertsWhenVenueHasNoId() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement insertStatement = mock(PreparedStatement.class);
        PreparedStatement findStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        VenueRepository repository = new VenueRepository(dataSource);
        Venue venue = venue(null);

        when(dataSource.getConnection()).thenReturn(connection, connection);
        when(connection.prepareStatement(anyString())).thenReturn(insertStatement, findStatement);
        when(findStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        stubVenueRow(resultSet, "Arena");

        Venue saved = repository.save(venue);

        assertEquals("Arena", saved.getName());
    }

    @Test
    void saveUpdatesWhenVenueHasId() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement updateStatement = mock(PreparedStatement.class);
        PreparedStatement findStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        VenueRepository repository = new VenueRepository(dataSource);
        Venue venue = venue(UUID.randomUUID());

        when(dataSource.getConnection()).thenReturn(connection, connection);
        when(connection.prepareStatement(anyString())).thenReturn(updateStatement, findStatement);
        when(findStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        stubVenueRow(resultSet, "Updated Arena");

        Venue saved = repository.save(venue);

        assertEquals("Updated Arena", saved.getName());
    }

    @Test
    void deleteByIdReturnsTrueWhenRowDeleted() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        VenueRepository repository = new VenueRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertTrue(repository.deleteById(UUID.randomUUID()));
    }

    @Test
    void deleteByIdReturnsFalseWhenNoRowDeleted() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        VenueRepository repository = new VenueRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        assertFalse(repository.deleteById(UUID.randomUUID()));
    }

    private DataSource mockVenueListDataSource() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        stubVenueRow(resultSet, "Cinestar");
        return dataSource;
    }

    private DataSource mockSingleVenueDataSource(final boolean found) throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(found);
        if (found) {
            stubVenueRow(resultSet, "Cinestar");
        }
        return dataSource;
    }

    private void stubVenueRow(final ResultSet resultSet, final String venueName) throws Exception {
        UUID venueId = UUID.randomUUID();
        UUID locationId = UUID.randomUUID();
        Timestamp timestamp = Timestamp.valueOf(LocalDate.of(2026, 1, 10).atStartOfDay());
        when(resultSet.getString("ID")).thenReturn(venueId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("NAME")).thenReturn(venueName);
        when(resultSet.getString("STREET")).thenReturn("Main Street");
        when(resultSet.getString("IMAGE_URL")).thenReturn("image");
        when(resultSet.getString("LOCATION_ID")).thenReturn(locationId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("CITY")).thenReturn("Sarajevo");
        when(resultSet.getString("COUNTRY")).thenReturn("Bosnia and Herzegovina");
        when(resultSet.getTimestamp("CREATED_AT")).thenReturn(timestamp);
        when(resultSet.getTimestamp("UPDATED_AT")).thenReturn(timestamp);
        when(resultSet.getTimestamp("LOCATION_CREATED_AT")).thenReturn(timestamp);
        when(resultSet.getTimestamp("LOCATION_UPDATED_AT")).thenReturn(timestamp);
    }

    private Venue venue(final UUID id) {
        Location location = new Location(UUID.randomUUID(), "Sarajevo", "Bosnia and Herzegovina", LocalDate.now(), LocalDate.now(), null);
        return new Venue(id, "Arena", "Street", "img", LocalDate.now(), LocalDate.now(), location, null);
    }
}
