package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.dto.response.MovieCatalogResponse;
import com.nbp.cinemaapp.dto.response.ScreeningAvailabilityResponse;
import com.nbp.cinemaapp.dto.response.TicketSalesReportResponse;
import com.nbp.cinemaapp.util.ResultSetUtil;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReportsRepository {

    private static final String FIND_MOVIE_CATALOG_SQL = """
        SELECT MOVIE_ID,
               TITLE,
               SYNOPSIS,
               DURATION,
               LANGUAGE,
               PG_RATING,
               DIRECTOR,
               TRAILER_URL,
               START_DATE,
               END_DATE,
               COVER_IMAGE_URL,
               GENRES,
               CAST_MEMBERS,
               WRITERS
        FROM VW_MOVIE_CATALOG
        ORDER BY TITLE
        """;

    private static final String FIND_SCREENING_AVAILABILITY_SQL = """
        SELECT SCREENING_ID,
               MOVIE_TITLE,
               DURATION,
               PG_RATING,
               START_TIME,
               ESTIMATED_END_TIME,
               HALL_NAME,
               VENUE_NAME,
               STREET,
               CITY,
               COUNTRY,
               TOTAL_SEATS,
               BOOKED_SEATS,
               AVAILABLE_SEATS,
               OCCUPANCY_PERCENTAGE
        FROM VW_SCREENING_AVAILABILITY
        ORDER BY START_TIME
        """;

    private static final String FIND_TICKET_SALES_REPORT_SQL = """
        SELECT SALE_DATE,
               VENUE_NAME,
               HALL_NAME,
               MOVIE_TITLE,
               STATUS,
               TICKET_COUNT,
               BOOKED_SEAT_COUNT,
               TOTAL_REVENUE,
               AVERAGE_TICKET_PRICE,
               UNIQUE_CUSTOMERS
        FROM VW_TICKET_SALES_REPORT
        ORDER BY SALE_DATE DESC, VENUE_NAME, HALL_NAME, MOVIE_TITLE
        """;

    private final DataSource dataSource;

    public ReportsRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<MovieCatalogResponse> findMovieCatalog() {
        List<MovieCatalogResponse> movies = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(FIND_MOVIE_CATALOG_SQL);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                movies.add(new MovieCatalogResponse(
                        rs.getString("MOVIE_ID"),
                        rs.getString("TITLE"),
                        rs.getString("SYNOPSIS"),
                        getInteger(rs, "DURATION"),
                        rs.getString("LANGUAGE"),
                        rs.getString("PG_RATING"),
                        rs.getString("DIRECTOR"),
                        rs.getString("TRAILER_URL"),
                        ResultSetUtil.getLocalDate(rs, "START_DATE"),
                        ResultSetUtil.getLocalDate(rs, "END_DATE"),
                        rs.getString("COVER_IMAGE_URL"),
                        rs.getString("GENRES"),
                        rs.getString("CAST_MEMBERS"),
                        rs.getString("WRITERS")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch movie catalog report", e);
        }

        return movies;
    }

    public List<ScreeningAvailabilityResponse> findScreeningAvailability() {
        List<ScreeningAvailabilityResponse> screenings = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(FIND_SCREENING_AVAILABILITY_SQL);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                screenings.add(new ScreeningAvailabilityResponse(
                        rs.getString("SCREENING_ID"),
                        rs.getString("MOVIE_TITLE"),
                        getInteger(rs, "DURATION"),
                        rs.getString("PG_RATING"),
                        ResultSetUtil.getLocalDateTime(rs, "START_TIME"),
                        ResultSetUtil.getLocalDateTime(rs, "ESTIMATED_END_TIME"),
                        rs.getString("HALL_NAME"),
                        rs.getString("VENUE_NAME"),
                        rs.getString("STREET"),
                        rs.getString("CITY"),
                        rs.getString("COUNTRY"),
                        getInteger(rs, "TOTAL_SEATS"),
                        getInteger(rs, "BOOKED_SEATS"),
                        getInteger(rs, "AVAILABLE_SEATS"),
                        rs.getBigDecimal("OCCUPANCY_PERCENTAGE")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch screening availability report", e);
        }

        return screenings;
    }

    public List<TicketSalesReportResponse> findTicketSalesReport() {
        List<TicketSalesReportResponse> reports = new ArrayList<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(FIND_TICKET_SALES_REPORT_SQL);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                reports.add(new TicketSalesReportResponse(
                        ResultSetUtil.getLocalDate(rs, "SALE_DATE"),
                        rs.getString("VENUE_NAME"),
                        rs.getString("HALL_NAME"),
                        rs.getString("MOVIE_TITLE"),
                        rs.getString("STATUS"),
                        getInteger(rs, "TICKET_COUNT"),
                        getInteger(rs, "BOOKED_SEAT_COUNT"),
                        rs.getBigDecimal("TOTAL_REVENUE"),
                        rs.getBigDecimal("AVERAGE_TICKET_PRICE"),
                        getInteger(rs, "UNIQUE_CUSTOMERS")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch ticket sales report", e);
        }

        return reports;
    }

    private Integer getInteger(final ResultSet rs, final String column) throws Exception {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }
}