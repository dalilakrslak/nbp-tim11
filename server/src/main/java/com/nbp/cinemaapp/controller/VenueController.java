package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.dto.request.VenueRequest;
import com.nbp.cinemaapp.dto.response.VenueResponse;
import com.nbp.cinemaapp.entity.Venue;
import com.nbp.cinemaapp.service.VenueService;
import com.nbp.cinemaapp.util.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/venues")
@Tag(name = "Venues", description = "Operacije za upravljanje kinima i lokacijama prikazivanja")
public class VenueController {
    private final VenueService venueService;

    public VenueController(final VenueService venueService) {
        this.venueService = venueService;
    }

    @Operation(
            summary = "Dohvaća sve venue zapise",
            description = "Vraća kompletnu listu svih kina bez paginacije."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista kina uspješno vraćena"),
            @ApiResponse(responseCode = "500", description = "Greška prilikom dohvaćanja kina")
    })
    @GetMapping("/all")
    public List<Venue> getAllVenues() {
        return venueService.getAllVenues();
    }

    @Operation(
            summary = "Dohvaća kina uz paginaciju",
            description = "Vraća stranicu kina na osnovu proslijeđenih parametara za paginaciju."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginisana lista kina uspješno vraćena"),
            @ApiResponse(responseCode = "400", description = "Neispravni parametri paginacije"),
            @ApiResponse(responseCode = "500", description = "Greška prilikom dohvaćanja kina")
    })
    @GetMapping
    public ResponseEntity<Page<Venue>> getVenues(@ModelAttribute final Pagination pagination) {
        return ResponseEntity.ok(venueService.getVenues(pagination.toPageable()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Kreira novo kino",
            description = "Kreira novo kino na osnovu dostavljenih podataka."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kino uspješno kreirano"),
            @ApiResponse(responseCode = "400", description = "Neispravan zahtjev za kreiranje kina"),
            @ApiResponse(responseCode = "500", description = "Greška prilikom kreiranja kina")
    })
    @PostMapping
    public ResponseEntity<VenueResponse> createVenue(@RequestBody VenueRequest request) {
        return ResponseEntity.ok(venueService.createVenue(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Ažurira postojeće kino",
            description = "Ažurira podatke o kinu na osnovu njegovog identifikatora."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kino uspješno ažurirano"),
            @ApiResponse(responseCode = "400", description = "Neispravan zahtjev za ažuriranje"),
            @ApiResponse(responseCode = "404", description = "Kino nije pronađeno"),
            @ApiResponse(responseCode = "500", description = "Greška prilikom ažuriranja kina")
    })
    @PutMapping("/{venueId}")
    public ResponseEntity<VenueResponse> updateVenue(@PathVariable UUID venueId,
                                                     @RequestBody VenueRequest request) {
        return ResponseEntity.ok(venueService.updateVenue(venueId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Briše kino",
            description = "Briše kino iz sistema na osnovu proslijeđenog identifikatora."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Kino uspješno obrisano"),
            @ApiResponse(responseCode = "404", description = "Kino nije pronađeno"),
            @ApiResponse(responseCode = "500", description = "Greška prilikom brisanja kina")
    })
    @DeleteMapping("/{venueId}")
    public ResponseEntity<Void> deleteVenue(@PathVariable UUID venueId) {
        venueService.deleteVenue(venueId);
        return ResponseEntity.noContent().build();
    }
}