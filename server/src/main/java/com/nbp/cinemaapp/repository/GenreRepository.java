package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Genre;
import com.nbp.cinemaapp.util.ResultSetUtil;
import com.nbp.cinemaapp.util.UuidUtil;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GenreRepository {

    private static final String FIND_ALL_SQL = """
        SELECT RAWTOHEX(ID) AS ID, NAME, CREATED_AT, UPDATED_AT
        FROM GENRES
        ORDER BY NAME
        """;

    private final DataSource dataSource;

    public GenreRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Genre> findAll() {
        List<Genre> genres = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL);
                ResultSet rs = preparedStatement.executeQuery()
        ) {
            while (rs.next()) {
                genres.add(mapGenre(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch genres", e);
        }

        return genres;
    }

    private Genre mapGenre(final ResultSet rs) throws SQLException {
        return new Genre(
                UuidUtil.fromRawHex(rs.getString("ID")),
                rs.getString("NAME"),
                ResultSetUtil.getLocalDate(rs, "CREATED_AT"),
                ResultSetUtil.getLocalDate(rs, "UPDATED_AT"),
                null
        );
    }
}