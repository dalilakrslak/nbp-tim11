package com.nbp.cinemaapp.service;

import com.nbp.cinemaapp.dto.SeatAvailability;
import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.entity.Seat;
import com.nbp.cinemaapp.entity.SeatBooking;
import com.nbp.cinemaapp.enums.TicketStatus;
import com.nbp.cinemaapp.repository.ScreeningRepository;
import com.nbp.cinemaapp.repository.SeatBookingRepository;
import com.nbp.cinemaapp.repository.SeatRepository;
import com.nbp.cinemaapp.util.Pagination;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
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
    private final SeatRepository seatRepository;

    public ScreeningService(final ScreeningRepository screeningRepository,
                            final SeatBookingRepository seatBookingRepository,
                            final SeatRepository seatRepository) {
        this.screeningRepository = screeningRepository;
        this.seatBookingRepository = seatBookingRepository;
        this.seatRepository = seatRepository;
    }

    public Optional<Screening> getScreeningById(final UUID screeningId) {
        return screeningRepository.findById(screeningId);
    }

    public Page<Screening> getAllScreenings(final UUID movieId,
                                            final String city,
                                            final String cinema,
                                            final LocalDate date,
                                            final Pagination pagination) {
        return screeningRepository.findAllFiltered(
                movieId,
                city,
                cinema,
                date,
                pagination.toPageable()
        );
    }

    public List<SeatAvailability> getSeatsForScreening(final UUID screeningId) {
        final List<SeatBooking> bookedSeats =
                seatBookingRepository.findAllByScreeningIdAndTicketStatus(screeningId, TicketStatus.PURCHASED);

        final Set<UUID> bookedSeatIds = bookedSeats.stream()
                .map(sb -> sb.getSeat().getId())
                .collect(Collectors.toSet());

        final Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new EntityNotFoundException("Screening not found"));

        final List<Seat> allSeats = seatRepository.findAllByHallId(screening.getHall().getId());

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