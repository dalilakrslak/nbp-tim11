package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Hall;
import com.nbp.cinemaapp.entity.Movie;
import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.util.ResultSetUtil;
import com.nbp.cinemaapp.util.UuidUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ScreeningRepository {

    private static final String BASE_SELECT = """
        SELECT RAWTOHEX(S.ID) AS ID,
               RAWTOHEX(S.MOVIE_ID) AS MOVIE_ID,
               RAWTOHEX(S.HALL_ID) AS HALL_ID,
               S.START_TIME,
               S.CREATED_AT,
               S.UPDATED_AT
        FROM SCREENINGS S
        JOIN HALLS H ON S.HALL_ID = H.ID
        JOIN VENUES V ON H.VENUE_ID = V.ID
        JOIN LOCATIONS L ON V.LOCATION_ID = L.ID
        WHERE 1 = 1
        """;

    private static final String BASE_COUNT = """
        SELECT COUNT(*)
        FROM SCREENINGS S
        JOIN HALLS H ON S.HALL_ID = H.ID
        JOIN VENUES V ON H.VENUE_ID = V.ID
        JOIN LOCATIONS L ON V.LOCATION_ID = L.ID
        WHERE 1 = 1
        """;

    private final DataSource dataSource;

    public ScreeningRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<Screening> findById(final UUID id) {
        final String sql = """
            SELECT RAWTOHEX(S.ID) AS ID,
                   RAWTOHEX(S.MOVIE_ID) AS MOVIE_ID,
                   RAWTOHEX(S.HALL_ID) AS HALL_ID,
                   S.START_TIME,
                   S.CREATED_AT,
                   S.UPDATED_AT
            FROM SCREENINGS S
            WHERE RAWTOHEX(S.ID) = ?
            """;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapScreening(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch screening by id: " + id, e);
        }

        return Optional.empty();
    }

    public List<Screening> findByMovieId(final UUID movieId) {
        final String sql = """
            SELECT RAWTOHEX(S.ID) AS ID,
                   RAWTOHEX(S.MOVIE_ID) AS MOVIE_ID,
                   RAWTOHEX(S.HALL_ID) AS HALL_ID,
                   S.START_TIME,
                   S.CREATED_AT,
                   S.UPDATED_AT
            FROM SCREENINGS S
            WHERE RAWTOHEX(S.MOVIE_ID) = ?
            ORDER BY S.START_TIME
            """;

        final List<Screening> screenings = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(movieId));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    screenings.add(mapScreening(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch screenings by movie id: " + movieId, e);
        }

        return screenings;
    }

    public Page<Screening> findAllFiltered(final UUID movieId,
                                           final String city,
                                           final String cinema,
                                           final java.time.LocalDate date,
                                           final Pageable pageable) {

        final StringBuilder selectSql = new StringBuilder(BASE_SELECT);
        final List<Object> selectParams = new ArrayList<>();
        appendFilters(selectSql, selectParams, movieId, city, cinema, date);

        final StringBuilder countSql = new StringBuilder(BASE_COUNT);
        final List<Object> countParams = new ArrayList<>();
        appendFilters(countSql, countParams, movieId, city, cinema, date);

        selectSql.append("""
            ORDER BY S.START_TIME
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
            """);

        final List<Screening> screenings = new ArrayList<>();
        long total = 0;

        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql.toString())) {
                int index = setParams(preparedStatement, selectParams);
                preparedStatement.setLong(index++, pageable.getOffset());
                preparedStatement.setInt(index, pageable.getPageSize());

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        screenings.add(mapScreening(rs));
                    }
                }
            }

            try (PreparedStatement countStatement = connection.prepareStatement(countSql.toString())) {
                setParams(countStatement, countParams);

                try (ResultSet rs = countStatement.executeQuery()) {
                    if (rs.next()) {
                        total = rs.getLong(1);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch filtered screenings", e);
        }

        return new PageImpl<>(screenings, pageable, total);
    }

    private void appendFilters(final StringBuilder sql,
                               final List<Object> params,
                               final UUID movieId,
                               final String city,
                               final String cinema,
                               final java.time.LocalDate date) {
        if (movieId != null) {
            sql.append(" AND RAWTOHEX(S.MOVIE_ID) = ? ");
            params.add(UuidUtil.toRawHex(movieId));
        }

        if (city != null && !city.isBlank()) {
            sql.append(" AND LOWER(L.CITY) = LOWER(?) ");
            params.add(city);
        }

        if (cinema != null && !cinema.isBlank()) {
            sql.append(" AND LOWER(V.NAME) = LOWER(?) ");
            params.add(cinema);
        }

        if (date != null) {
            sql.append(" AND TRUNC(S.START_TIME) = ? ");
            params.add(Date.valueOf(date));
        }
    }

    private int setParams(final PreparedStatement preparedStatement, final List<Object> params) throws SQLException {
        int index = 1;

        for (Object param : params) {
            if (param instanceof String stringValue) {
                preparedStatement.setString(index++, stringValue);
            } else if (param instanceof Date dateValue) {
                preparedStatement.setDate(index++, dateValue);
            } else {
                preparedStatement.setObject(index++, param);
            }
        }

        return index;
    }

    private Screening mapScreening(final ResultSet rs) throws SQLException {
        final Movie movie = new Movie();
        movie.setId(UuidUtil.fromRawHex(rs.getString("MOVIE_ID")));

        final Hall hall = new Hall();
        hall.setId(UuidUtil.fromRawHex(rs.getString("HALL_ID")));

        return new Screening(
                UuidUtil.fromRawHex(rs.getString("ID")),
                movie,
                hall,
                ResultSetUtil.getLocalDateTime(rs, "START_TIME"),
                ResultSetUtil.getLocalDate(rs, "CREATED_AT"),
                ResultSetUtil.getLocalDate(rs, "UPDATED_AT")
        );
    }
}