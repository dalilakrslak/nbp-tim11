package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Genre;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GenreRepositoryTest {

    @Test
    void findAllReturnsMappedGenres() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        GenreRepository repository = new GenreRepository(dataSource);
        UUID genreId = UUID.randomUUID();
        Timestamp timestamp = Timestamp.valueOf(LocalDate.of(2026, 1, 10).atStartOfDay());

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(org.mockito.ArgumentMatchers.anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("ID")).thenReturn(genreId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("NAME")).thenReturn("Action");
        when(resultSet.getTimestamp("CREATED_AT")).thenReturn(timestamp);
        when(resultSet.getTimestamp("UPDATED_AT")).thenReturn(timestamp);

        List<Genre> genres = repository.findAll();

        assertEquals(1, genres.size());
        assertEquals(genreId, genres.get(0).getId());
        assertEquals("Action", genres.get(0).getName());
        assertNotNull(genres.get(0).getCreatedAt());
    }
}
