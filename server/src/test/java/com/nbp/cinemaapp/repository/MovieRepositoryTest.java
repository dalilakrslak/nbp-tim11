package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Movie;
import com.nbp.cinemaapp.enums.PgRating;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.sql.DataSource;
import java.sql.Connection;
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

class MovieRepositoryTest {

    @Test
    void findByIdReturnsMovieWithRelations() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement movieStatement = mock(PreparedStatement.class);
        PreparedStatement genreStatement = mock(PreparedStatement.class);
        PreparedStatement photoStatement = mock(PreparedStatement.class);
        ResultSet movieResultSet = mock(ResultSet.class);
        ResultSet genreResultSet = mock(ResultSet.class);
        ResultSet photoResultSet = mock(ResultSet.class);
        MovieRepository repository = new MovieRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(movieStatement, genreStatement, photoStatement);
        when(movieStatement.executeQuery()).thenReturn(movieResultSet);
        when(genreStatement.executeQuery()).thenReturn(genreResultSet);
        when(photoStatement.executeQuery()).thenReturn(photoResultSet);
        when(movieResultSet.next()).thenReturn(true);
        when(genreResultSet.next()).thenReturn(true, false);
        when(photoResultSet.next()).thenReturn(true, false);
        stubMovieRow(movieResultSet, "Interstellar");
        stubGenreRow(genreResultSet);
        stubPhotoRow(photoResultSet);

        Optional<Movie> movie = repository.findById(UUID.randomUUID());

