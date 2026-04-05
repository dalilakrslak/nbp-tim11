package com.nbp.cinemaapp.service;

import com.nbp.cinemaapp.dto.request.ChargeRequest;
import com.nbp.cinemaapp.dto.response.ChargeResponse;
import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.entity.Seat;
import com.nbp.cinemaapp.repository.ScreeningRepository;
import com.nbp.cinemaapp.repository.SeatRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final TicketService ticketService;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;

    public PaymentService(final TicketService ticketService, final ScreeningRepository screeningRepository, final SeatRepository seatRepository) {
        this.ticketService = ticketService;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
    }

    public ChargeResponse chargeNewCard(final ChargeRequest request) throws StripeException {
        final Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", request.getAmount() * 100);
        chargeParams.put("currency", "USD");
        chargeParams.put("source", request.getToken());

        final Map<String, String> metadata = new HashMap<>();
        metadata.put("userEmail", request.getEmail());
        chargeParams.put("metadata", metadata);

        final Charge charge = Charge.create(chargeParams);

        if ("succeeded".equals(charge.getStatus())) {

            final Screening screening = screeningRepository.findById(request.getBookingDetails().getScreeningId())
                    .orElseThrow(() -> new EntityNotFoundException("Screening not found"));

            final UUID hallId = screening.getHall().getId();

            final List<Seat> seats = seatRepository.findBySeatCodeInAndHallId(request.getBookingDetails().getSelectedSeatCodes(), hallId);

            if (seats.size() != request.getBookingDetails().getSelectedSeatCodes().size()) {
                throw new IllegalArgumentException("Some seats are invalid or do not belong to the hall.");
            }

            final Set<UUID> seatIds = seats.stream()
                    .map(Seat::getId)
                    .collect(Collectors.toSet());

            ticketService.createTicket(
                    request.getUserId(),
                    request.getBookingDetails().getScreeningId(),
                    seatIds,
                    request.getAmount()
            );
        }

        return new ChargeResponse(
                charge.getId(),
                charge.getStatus(),
                charge.getBalanceTransaction()
        );
    }

}
