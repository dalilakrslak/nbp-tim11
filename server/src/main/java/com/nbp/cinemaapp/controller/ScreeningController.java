package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.dto.SeatAvailability;
import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.service.ScreeningService;
import com.nbp.cinemaapp.util.Pagination;
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
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(final ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Screening> getScreeningById(@PathVariable final UUID id) {
        return screeningService.getScreeningById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<Screening>> getAllScreenings(
            @RequestParam(required = false) final UUID movieId,
            @RequestParam(required = false) final String city,
            @RequestParam(required = false) final String cinema,
            @RequestParam(required = false) final LocalDate date,
            @ModelAttribute final Pagination pagination
    ) {

        return ResponseEntity.ok(screeningService.getAllScreenings(movieId, city, cinema, date, pagination));
    }

    @GetMapping("/{screeningId}/seats")
    public ResponseEntity<List<SeatAvailability>> getSeatsForScreening(@PathVariable final UUID screeningId) {

        return ResponseEntity.ok(screeningService.getSeatsForScreening(screeningId));
    }

    @GetMapping("/{screeningId}/seats/{seatId}")
    public ResponseEntity<Boolean> isSeatTaken(
            @PathVariable final UUID screeningId,
            @PathVariable final UUID seatId) {

        return ResponseEntity.ok(screeningService.isSeatTaken(screeningId, seatId));
    }
}
