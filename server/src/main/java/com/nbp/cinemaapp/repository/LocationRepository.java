package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Location;
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
import java.util.Optional;
import java.util.UUID;

@Repository
public class LocationRepository {

    private static final String FIND_ALL_SQL = """
        SELECT RAWTOHEX(ID) AS ID, CITY, COUNTRY, CREATED_AT, UPDATED_AT
        FROM LOCATIONS
        ORDER BY CITY
        """;

    private static final String FIND_BY_ID_SQL = """
        SELECT RAWTOHEX(ID) AS ID, CITY, COUNTRY, CREATED_AT, UPDATED_AT
        FROM LOCATIONS
        WHERE RAWTOHEX(ID) = ?
        """;

    private final DataSource dataSource;

    public LocationRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Location> findAll() {
        List<Location> locations = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL);
                ResultSet rs = preparedStatement.executeQuery()
        ) {
            while (rs.next()) {
                locations.add(mapLocation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch locations", e);
        }

        return locations;
    }

    public Optional<Location> findById(final UUID id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapLocation(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch location by id: " + id, e);
        }

        return Optional.empty();
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