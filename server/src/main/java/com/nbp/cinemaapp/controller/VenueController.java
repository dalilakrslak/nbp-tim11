package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.dto.request.VenueRequest;
import com.nbp.cinemaapp.dto.response.VenueResponse;
import com.nbp.cinemaapp.entity.Venue;
import com.nbp.cinemaapp.service.VenueService;
import com.nbp.cinemaapp.util.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/venues")
public class VenueController {
    private final VenueService venueService;

    public VenueController(final VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping("/all")
    public List<Venue> getAllVenues() { return venueService.getAllVenues(); }

    @GetMapping
    public ResponseEntity<Page<Venue>> getVenues(@ModelAttribute final Pagination pagination) {
        return ResponseEntity.ok(venueService.getVenues(pagination.toPageable()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<VenueResponse> createVenue(@RequestBody VenueRequest request) {
        return ResponseEntity.ok(venueService.createVenue(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{venueId}")
    public ResponseEntity<VenueResponse> updateVenue(@PathVariable UUID venueId,
                                                     @RequestBody VenueRequest request) {
        return ResponseEntity.ok(venueService.updateVenue(venueId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{venueId}")
    public ResponseEntity<Void> deleteVenue(@PathVariable UUID venueId) {
        venueService.deleteVenue(venueId);
        return ResponseEntity.noContent().build();
    }
}