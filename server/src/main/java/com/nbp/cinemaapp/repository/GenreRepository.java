package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Genre;
import com.nbp.cinemaapp.util.ResultSetUtil;
import com.nbp.cinemaapp.util.UuidUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreRepository {

    private final JdbcTemplate jdbcTemplate;

    public GenreRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> findAll() {
        String sql = """
            SELECT RAWTOHEX(ID) AS ID, NAME, CREATED_AT, UPDATED_AT
            FROM GENRES
            ORDER BY NAME
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapGenre(rs));
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