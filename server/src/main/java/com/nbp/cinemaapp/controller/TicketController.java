package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.dto.request.CreateTicketRequest;
import com.nbp.cinemaapp.dto.response.TicketResponse;
import com.nbp.cinemaapp.entity.Ticket;
import com.nbp.cinemaapp.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Tickets", description = "Operacije za kreiranje i upravljanje kino kartama")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(
            summary = "Kreira novu kartu",
            description = "Kreira novu kino kartu na osnovu korisnika, projekcije, odabranih sjedišta i cijene."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Karta uspješno kreirana"),
            @ApiResponse(responseCode = "400", description = "Neispravan zahtjev za kreiranje karte"),
            @ApiResponse(responseCode = "404", description = "Korisnik, projekcija ili sjedište nisu pronađeni"),
            @ApiResponse(responseCode = "409", description = "Jedno ili više sjedišta je već zauzeto"),
            @ApiResponse(responseCode = "500", description = "Greška prilikom kreiranja karte")
    })
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
