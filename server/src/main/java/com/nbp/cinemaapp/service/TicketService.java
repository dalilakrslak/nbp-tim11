package com.nbp.cinemaapp.service;

import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.entity.Seat;
import com.nbp.cinemaapp.entity.SeatBooking;
import com.nbp.cinemaapp.entity.Ticket;
import com.nbp.cinemaapp.entity.User;
import com.nbp.cinemaapp.enums.SystemRole;
import com.nbp.cinemaapp.enums.TicketStatus;
import com.nbp.cinemaapp.repository.ScreeningRepository;
import com.nbp.cinemaapp.repository.SeatBookingRepository;
import com.nbp.cinemaapp.repository.SeatRepository;
import com.nbp.cinemaapp.repository.TicketRepository;
import com.nbp.cinemaapp.repository.UserRepository;
import com.nbp.cinemaapp.specification.SeatBookingSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.GONE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final SeatBookingRepository seatBookingRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final TicketPdfService ticketPdfService;

    public TicketService(
            TicketRepository ticketRepository,
            SeatBookingRepository seatBookingRepository,
            ScreeningRepository screeningRepository,
            SeatRepository seatRepository,
            UserRepository userRepository,
            TicketPdfService ticketPdfService
    ) {
        this.ticketRepository = ticketRepository;
        this.seatBookingRepository = seatBookingRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.ticketPdfService = ticketPdfService;
    }

    @Transactional
    public Ticket createTicket(final UUID userId, final UUID screeningId, final Set<UUID> seatIds, final Integer price) {

        final Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new EntityNotFoundException("Screening not found"));

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        final List<Seat> selectedSeats = seatRepository.findAllById(seatIds);

        if (selectedSeats.size() != seatIds.size()) {
            throw new IllegalArgumentException("Some seats do not exist");
        }

        final UUID screeningHallId = screening.getHall().getId();
        final Boolean allSeatsMatchHall = selectedSeats.stream()
                .allMatch(seat -> seat.getHall().getId().equals(screeningHallId));

        if (!allSeatsMatchHall) {
            throw new IllegalArgumentException("Selected seats must belong to the same hall as the screening");
        }

        final List<SeatBooking> existingBookings =
                seatBookingRepository.findAllByScreeningIdAndTicketStatus(screeningId, TicketStatus.PURCHASED);

        final Set<UUID> alreadyTakenSeatIds = existingBookings.stream()
                .map(sb -> sb.getSeat().getId())
                .collect(Collectors.toSet());

        for (final UUID seatId : seatIds) {
            if (alreadyTakenSeatIds.contains(seatId)) {
                throw new IllegalArgumentException("Seat with ID " + seatId + " is already taken");
            }
        }

        final Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());
        ticket.setPrice(price);
        ticket.setStatus(TicketStatus.PURCHASED);
        ticket.setBookingDate(LocalDate.now());
        ticket.setUser(user);
        ticket.setScreening(screening);
        ticket.setTicketPdf(ticketPdfService.generateTicketPdf(ticket, selectedSeats));

        final Ticket savedTicket = ticketRepository.save(ticket);

        final Set<SeatBooking> seatBookings = selectedSeats.stream().map(seat -> {
            final SeatBooking sb = new SeatBooking();
            sb.setSeat(seat);
            sb.setTicket(savedTicket);
            return sb;
        }).collect(Collectors.toSet());

        seatBookingRepository.saveAll(seatBookings);

        return savedTicket;
    }

    public TicketDownloadResponse getTicketPdf(final UUID ticketId, final String requesterEmail) {
        final TicketRepository.TicketPdfRecord ticketPdfRecord = ticketRepository.findTicketPdfById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Ticket not found"));

        if (ticketPdfRecord.pdfBytes() == null || ticketPdfRecord.pdfBytes().length == 0) {
            throw new ResponseStatusException(NOT_FOUND, "Ticket PDF not found");
        }

        final User requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new ResponseStatusException(GONE, "The user with specified email does not exist"));

        final boolean isOwner = requester.getId().equals(ticketPdfRecord.userId());
        final boolean isAdmin = requester.getRole() == SystemRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new ResponseStatusException(FORBIDDEN, "You are not allowed to download this ticket");
        }

        return new TicketDownloadResponse(
                ticketPdfRecord.ticketId(),
                ticketPdfRecord.pdfBytes()
        );
    }

    public record TicketDownloadResponse(UUID ticketId, byte[] pdfBytes) {
    }
}

