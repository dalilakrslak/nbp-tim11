package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.dto.SeatAvailability;
import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.service.ScreeningService;
import com.nbp.cinemaapp.util.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/screenings")
@Tag(name = "Screenings", description = "Operacije za pregled projekcija i dostupnosti sjedišta")
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(final ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @Operation(
            summary = "Dohvaća projekciju po ID-u",
            description = "Vraća detalje jedne projekcije na osnovu njenog UUID identifikatora."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projekcija uspješno pronađena"),
            @ApiResponse(responseCode = "404", description = "Projekcija nije pronađena")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Screening> getScreeningById(
            @Parameter(description = "UUID identifikator projekcije", required = true)
            @PathVariable final UUID id) {
        return screeningService.getScreeningById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Dohvaća projekcije uz filtere",
            description = "Vraća paginiranu listu projekcija uz opcionalno filtriranje po filmu, gradu, kinu i datumu."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista projekcija uspješno vraćena"),
            @ApiResponse(responseCode = "400", description = "Neispravni parametri filtera ili paginacije")
    })
    @GetMapping
    public ResponseEntity<Page<Screening>> getAllScreenings(
            @Parameter(description = "UUID identifikator filma")
            @RequestParam(required = false) final UUID movieId,

            @Parameter(description = "Naziv grada u kojem se prikazuje film")
            @RequestParam(required = false) final String city,

            @Parameter(description = "Naziv kina")
            @RequestParam(required = false) final String cinema,

            @Parameter(description = "Datum projekcije", example = "2026-04-15")
            @RequestParam(required = false) final LocalDate date,

            @ModelAttribute final Pagination pagination
    ) {
        return ResponseEntity.ok(screeningService.getAllScreenings(movieId, city, cinema, date, pagination));
    }

    @Operation(
            summary = "Dohvaća raspoloživost sjedišta za projekciju",
            description = "Vraća listu svih sjedišta za odabranu projekciju zajedno sa informacijom da li su zauzeta."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista sjedišta uspješno vraćena"),
            @ApiResponse(responseCode = "404", description = "Projekcija nije pronađena")
    })
    @GetMapping("/{screeningId}/seats")
    public ResponseEntity<List<SeatAvailability>> getSeatsForScreening(
            @Parameter(description = "UUID identifikator projekcije", required = true)
            @PathVariable final UUID screeningId) {
        return ResponseEntity.ok(screeningService.getSeatsForScreening(screeningId));
    }

    @Operation(
            summary = "Provjerava da li je sjedište zauzeto",
            description = "Vraća informaciju da li je određeno sjedište zauzeto za konkretnu projekciju."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status sjedišta uspješno vraćen"),
            @ApiResponse(responseCode = "404", description = "Projekcija ili sjedište nisu pronađeni")
    })
    @GetMapping("/{screeningId}/seats/{seatId}")
    public ResponseEntity<Boolean> isSeatTaken(
            @Parameter(description = "UUID identifikator projekcije", required = true)
            @PathVariable final UUID screeningId,

            @Parameter(description = "UUID identifikator sjedišta", required = true)
            @PathVariable final UUID seatId) {
        return ResponseEntity.ok(screeningService.isSeatTaken(screeningId, seatId));
    }
}