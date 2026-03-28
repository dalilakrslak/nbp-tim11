package com.nbp.cinemaapp.specification;

import com.nbp.cinemaapp.entity.SeatBooking;
import com.nbp.cinemaapp.enums.TicketStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class SeatBookingSpecification {

    public static Specification<SeatBooking> hasScreeningIdAndTicketStatus(final UUID screeningId, final TicketStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (screeningId == null || status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("ticket").get("screening").get("id"), screeningId),
                    criteriaBuilder.equal(root.get("ticket").get("status"), status)
            );
        };
    }
}
