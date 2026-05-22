package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Actor;
import com.nbp.cinemaapp.entity.Genre;
import com.nbp.cinemaapp.entity.Movie;
import com.nbp.cinemaapp.entity.MovieGenre;
import com.nbp.cinemaapp.entity.MoviePhoto;
import com.nbp.cinemaapp.entity.MovieWriter;
import com.nbp.cinemaapp.entity.Role;
import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.entity.Hall;
import com.nbp.cinemaapp.entity.Venue;
import com.nbp.cinemaapp.entity.Location;
import com.nbp.cinemaapp.entity.Writer;
import com.nbp.cinemaapp.enums.PgRating;
import com.nbp.cinemaapp.util.ResultSetUtil;
import com.nbp.cinemaapp.util.UuidUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MovieRepository {

    private static final String BASE_SELECT = """
        SELECT RAWTOHEX(M.ID) AS ID,
               M.TITLE,
               M.SYNOPSIS,
               M.DURATION,
               M.START_DATE,
               M.END_DATE,
               M.PG_RATING,
               M.LANGUAGE,
               M.TRAILER_URL,
               M.DIRECTOR,
               M.CREATED_AT,
               M.UPDATED_AT
        FROM MOVIES M
        """;

    private static final String BASE_COUNT = """
        SELECT COUNT(*)
        FROM MOVIES M
        """;

    private static final String FIND_BY_ID_SQL = BASE_SELECT + """
        WHERE RAWTOHEX(M.ID) = ?
        """;

    private static final String FIND_ALL_SQL = BASE_SELECT + """
        ORDER BY M.TITLE
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
        """;

    private static final String COUNT_ALL_SQL = BASE_COUNT;

    private static final String INSERT_SQL = """
        INSERT INTO MOVIES (
            ID,
            TITLE,
            SYNOPSIS,
            DURATION,
            START_DATE,
            END_DATE,
            PG_RATING,
            LANGUAGE,
            TRAILER_URL,
            DIRECTOR,
            CREATED_AT,
            UPDATED_AT
        )
        VALUES (
            HEXTORAW(?),
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP
        )
        """;

    private static final String UPDATE_SQL = """
        UPDATE MOVIES
        SET TITLE = ?,
            SYNOPSIS = ?,
            DURATION = ?,
            START_DATE = ?,
            END_DATE = ?,
            PG_RATING = ?,
            LANGUAGE = ?,
            TRAILER_URL = ?,
            DIRECTOR = ?,
            UPDATED_AT = CURRENT_TIMESTAMP
        WHERE RAWTOHEX(ID) = ?
        """;

    private static final String DELETE_BY_ID_SQL = """
        DELETE FROM MOVIES
        WHERE RAWTOHEX(ID) = ?
        """;

    private static final String FIND_MOVIE_GENRES_SQL = """
        SELECT RAWTOHEX(MG.ID) AS ID,
               MG.CREATED_AT,
               MG.UPDATED_AT,
               RAWTOHEX(G.ID) AS GENRE_ID,
               G.NAME,
               G.CREATED_AT AS GENRE_CREATED_AT,
               G.UPDATED_AT AS GENRE_UPDATED_AT
        FROM MOVIE_GENRE MG
        JOIN GENRES G ON MG.GENRE_ID = G.ID
        WHERE RAWTOHEX(MG.MOVIE_ID) = ?
        ORDER BY G.NAME
        """;

    private static final String FIND_MOVIE_PHOTOS_SQL = """
        SELECT RAWTOHEX(ID) AS ID,
               URL,
               IS_COVER_IMAGE,
               CREATED_AT,
               UPDATED_AT
        FROM MOVIE_PHOTOS
        WHERE RAWTOHEX(MOVIE_ID) = ?
        ORDER BY IS_COVER_IMAGE DESC, CREATED_AT
        """;

    private static final String FIND_MOVIE_SCREENINGS_SQL = """
    SELECT RAWTOHEX(S.ID) AS ID,
           S.START_TIME,
           S.CREATED_AT,
           S.UPDATED_AT,

           RAWTOHEX(H.ID) AS HALL_ID,
           H.NAME AS HALL_NAME,
           H.CREATED_AT AS HALL_CREATED_AT,
           H.UPDATED_AT AS HALL_UPDATED_AT,

           RAWTOHEX(V.ID) AS VENUE_ID,
           V.NAME AS VENUE_NAME,
           V.STREET AS VENUE_STREET,
           V.IMAGE_URL AS VENUE_IMAGE_URL,
           V.CREATED_AT AS VENUE_CREATED_AT,
           V.UPDATED_AT AS VENUE_UPDATED_AT,

           RAWTOHEX(L.ID) AS LOCATION_ID,
           L.CITY AS LOCATION_CITY,
           L.COUNTRY AS LOCATION_COUNTRY,
           L.CREATED_AT AS LOCATION_CREATED_AT,
           L.UPDATED_AT AS LOCATION_UPDATED_AT
    FROM SCREENINGS S
    JOIN HALLS H ON S.HALL_ID = H.ID
    JOIN VENUES V ON H.VENUE_ID = V.ID
    JOIN LOCATIONS L ON V.LOCATION_ID = L.ID
    WHERE RAWTOHEX(S.MOVIE_ID) = ?
    ORDER BY S.START_TIME
    """;

    private static final String FIND_MOVIE_WRITERS_SQL = """
        SELECT RAWTOHEX(MW.ID) AS ID,
               MW.CREATED_AT,
               MW.UPDATED_AT,
               RAWTOHEX(W.ID) AS WRITER_ID,
               W.FIRST_NAME AS WRITER_FIRST_NAME,
               W.LAST_NAME AS WRITER_LAST_NAME,
               W.CREATED_AT AS WRITER_CREATED_AT,
               W.UPDATED_AT AS WRITER_UPDATED_AT
        FROM MOVIE_WRITER MW
        JOIN WRITERS W ON MW.WRITER_ID = W.ID
        WHERE RAWTOHEX(MW.MOVIE_ID) = ?
        ORDER BY W.LAST_NAME, W.FIRST_NAME
        """;

    private static final String FIND_MOVIE_ROLES_SQL = """
        SELECT RAWTOHEX(R.ID) AS ID,
               R.NAME,
               R.CREATED_AT,
               R.UPDATED_AT,
               RAWTOHEX(A.ID) AS ACTOR_ID,
               A.FIRST_NAME AS ACTOR_FIRST_NAME,
               A.LAST_NAME AS ACTOR_LAST_NAME,
               A.CREATED_AT AS ACTOR_CREATED_AT,
               A.UPDATED_AT AS ACTOR_UPDATED_AT
        FROM ROLES R
        JOIN ACTORS A ON R.ACTOR_ID = A.ID
        WHERE RAWTOHEX(R.MOVIE_ID) = ?
        ORDER BY A.LAST_NAME, A.FIRST_NAME
        """;

    private final DataSource dataSource;

    public MovieRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<Movie> findById(final UUID id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }

                return Optional.of(mapMovieWithRelations(connection, rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch movie by id: " + id, e);
        }
    }

    public Page<Movie> findAll(final Pageable pageable) {
        return executePagedQuery(FIND_ALL_SQL, COUNT_ALL_SQL, List.of(), pageable, true);
    }

    public Page<Movie> findCurrentlyShowing(final String title,
                                            final List<String> genres,
                                            final String city,
                                            final String cinema,
                                            final java.time.LocalDateTime projectionTime,
                                            final LocalDate date,
                                            final Pageable pageable) {
        final LocalDate targetDate = date != null ? date : LocalDate.now();
        QueryParts queryParts = buildFilteredQuery(
                title,
                genres,
                city,
                cinema,
                projectionTime,
                "M.START_DATE <= ? AND M.END_DATE >= ?",
                List.of(Timestamp.valueOf(targetDate.atStartOfDay()), Timestamp.valueOf(targetDate.atStartOfDay()))
        );

        return executePagedQuery(
                BASE_SELECT + queryParts.whereClause + " ORDER BY M.TITLE OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
                BASE_COUNT + queryParts.whereClause,
                queryParts.parameters,
                pageable,
                true
        );
    }

    public Page<Movie> findUpcoming(final String title,
                                    final List<String> genres,
                                    final String city,
                                    final String cinema,
                                    final Pageable pageable) {
        QueryParts queryParts = buildFilteredQuery(
                title,
                genres,
                city,
                cinema,
                null,
                "M.START_DATE > ?",
                List.of(Timestamp.valueOf(LocalDate.now().atStartOfDay()))
        );

        return executePagedQuery(
                BASE_SELECT + queryParts.whereClause + " ORDER BY M.START_DATE, M.TITLE OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
                BASE_COUNT + queryParts.whereClause,
                queryParts.parameters,
                pageable,
                true
        );
    }

    public Page<Movie> findLatest(final int size) {
        final Pageable pageable = PageRequest.of(0, size);
        String sql = BASE_SELECT + """
            WHERE M.START_DATE <= ? AND M.END_DATE >= ?
            ORDER BY M.START_DATE DESC, M.TITLE
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
            """;
        List<Object> parameters = List.of(
                Timestamp.valueOf(LocalDate.now().atStartOfDay()),
                Timestamp.valueOf(LocalDate.now().atStartOfDay())
        );
        String countSql = BASE_COUNT + """
            WHERE M.START_DATE <= ? AND M.END_DATE >= ?
            """;

        return executePagedQuery(sql, countSql, parameters, pageable, true);
    }

    public Page<Movie> findSimilarMovies(final List<UUID> genreIds, final UUID movieId, final Pageable pageable) {
        if (genreIds == null || genreIds.isEmpty()) {
            return Page.empty(pageable);
        }

        String placeholders = String.join(", ", Collections.nCopies(genreIds.size(), "?"));
        String base = """
            FROM MOVIES M
            JOIN MOVIE_GENRE MG ON MG.MOVIE_ID = M.ID
            WHERE MG.GENRE_ID IN (%s)
              AND RAWTOHEX(M.ID) <> ?
            GROUP BY M.ID, M.TITLE, M.SYNOPSIS, M.DURATION, M.START_DATE, M.END_DATE, M.PG_RATING, M.LANGUAGE, M.TRAILER_URL, M.DIRECTOR, M.CREATED_AT, M.UPDATED_AT
            """.formatted(placeholders);

        String sql = """
            SELECT RAWTOHEX(M.ID) AS ID,
                   M.TITLE,
                   M.SYNOPSIS,
                   M.DURATION,
                   M.START_DATE,
                   M.END_DATE,
                   M.PG_RATING,
                   M.LANGUAGE,
                   M.TRAILER_URL,
                   M.DIRECTOR,
                   M.CREATED_AT,
                   M.UPDATED_AT
            """ + base + """
            ORDER BY COUNT(M.ID) DESC, M.TITLE
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
            """;

        String countSql = """
            SELECT COUNT(*)
            FROM (
                SELECT M.ID
                """ + base + """
            )
            """;

        List<Object> parameters = new ArrayList<>();
        for (UUID genreId : genreIds) {
            parameters.add(UuidUtil.toRawHex(genreId));
        }
        parameters.add(UuidUtil.toRawHex(movieId));

        return executePagedQuery(sql, countSql, parameters, pageable, true);
    }

    public Movie save(final Movie movie) {
        if (movie.getId() == null) {
            UUID id = UUID.randomUUID();
            insert(id, movie);

            return findById(id)
                    .orElseThrow(() -> new IllegalStateException("Movie was inserted but could not be retrieved"));
        }

        update(movie);
        return findById(movie.getId())
                .orElseThrow(() -> new IllegalStateException("Movie was updated but could not be retrieved"));
    }

    public void deleteById(final UUID id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete movie by id: " + id, e);
        }
    }

    private void insert(final UUID id, final Movie movie) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));
            preparedStatement.setString(2, movie.getTitle());
            preparedStatement.setString(3, movie.getSynopsis());
            setInteger(preparedStatement, 4, movie.getDuration());
            setTimestamp(preparedStatement, 5, movie.getStartDate());
            setTimestamp(preparedStatement, 6, movie.getEndDate());
            preparedStatement.setString(7, getPgRatingName(movie));
            preparedStatement.setString(8, movie.getLanguage());
            preparedStatement.setString(9, movie.getTrailerUrl());
            preparedStatement.setString(10, movie.getDirector());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert movie", e);
        }
    }

    private void update(final Movie movie) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)
        ) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getSynopsis());
            setInteger(preparedStatement, 3, movie.getDuration());
            setTimestamp(preparedStatement, 4, movie.getStartDate());
            setTimestamp(preparedStatement, 5, movie.getEndDate());
            preparedStatement.setString(6, getPgRatingName(movie));
            preparedStatement.setString(7, movie.getLanguage());
            preparedStatement.setString(8, movie.getTrailerUrl());
            preparedStatement.setString(9, movie.getDirector());
            preparedStatement.setString(10, UuidUtil.toRawHex(movie.getId()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update movie with id: " + movie.getId(), e);
        }
    }

    private Page<Movie> executePagedQuery(final String sql,
                                          final String countSql,
                                          final List<Object> parameters,
                                          final Pageable pageable,
                                          final boolean includeRelations) {
        List<Movie> movies = new ArrayList<>();
        long total = 0;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            int nextIndex = bindParameters(preparedStatement, parameters);
            preparedStatement.setLong(nextIndex++, pageable.getOffset());
            preparedStatement.setInt(nextIndex, pageable.getPageSize());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    movies.add(includeRelations ? mapMovieWithRelations(connection, rs) : mapMovie(rs));
                }
            }

            try (PreparedStatement countStatement = connection.prepareStatement(countSql)) {
                bindParameters(countStatement, parameters);

                try (ResultSet countRs = countStatement.executeQuery()) {
                    if (countRs.next()) {
                        total = countRs.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch paged movies", e);
        }

        return new PageImpl<>(movies, pageable, total);
    }

    private QueryParts buildFilteredQuery(final String title,
                                          final List<String> genres,
                                          final String city,
                                          final String cinema,
                                          final java.time.LocalDateTime projectionTime,
                                          final String datePredicate,
                                          final List<Object> dateParameters) {
        List<String> predicates = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            predicates.add("LOWER(M.TITLE) LIKE ?");
            parameters.add("%" + title.toLowerCase() + "%");
        }

        if (genres != null && !genres.isEmpty()) {
            String placeholders = String.join(", ", Collections.nCopies(genres.size(), "?"));
            predicates.add(
                    """
                    EXISTS (
                        SELECT 1
                        FROM MOVIE_GENRE MG
                        JOIN GENRES G ON MG.GENRE_ID = G.ID
                        WHERE MG.MOVIE_ID = M.ID
                          AND LOWER(G.NAME) IN (%s)
                    )""".formatted(placeholders)
            );
            for (String genre : genres) {
                parameters.add(genre.toLowerCase());
            }
        }

        if (city != null && !city.isBlank()) {
            predicates.add("""
                EXISTS (
                    SELECT 1
                    FROM SCREENINGS S
                    JOIN HALLS H ON S.HALL_ID = H.ID
                    JOIN VENUES V ON H.VENUE_ID = V.ID
                    JOIN LOCATIONS L ON V.LOCATION_ID = L.ID
                    WHERE S.MOVIE_ID = M.ID
                      AND LOWER(L.CITY) LIKE ?
                )""");
            parameters.add("%" + city.toLowerCase() + "%");
        }

        if (cinema != null && !cinema.isBlank()) {
            predicates.add("""
                EXISTS (
                    SELECT 1
                    FROM SCREENINGS S
                    JOIN HALLS H ON S.HALL_ID = H.ID
                    JOIN VENUES V ON H.VENUE_ID = V.ID
                    WHERE S.MOVIE_ID = M.ID
                      AND LOWER(V.NAME) LIKE ?
                )""");
            parameters.add("%" + cinema.toLowerCase() + "%");
        }

        if (projectionTime != null) {
            predicates.add("""
                EXISTS (
                    SELECT 1
                    FROM SCREENINGS S
                    WHERE S.MOVIE_ID = M.ID
                      AND S.START_TIME = ?
                )""");
            parameters.add(Timestamp.valueOf(projectionTime));
        }

        predicates.add(datePredicate);
        parameters.addAll(dateParameters);

        return new QueryParts(" WHERE " + String.join(" AND ", predicates), parameters);
    }

    private Movie mapMovieWithRelations(final Connection connection, final ResultSet rs) throws SQLException {
        Movie movie = mapMovie(rs);
        List<MovieGenre> movieGenres = findMovieGenres(connection, movie.getId());
        List<MoviePhoto> moviePhotos = findMoviePhotos(connection, movie.getId());
        List<Screening> screenings = findMovieScreenings(connection, movie.getId());
        List<MovieWriter> movieWriters = findMovieWriters(connection, movie.getId());
        List<Role> roles = findMovieRoles(connection, movie.getId());

        return new Movie(
                movie.getId(),
                movie.getTitle(),
                movie.getSynopsis(),
                movie.getDuration(),
                movie.getStartDate(),
                movie.getEndDate(),
                movie.getPgRating(),
                movie.getLanguage(),
                movie.getTrailerUrl(),
                movie.getDirector(),
                movie.getCreatedAt(),
                movie.getUpdatedAt(),
                movieGenres,
                moviePhotos,
                screenings,
                movieWriters,
                roles
        );
    }

    private List<MovieGenre> findMovieGenres(final Connection connection, final UUID movieId) throws SQLException {
        List<MovieGenre> movieGenres = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_MOVIE_GENRES_SQL)) {
            preparedStatement.setString(1, UuidUtil.toRawHex(movieId));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Genre genre = new Genre(
                            UuidUtil.fromRawHex(rs.getString("GENRE_ID")),
                            rs.getString("NAME"),
                            ResultSetUtil.getLocalDate(rs, "GENRE_CREATED_AT"),
                            ResultSetUtil.getLocalDate(rs, "GENRE_UPDATED_AT"),
                            null
                    );

                    movieGenres.add(new MovieGenre(
                            UuidUtil.fromRawHex(rs.getString("ID")),
                            null,
                            genre,
                            ResultSetUtil.getLocalDate(rs, "CREATED_AT"),
                            ResultSetUtil.getLocalDate(rs, "UPDATED_AT")
                    ));
                }
            }
        }

        return movieGenres;
    }

    private List<MoviePhoto> findMoviePhotos(final Connection connection, final UUID movieId) throws SQLException {
        List<MoviePhoto> photos = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_MOVIE_PHOTOS_SQL)) {
            preparedStatement.setString(1, UuidUtil.toRawHex(movieId));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    photos.add(new MoviePhoto(
                            UuidUtil.fromRawHex(rs.getString("ID")),
                            rs.getString("URL"),
                            rs.getInt("IS_COVER_IMAGE") == 1,
                            ResultSetUtil.getLocalDate(rs, "CREATED_AT"),
                            ResultSetUtil.getLocalDate(rs, "UPDATED_AT"),
                            null
                    ));
                }
            }
        }

        return photos;
    }

    private List<Screening> findMovieScreenings(final Connection connection, final UUID movieId) throws SQLException {
        List<Screening> screenings = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_MOVIE_SCREENINGS_SQL)) {
            preparedStatement.setString(1, UuidUtil.toRawHex(movieId));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Location location = new Location(
                            UuidUtil.fromRawHex(rs.getString("LOCATION_ID")),
                            rs.getString("LOCATION_CITY"),
                            rs.getString("LOCATION_COUNTRY"),
                            ResultSetUtil.getLocalDate(rs, "LOCATION_CREATED_AT"),
                            ResultSetUtil.getLocalDate(rs, "LOCATION_UPDATED_AT"),
                            null
                    );

                    Venue venue = new Venue(
                            UuidUtil.fromRawHex(rs.getString("VENUE_ID")),
                            rs.getString("VENUE_NAME"),
                            rs.getString("VENUE_STREET"),
                            rs.getString("VENUE_IMAGE_URL"),
                            ResultSetUtil.getLocalDate(rs, "VENUE_CREATED_AT"),
                            ResultSetUtil.getLocalDate(rs, "VENUE_UPDATED_AT"),
                            location,
                            null
                    );

                    Hall hall = new Hall(
                            UuidUtil.fromRawHex(rs.getString("HALL_ID")),
                            rs.getString("HALL_NAME"),
                            venue,
                            null,
                            ResultSetUtil.getLocalDate(rs, "HALL_CREATED_AT"),
                            ResultSetUtil.getLocalDate(rs, "HALL_UPDATED_AT")
                    );

                    screenings.add(new Screening(
                            UuidUtil.fromRawHex(rs.getString("ID")),
                            null,
                            hall,
                            ResultSetUtil.getLocalDateTime(rs, "START_TIME"),
                            ResultSetUtil.getLocalDate(rs, "CREATED_AT"),
                            ResultSetUtil.getLocalDate(rs, "UPDATED_AT")
                    ));
                }
            }
        }

        return screenings;
    }

    private List<MovieWriter> findMovieWriters(final Connection connection, final UUID movieId) throws SQLException {
        List<MovieWriter> movieWriters = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_MOVIE_WRITERS_SQL)) {
            preparedStatement.setString(1, UuidUtil.toRawHex(movieId));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Writer writer = new Writer(
                            UuidUtil.fromRawHex(rs.getString("WRITER_ID")),
                            rs.getString("WRITER_FIRST_NAME"),
                            rs.getString("WRITER_LAST_NAME"),
                            ResultSetUtil.getLocalDate(rs, "WRITER_CREATED_AT"),
                            ResultSetUtil.getLocalDate(rs, "WRITER_UPDATED_AT"),
                            null
                    );

                    movieWriters.add(new MovieWriter(
                            UuidUtil.fromRawHex(rs.getString("ID")),
                            null,
                            writer,
                            ResultSetUtil.getLocalDate(rs, "CREATED_AT"),
                            ResultSetUtil.getLocalDate(rs, "UPDATED_AT")
                    ));
                }
            }
        }

        return movieWriters;
    }

    private List<Role> findMovieRoles(final Connection connection, final UUID movieId) throws SQLException {
        List<Role> roles = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_MOVIE_ROLES_SQL)) {
            preparedStatement.setString(1, UuidUtil.toRawHex(movieId));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Actor actor = new Actor(
                            UuidUtil.fromRawHex(rs.getString("ACTOR_ID")),
                            rs.getString("ACTOR_FIRST_NAME"),
                            rs.getString("ACTOR_LAST_NAME"),
                            ResultSetUtil.getLocalDate(rs, "ACTOR_CREATED_AT"),
                            ResultSetUtil.getLocalDate(rs, "ACTOR_UPDATED_AT"),
                            null
                    );

                    roles.add(new Role(
                            UuidUtil.fromRawHex(rs.getString("ID")),
                            rs.getString("NAME"),
                            ResultSetUtil.getLocalDate(rs, "CREATED_AT"),
                            ResultSetUtil.getLocalDate(rs, "UPDATED_AT"),
                            null,
                            actor
                    ));
                }
            }
        }

        return roles;
    }

    private Movie mapMovie(final ResultSet rs) throws SQLException {
        return new Movie(
                UuidUtil.fromRawHex(rs.getString("ID")),
                rs.getString("TITLE"),
                rs.getString("SYNOPSIS"),
                getInteger(rs, "DURATION"),
                ResultSetUtil.getLocalDate(rs, "START_DATE"),
                ResultSetUtil.getLocalDate(rs, "END_DATE"),
                getPgRating(rs.getString("PG_RATING")),
                rs.getString("LANGUAGE"),
                rs.getString("TRAILER_URL"),
                rs.getString("DIRECTOR"),
                ResultSetUtil.getLocalDate(rs, "CREATED_AT"),
                ResultSetUtil.getLocalDate(rs, "UPDATED_AT"),
                null,
                null,
                null,
                null,
                null
        );
    }

    private int bindParameters(final PreparedStatement preparedStatement, final List<Object> parameters) throws SQLException {
        int index = 1;
        for (Object parameter : parameters) {
            if (parameter instanceof String stringValue) {
                preparedStatement.setString(index++, stringValue);
            } else if (parameter instanceof Timestamp timestamp) {
                preparedStatement.setTimestamp(index++, timestamp);
            } else {
                preparedStatement.setObject(index++, parameter);
            }
        }
        return index;
    }

    private void setTimestamp(final PreparedStatement preparedStatement, final int parameterIndex, final LocalDate value)
            throws SQLException {
        if (value == null) {
            preparedStatement.setNull(parameterIndex, Types.TIMESTAMP);
            return;
        }

        preparedStatement.setTimestamp(parameterIndex, Timestamp.valueOf(value.atStartOfDay()));
    }

    private void setInteger(final PreparedStatement preparedStatement, final int parameterIndex, final Integer value)
            throws SQLException {
        if (value == null) {
            preparedStatement.setNull(parameterIndex, Types.INTEGER);
            return;
        }

        preparedStatement.setInt(parameterIndex, value);
    }

    private Integer getInteger(final ResultSet rs, final String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    private PgRating getPgRating(final String value) {
        return value != null ? PgRating.valueOf(value) : null;
    }

    private String getPgRatingName(final Movie movie) {
        return movie.getPgRating() != null ? movie.getPgRating().name() : null;
    }

    private record QueryParts(String whereClause, List<Object> parameters) {
    }
}
