package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.dto.request.CreateTicketRequest;
import com.nbp.cinemaapp.dto.response.TicketResponse;
import com.nbp.cinemaapp.entity.Ticket;
import com.nbp.cinemaapp.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/create-ticket")
    public ResponseEntity<TicketResponse> createTicket(@RequestBody CreateTicketRequest request) {
        final Ticket ticket = ticketService.createTicket(
                request.getUserId(),
                request.getScreeningId(),
                request.getSeatIds(),
                request.getPrice()
        );

        return ResponseEntity.status(201).body(new TicketResponse(ticket));
    }
}