        assertTrue(movie.isPresent());
        assertEquals("Interstellar", movie.get().getTitle());
        assertEquals(1, movie.get().getMovieGenres().size());
        assertEquals(1, movie.get().getPhotos().size());
    }

    @Test
    void findAllReturnsPage() throws Exception {
        Page<Movie> page = new MovieRepository(mockPagedMovieDataSource("Avatar", 1L))
                .findAll(PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("Avatar", page.getContent().get(0).getTitle());
    }

    @Test
    void findCurrentlyShowingReturnsFilteredPage() throws Exception {
        Page<Movie> page = new MovieRepository(mockPagedMovieDataSource("Oppenheimer", 1L))
                .findCurrentlyShowing(
                        "Opp",
                        List.of("Drama"),
                        "Sarajevo",
                        "Cinestar",
                        LocalDateTime.of(2026, 1, 10, 20, 0),
                        LocalDate.of(2026, 1, 10),
                        PageRequest.of(0, 10)
                );

        assertEquals("Oppenheimer", page.getContent().get(0).getTitle());
    }

    @Test
    void findUpcomingReturnsFilteredPage() throws Exception {
        Page<Movie> page = new MovieRepository(mockPagedMovieDataSource("Minecraft", 1L))
                .findUpcoming("Mine", List.of("Adventure"), "Sarajevo", "Cinestar", PageRequest.of(0, 10));

        assertEquals("Minecraft", page.getContent().get(0).getTitle());
    }

    @Test
    void findLatestReturnsLatestPage() throws Exception {
        Page<Movie> page = new MovieRepository(mockPagedMovieDataSource("Latest Movie", 1L))
                .findLatest(3);

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void findSimilarMoviesReturnsEmptyForNoGenres() {
        MovieRepository repository = new MovieRepository(mock(DataSource.class));

        Page<Movie> page = repository.findSimilarMovies(List.of(), UUID.randomUUID(), PageRequest.of(0, 10));

        assertTrue(page.isEmpty());
    }

    @Test
    void findSimilarMoviesReturnsPageWhenGenresPresent() throws Exception {
        Page<Movie> page = new MovieRepository(mockPagedMovieDataSource("Similar Movie", 1L))
                .findSimilarMovies(List.of(UUID.randomUUID()), UUID.randomUUID(), PageRequest.of(0, 10));

        assertEquals("Similar Movie", page.getContent().get(0).getTitle());
    }

    @Test
    void saveInsertsWhenMovieHasNoId() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement insertStatement = mock(PreparedStatement.class);
        PreparedStatement findStatement = mock(PreparedStatement.class);
        PreparedStatement genreStatement = mock(PreparedStatement.class);
        PreparedStatement photoStatement = mock(PreparedStatement.class);
        ResultSet movieResultSet = mock(ResultSet.class);
        ResultSet genreResultSet = mock(ResultSet.class);
        ResultSet photoResultSet = mock(ResultSet.class);
        MovieRepository repository = new MovieRepository(dataSource);
        Movie movie = movie(null);

        when(dataSource.getConnection()).thenReturn(connection, connection);
        when(connection.prepareStatement(anyString())).thenReturn(insertStatement, findStatement, genreStatement, photoStatement);
        when(findStatement.executeQuery()).thenReturn(movieResultSet);
        when(genreStatement.executeQuery()).thenReturn(genreResultSet);
        when(photoStatement.executeQuery()).thenReturn(photoResultSet);
        when(movieResultSet.next()).thenReturn(true);
        when(genreResultSet.next()).thenReturn(false);
        when(photoResultSet.next()).thenReturn(false);
        stubMovieRow(movieResultSet, "Inserted Movie");

        Movie saved = repository.save(movie);

        assertEquals("Inserted Movie", saved.getTitle());
    }

    @Test
    void saveUpdatesWhenMovieHasId() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement updateStatement = mock(PreparedStatement.class);
        PreparedStatement findStatement = mock(PreparedStatement.class);
        PreparedStatement genreStatement = mock(PreparedStatement.class);
        PreparedStatement photoStatement = mock(PreparedStatement.class);
        ResultSet movieResultSet = mock(ResultSet.class);
        ResultSet genreResultSet = mock(ResultSet.class);
        ResultSet photoResultSet = mock(ResultSet.class);
        MovieRepository repository = new MovieRepository(dataSource);
        Movie movie = movie(UUID.randomUUID());

        when(dataSource.getConnection()).thenReturn(connection, connection);
        when(connection.prepareStatement(anyString())).thenReturn(updateStatement, findStatement, genreStatement, photoStatement);
        when(findStatement.executeQuery()).thenReturn(movieResultSet);
        when(genreStatement.executeQuery()).thenReturn(genreResultSet);
        when(photoStatement.executeQuery()).thenReturn(photoResultSet);
        when(movieResultSet.next()).thenReturn(true);
        when(genreResultSet.next()).thenReturn(false);
        when(photoResultSet.next()).thenReturn(false);
        stubMovieRow(movieResultSet, "Updated Movie");

        Movie saved = repository.save(movie);

        assertEquals("Updated Movie", saved.getTitle());
    }

    @Test
    void deleteByIdExecutesDelete() throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        MovieRepository repository = new MovieRepository(dataSource);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.deleteById(UUID.randomUUID());
    }

    private DataSource mockPagedMovieDataSource(final String title, final long total) throws Exception {
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement selectStatement = mock(PreparedStatement.class);
        PreparedStatement countStatement = mock(PreparedStatement.class);
        ResultSet selectResultSet = mock(ResultSet.class);
        ResultSet countResultSet = mock(ResultSet.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(selectStatement, countStatement);
        when(selectStatement.executeQuery()).thenReturn(selectResultSet);
        when(countStatement.executeQuery()).thenReturn(countResultSet);
        when(selectResultSet.next()).thenReturn(true, false);
        when(countResultSet.next()).thenReturn(true);
        when(countResultSet.getLong(1)).thenReturn(total);
        stubMovieRow(selectResultSet, title);
        return dataSource;
    }

    private void stubMovieRow(final ResultSet resultSet, final String title) throws Exception {
        UUID movieId = UUID.randomUUID();
        Timestamp created = Timestamp.valueOf(LocalDate.of(2026, 1, 10).atStartOfDay());
        when(resultSet.getString("ID")).thenReturn(movieId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("TITLE")).thenReturn(title);
        when(resultSet.getString("SYNOPSIS")).thenReturn("Synopsis");
        when(resultSet.getInt("DURATION")).thenReturn(120);
        when(resultSet.wasNull()).thenReturn(false);
        when(resultSet.getString("PG_RATING")).thenReturn(PgRating.PG_13.name());
        when(resultSet.getString("LANGUAGE")).thenReturn("English");
        when(resultSet.getString("TRAILER_URL")).thenReturn("trailer");
        when(resultSet.getString("DIRECTOR")).thenReturn("Director");
        when(resultSet.getTimestamp("START_DATE")).thenReturn(created);
        when(resultSet.getTimestamp("END_DATE")).thenReturn(created);
        when(resultSet.getTimestamp("CREATED_AT")).thenReturn(created);
        when(resultSet.getTimestamp("UPDATED_AT")).thenReturn(created);
    }

    private void stubGenreRow(final ResultSet resultSet) throws Exception {
        UUID genreRelationId = UUID.randomUUID();
        UUID genreId = UUID.randomUUID();
        Timestamp timestamp = Timestamp.valueOf(LocalDate.of(2026, 1, 10).atStartOfDay());
        when(resultSet.getString("ID")).thenReturn(genreRelationId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("GENRE_ID")).thenReturn(genreId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("NAME")).thenReturn("Drama");
        when(resultSet.getTimestamp("CREATED_AT")).thenReturn(timestamp);
        when(resultSet.getTimestamp("UPDATED_AT")).thenReturn(timestamp);
        when(resultSet.getTimestamp("GENRE_CREATED_AT")).thenReturn(timestamp);
        when(resultSet.getTimestamp("GENRE_UPDATED_AT")).thenReturn(timestamp);
    }

    private void stubPhotoRow(final ResultSet resultSet) throws Exception {
        UUID photoId = UUID.randomUUID();
        Timestamp timestamp = Timestamp.valueOf(LocalDate.of(2026, 1, 10).atStartOfDay());
        when(resultSet.getString("ID")).thenReturn(photoId.toString().replace("-", "").toUpperCase());
        when(resultSet.getString("URL")).thenReturn("photo");
        when(resultSet.getInt("IS_COVER_IMAGE")).thenReturn(1);
        when(resultSet.getTimestamp("CREATED_AT")).thenReturn(timestamp);
        when(resultSet.getTimestamp("UPDATED_AT")).thenReturn(timestamp);
    }

    private Movie movie(final UUID id) {
        return new Movie(
                id,
                "Movie",
                "Synopsis",
                120,
                LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 2, 10),
                PgRating.PG_13,
                "English",
                "trailer",
                "Director",
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
