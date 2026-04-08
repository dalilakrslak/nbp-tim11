package com.nbp.cinemaapp.service;

import com.nbp.cinemaapp.dto.request.VenueRequest;
import com.nbp.cinemaapp.dto.response.VenueResponse;
import com.nbp.cinemaapp.entity.Location;
import com.nbp.cinemaapp.entity.Venue;
import com.nbp.cinemaapp.mapper.VenueMapper;
import com.nbp.cinemaapp.repository.LocationRepository;
import com.nbp.cinemaapp.repository.VenueRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VenueService {
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;
    private final LocationRepository locationRepository;

    public VenueService(final VenueRepository venueRepository,
                        final VenueMapper venueMapper,
                        final LocationRepository locationRepository) {
        this.venueRepository = venueRepository;
        this.venueMapper = venueMapper;
        this.locationRepository = locationRepository;
    }

    public List<Venue> getAllVenues() { return venueRepository.findAll(); }

    public Page<Venue> getVenues(final Pageable pageable) {
        return venueRepository.findAll(pageable);
    }

    public VenueResponse createVenue(VenueRequest request) {
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));

        Venue venue = venueMapper.dtoToEntity(request, location);
        return venueMapper.entityToDto(venueRepository.save(venue));
    }

    public VenueResponse updateVenue(UUID venueId, VenueRequest request) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new EntityNotFoundException("Venue not found"));

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));

        venueMapper.updateEntity(venue, request, location);
        return venueMapper.entityToDto(venueRepository.save(venue));
    }

    public void deleteVenue(final UUID venueId) {
        boolean deleted = venueRepository.deleteById(venueId);

        if (!deleted) {
            throw new EntityNotFoundException("Venue not found with id: " + venueId);
        }
    }
}