package com.nbp.cinemaapp.repository;

import com.nbp.cinemaapp.entity.SeatBooking;
import com.nbp.cinemaapp.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeatBookingRepository extends JpaRepository<SeatBooking, UUID>, JpaSpecificationExecutor<SeatBooking> {

    boolean existsBySeatIdAndTicketScreeningIdAndTicketStatus(final UUID seatId, final UUID screeningId, final TicketStatus status);
}
