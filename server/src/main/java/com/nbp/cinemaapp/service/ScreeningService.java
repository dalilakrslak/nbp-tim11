package com.nbp.cinemaapp.service;

import com.nbp.cinemaapp.dto.SeatAvailability;
import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.entity.Seat;
import com.nbp.cinemaapp.entity.SeatBooking;
import com.nbp.cinemaapp.enums.TicketStatus;
import com.nbp.cinemaapp.repository.ScreeningRepository;
import com.nbp.cinemaapp.repository.SeatBookingRepository;
import com.nbp.cinemaapp.specification.ScreeningSpecification;
import com.nbp.cinemaapp.specification.SeatBookingSpecification;
import com.nbp.cinemaapp.util.Pagination;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final SeatBookingRepository seatBookingRepository;

    public ScreeningService(final ScreeningRepository screeningRepository, final SeatBookingRepository seatBookingRepository) {
        this.screeningRepository = screeningRepository;
        this.seatBookingRepository = seatBookingRepository;
    }

    public Optional<Screening> getScreeningById(final UUID screeningId) {
        return screeningRepository.findById(screeningId);
    }

    public Page<Screening> getAllScreenings(
            final UUID movieId,
            final String city,
            final String cinema,
            final LocalDate date,
            final Pagination pagination) {

        final Specification<Screening> specification = Specification.where(ScreeningSpecification.hasMovieId(movieId)
                .and(ScreeningSpecification.hasCity(city)
                .and(ScreeningSpecification.hasCinema(cinema))
                .and(ScreeningSpecification.hasDate(date))));

        return screeningRepository.findAll(specification, pagination.toPageable());
    }

    public List<SeatAvailability> getSeatsForScreening(final UUID screeningId) {
        // Find all SeatBooking for this screening with tickets that have a "taken" status (PURCHASED)
        final List<SeatBooking> bookedSeats = seatBookingRepository.findAll(
                SeatBookingSpecification.hasScreeningIdAndTicketStatus(screeningId, TicketStatus.PURCHASED)
        );

        // Collect booked seat IDs
        final Set<UUID> bookedSeatIds = bookedSeats.stream()
                .map(sb -> sb.getSeat().getId())
                .collect(Collectors.toSet());

        // Get the screening (to get the hall and all its seats)
        final Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new EntityNotFoundException("Screening not found"));

        final Set<Seat> allSeats = screening.getHall().getSeats();

        return allSeats.stream()
                .map(seat -> new SeatAvailability(
                        seat.getId(),
                        seat.getSeatCode(),
                        seat.getSeatType(),
                        bookedSeatIds.contains(seat.getId())
                ))
                .collect(Collectors.toList());
    }

    public Boolean isSeatTaken(final UUID screeningId, final UUID seatId) {

        return seatBookingRepository.existsBySeatIdAndTicketScreeningIdAndTicketStatus(
                seatId, screeningId, TicketStatus.PURCHASED
        );
    }

    public List<Screening> getScreeningsByMovieId(final UUID movieId) {

        return screeningRepository.findByMovieId(movieId);
    }
}
