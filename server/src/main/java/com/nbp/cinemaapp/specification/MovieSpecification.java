package com.nbp.cinemaapp.specification;

import com.nbp.cinemaapp.entity.Genre;
import com.nbp.cinemaapp.entity.Location;
import com.nbp.cinemaapp.entity.Movie;
import com.nbp.cinemaapp.entity.MovieGenre;
import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.entity.Venue;
import jakarta.persistence.criteria.Join;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MovieSpecification {

    private static final Logger logger = LoggerFactory.getLogger(MovieSpecification.class);

    public static Specification<Movie> hasTitle(final String title) {
        return (root, query, criteriaBuilder) -> {

            if(title == null || title.isEmpty()){
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Movie> hasGenres(final List<String> genres){
        return (root, query, criteriaBuilder) -> {
            if (genres == null || genres.isEmpty()) {
                return criteriaBuilder.conjunction(); // don't filter anything
            }

            final Join<Movie, MovieGenre> movieGenreJoin = root.join("movieGenres");

            final Join<MovieGenre, Genre> genreJoin = movieGenreJoin.join("genre");

            return genreJoin.get("name").in(genres);
        };
    }

    public static Specification<Movie> hasCity(final String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null || city.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            final Join<Movie, Location> locationJoin = root
                    .join("screenings")
                    .join("hall")
                    .join("venue")
                    .join("location");

            query.distinct(true);

            return criteriaBuilder.like(
                    criteriaBuilder.lower(locationJoin.get("city")),
                    "%" + city.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Movie> hasCinema(final String cinema) {
        return (root, query, criteriaBuilder) -> {
            if (cinema == null || cinema.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            final Join<Movie, Venue> venueJoin = root
                    .join("screenings")
                    .join("hall")
                    .join("venue");

            query.distinct(true);

            return criteriaBuilder.like(
                    criteriaBuilder.lower(venueJoin.get("name")),
                    "%" + cinema.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Movie> hasProjectionTime(final LocalDateTime projectionTime) {
        return (root, query, criteriaBuilder) -> {
            if (projectionTime == null) {
                return criteriaBuilder.conjunction();
            }

            final Join<Movie, Screening> screeningJoin = root.join("screenings");

            return criteriaBuilder.equal(screeningJoin.get("startTime"), projectionTime);
        };
    }

    public static Specification<Movie> isShowingOnDate(final LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            final LocalDate targetDate = (date != null) ? date : LocalDate.now();

            return criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), targetDate),
                    criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), targetDate)
            );
        };
    }

    public static Specification<Movie> isUpcoming() {
        return (root, query, criteriaBuilder) -> {
            final LocalDate today = LocalDate.now();

            return criteriaBuilder.greaterThan(root.get("startDate"), today);
        };
    }
}
