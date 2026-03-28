package com.nbp.cinemaapp.service;

import com.nbp.cinemaapp.entity.Location;
import com.nbp.cinemaapp.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(final LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() { return locationRepository.findAll(); }
}