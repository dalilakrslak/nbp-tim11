package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Screening;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScreeningRepositoryTest {

    @Test
    void findByIdReturnsScreeningWhenPresent() throws Exception {
        ScreeningRepository repository = new ScreeningRepository(mockSingleScreeningDataSource(true));

        Optional<Screening> screening = repository.findById(UUID.randomUUID());

        assertTrue(screening.isPresent());
        assertEquals(LocalDateTime.of(2026, 1, 10, 20, 0), screening.get().getStartTime());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() throws Exception {
        ScreeningRepository repository = new ScreeningRepository(mockSingleScreeningDataSource(false));

        Optional<Screening> screening = repository.findById(UUID.randomUUID());

        assertTrue(screening.isEmpty());
    }

    @Test
    void findByMovieIdReturnsOrderedScreenings() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        ScreeningRepository repository = new ScreeningRepository(dataSource);

        stubScreeningQuery(dataSource, connection, statement, resultSet);
        when(resultSet.next()).thenReturn(true, false);

        List<Screening> screenings = repository.findByMovieId(UUID.randomUUID());

        assertEquals(1, screenings.size());
    }

    @Test
    void findAllFilteredReturnsPageAndCount() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement selectStatement = mock(PreparedStatement.class);
        PreparedStatement countStatement = mock(PreparedStatement.class);
        ResultSet selectResultSet = mock(ResultSet.class);
        ResultSet countResultSet = mock(ResultSet.class);
        ScreeningRepository repository = new ScreeningRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(selectStatement, countStatement);
        when(selectStatement.executeQuery()).thenReturn(selectResultSet);
        when(countStatement.executeQuery()).thenReturn(countResultSet);
        when(selectResultSet.next()).thenReturn(true, false);
        when(countResultSet.next()).thenReturn(true);
        when(countResultSet.getLong(1)).thenReturn(1L);
        stubScreeningRow(selectResultSet);

        Page<Screening> page = repository.findAllFiltered(
                UUID.randomUUID(),
                "Sarajevo",
                "Cineplexx",
                LocalDate.of(2026, 1, 10),
                PageRequest.of(0, 10)
        );

        assertEquals(1, page.getTotalElements());
        assertEquals(1, page.getContent().size());
    }

    private DataSource mockSingleScreeningDataSource(final boolean found) throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(found);
        if (found) {
            stubScreeningRow(resultSet);
        }
        return dataSource;
    }

    private void stubScreeningQuery(final DataSource dataSource,
                                    final Connection connection,
                                    final PreparedStatement statement,
                                    final ResultSet resultSet) throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        stubScreeningRow(resultSet);
    }

    private void stubScreeningRow(final ResultSet resultSet) throws Exception {
        UUID screeningId = UUID.randomUUID();
        UUID movieId = UUID.randomUUID();
        UUID hallId = UUID.randomUUID();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2026, 1, 10, 20, 0));
        when(resultSet.getString("ID")).thenReturn(screeningId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("MOVIE_ID")).thenReturn(movieId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("HALL_ID")).thenReturn(hallId.toString().replace("-", "").toUpperCase());
        when(resultSet.getTimestamp("START_TIME")).thenReturn(timestamp);
        when(resultSet.getTimestamp("CREATED_AT")).thenReturn(Timestamp.valueOf(LocalDate.of(2026, 1, 10).atStartOfDay()));
        when(resultSet.getTimestamp("UPDATED_AT")).thenReturn(Timestamp.valueOf(LocalDate.of(2026, 1, 10).atStartOfDay()));
    }
}
