package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.Location;
import com.nbp.cinemaapp.entity.Venue;
import com.nbp.cinemaapp.util.ResultSetUtil;
import com.nbp.cinemaapp.util.UuidUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    private final JdbcTemplate jdbcTemplate;

    public VenueRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Venue> findAll() {
        String sql = BASE_SELECT + ORDER_BY_NAME;

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapVenue(rs));
    }

    public Page<Venue> findAll(final Pageable pageable) {
        String sql = BASE_SELECT + ORDER_BY_NAME + """
            OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
            """;

        List<Venue> venues = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> mapVenue(rs),
                pageable.getOffset(),
                pageable.getPageSize()
        );

        Long total = jdbcTemplate.queryForObject(COUNT_ALL, Long.class);

        return new PageImpl<>(venues, pageable, total != null ? total : 0);
    }

    public Optional<Venue> findById(final UUID id) {
        String sql = BASE_SELECT + """
            WHERE RAWTOHEX(V.ID) = ?
            """;

        List<Venue> venues = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> mapVenue(rs),
                UuidUtil.toRawHex(id)
        );

        return venues.stream().findFirst();
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

    public void deleteById(final UUID id) {
        String sql = """
            DELETE FROM VENUES
            WHERE RAWTOHEX(ID) = ?
            """;

        jdbcTemplate.update(sql, UuidUtil.toRawHex(id));
    }

    private void insert(final UUID id, final Venue venue) {
        String sql = """
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

        jdbcTemplate.update(
                sql,
                UuidUtil.toRawHex(id),
                venue.getName(),
                venue.getStreet(),
                venue.getImageUrl(),
                UuidUtil.toRawHex(venue.getLocation().getId())
        );
    }

    private void update(final Venue venue) {
        String sql = """
            UPDATE VENUES
            SET NAME = ?,
                STREET = ?,
                IMAGE_URL = ?,
                UPDATED_AT = CURRENT_TIMESTAMP,
                LOCATION_ID = HEXTORAW(?)
            WHERE RAWTOHEX(ID) = ?
            """;

        jdbcTemplate.update(
                sql,
                venue.getName(),
                venue.getStreet(),
                venue.getImageUrl(),
                UuidUtil.toRawHex(venue.getLocation().getId()),
                UuidUtil.toRawHex(venue.getId())
        );
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