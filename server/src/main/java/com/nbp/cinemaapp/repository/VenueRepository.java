package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Location;
import com.nbp.cinemaapp.entity.Venue;
import com.nbp.cinemaapp.util.ResultSetUtil;
import com.nbp.cinemaapp.util.UuidUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
public class VenueRepository {

    private static final String BASE_SELECT = """
        SELECT RAWTOHEX(V.ID) AS ID,
               V.NAME,
               V.STREET,
               V.IMAGE_URL,
               V.CREATED_AT,
               V.UPDATED_AT,
               RAWTOHEX(L.ID) AS LOCATION_ID,
               L.CITY,
               L.COUNTRY,
               L.CREATED_AT AS LOCATION_CREATED_AT,
               L.UPDATED_AT AS LOCATION_UPDATED_AT
        FROM VENUES V
        JOIN LOCATIONS L ON V.LOCATION_ID = L.ID
        """;

    private static final String ORDER_BY_NAME = """
        ORDER BY V.NAME
        """;

    private static final String COUNT_ALL = """
        SELECT COUNT(*)
        FROM VENUES
        """;

    private static final String FIND_BY_ID_SQL = BASE_SELECT + """
        WHERE RAWTOHEX(V.ID) = ?
        """;

    private static final String DELETE_BY_ID_SQL = """
        DELETE FROM VENUES
        WHERE RAWTOHEX(ID) = ?
        """;

    private static final String INSERT_SQL = """
        INSERT INTO VENUES (
            ID,
            NAME,
            STREET,
            IMAGE_URL,
            CREATED_AT,
            UPDATED_AT,
            LOCATION_ID
        )
        VALUES (
            HEXTORAW(?),
            ?,
            ?,
            ?,
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP,
            HEXTORAW(?)
        )
        """;

    private static final String UPDATE_SQL = """
        UPDATE VENUES
        SET NAME = ?,
            STREET = ?,
            IMAGE_URL = ?,
            UPDATED_AT = CURRENT_TIMESTAMP,
            LOCATION_ID = HEXTORAW(?)
        WHERE RAWTOHEX(ID) = ?
        """;

    private final DataSource dataSource;

    public VenueRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Venue> findAll() {
        String sql = BASE_SELECT + ORDER_BY_NAME;
        List<Venue> venues = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery()
        ) {
            while (rs.next()) {
                venues.add(mapVenue(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch venues", e);
        }

        return venues;
    }

    public Page<Venue> findAll(final Pageable pageable) {
        String sql = BASE_SELECT + ORDER_BY_NAME + """
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
            """;

        List<Venue> venues = new ArrayList<>();
        long total = 0;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setLong(1, pageable.getOffset());
            preparedStatement.setInt(2, pageable.getPageSize());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    venues.add(mapVenue(rs));
                }
            }

            try (PreparedStatement countStatement = connection.prepareStatement(COUNT_ALL);
                 ResultSet countRs = countStatement.executeQuery()) {
                if (countRs.next()) {
                    total = countRs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch paged venues", e);
        }

        return new PageImpl<>(venues, pageable, total);
    }

    public Optional<Venue> findById(final UUID id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapVenue(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch venue by id: " + id, e);
        }

        return Optional.empty();
    }

    public Venue save(final Venue venue) {
        if (venue.getId() == null) {
            UUID id = UUID.randomUUID();
            insert(id, venue);

            return findById(id)
                    .orElseThrow(() -> new IllegalStateException("Venue was inserted but could not be retrieved"));
        }

        update(venue);

        return findById(venue.getId())
                .orElseThrow(() -> new IllegalStateException("Venue was updated but could not be retrieved"));
    }

    public boolean deleteById(final UUID id) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete venue by id: " + id, e);
        }
    }

    private void insert(final UUID id, final Venue venue) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)
        ) {
            preparedStatement.setString(1, UuidUtil.toRawHex(id));
            preparedStatement.setString(2, venue.getName());
            preparedStatement.setString(3, venue.getStreet());
            preparedStatement.setString(4, venue.getImageUrl());
            preparedStatement.setString(5, UuidUtil.toRawHex(venue.getLocation().getId()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert venue", e);
        }
    }

    private void update(final Venue venue) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)
        ) {
            preparedStatement.setString(1, venue.getName());
            preparedStatement.setString(2, venue.getStreet());
            preparedStatement.setString(3, venue.getImageUrl());
            preparedStatement.setString(4, UuidUtil.toRawHex(venue.getLocation().getId()));
            preparedStatement.setString(5, UuidUtil.toRawHex(venue.getId()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update venue with id: " + venue.getId(), e);
        }
    }

    private Venue mapVenue(final ResultSet rs) throws SQLException {
        Location location = new Location(
                UuidUtil.fromRawHex(rs.getString("LOCATION_ID")),
                rs.getString("CITY"),
                rs.getString("COUNTRY"),
                ResultSetUtil.getLocalDate(rs, "LOCATION_CREATED_AT"),
                ResultSetUtil.getLocalDate(rs, "LOCATION_UPDATED_AT"),
                null
        );

        return new Venue(
                UuidUtil.fromRawHex(rs.getString("ID")),
                rs.getString("NAME"),
                rs.getString("STREET"),
                rs.getString("IMAGE_URL"),
                ResultSetUtil.getLocalDate(rs, "CREATED_AT"),
                ResultSetUtil.getLocalDate(rs, "UPDATED_AT"),
                location,
                null
        );
    }
}