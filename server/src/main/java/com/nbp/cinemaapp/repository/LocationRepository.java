package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Location;
import com.nbp.cinemaapp.util.ResultSetUtil;
import com.nbp.cinemaapp.util.UuidUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LocationRepository {

    private final JdbcTemplate jdbcTemplate;

    public LocationRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Location> findAll() {
        String sql = """
            SELECT RAWTOHEX(ID) AS ID, CITY, COUNTRY, CREATED_AT, UPDATED_AT
            FROM LOCATIONS
            ORDER BY CITY
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapLocation(rs));
    }

    public Optional<Location> findById(final UUID id) {
        String sql = """
            SELECT RAWTOHEX(ID) AS ID, CITY, COUNTRY, CREATED_AT, UPDATED_AT
            FROM LOCATIONS
            WHERE RAWTOHEX(ID) = ?
            """;

        List<Location> results = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> mapLocation(rs),
                UuidUtil.toRawHex(id)
        );

        return results.stream().findFirst();
    }

    private Location mapLocation(final ResultSet rs) throws SQLException {
        return new Location(
                UuidUtil.fromRawHex(rs.getString("ID")),
                rs.getString("CITY"),
                rs.getString("COUNTRY"),
                ResultSetUtil.getLocalDate(rs, "CREATED_AT"),
                ResultSetUtil.getLocalDate(rs, "UPDATED_AT"),
                null
        );
    }
}