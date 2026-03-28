package com.nbp.cinemaapp.specification;

import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.entity.Venue;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class ScreeningSpecification {

    public static Specification<Screening> hasMovieId(final UUID movieId) {
        return (root, query, criteriaBuilder) -> {
            if (movieId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("movie").get("id"), movieId);
        };
    }

    public static Specification<Screening> hasCity(final String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null || city.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            final Join<Object, Object> locationJoin = root
                    .join("hall")
                    .join("venue")
                    .join("location");

            return criteriaBuilder.like(
                    criteriaBuilder.lower(locationJoin.get("city")),
                    "%" + city.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Screening> hasCinema(final String cinema) {
        return (root, query, criteriaBuilder) -> {
            if (cinema == null || cinema.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            final Join<Object, Venue> venueJoin = root
                    .join("hall")
                    .join("venue");

            return criteriaBuilder.like(
                    criteriaBuilder.lower(venueJoin.get("name")),
                    "%" + cinema.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Screening> hasDate(final LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);

            return criteriaBuilder.between(root.get("startTime"), startOfDay, endOfDay);
        };
    }
}
