package com.nbp.cinemaapp.mapper;

import com.nbp.cinemaapp.dto.request.VenueRequest;
import com.nbp.cinemaapp.dto.response.VenueResponse;
import com.nbp.cinemaapp.entity.Location;
import com.nbp.cinemaapp.entity.Venue;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {

    public Venue dtoToEntity(VenueRequest venueRequest, Location location) {
        Venue venue = new Venue();
        venue.setName(venueRequest.getName());
        venue.setStreet(venueRequest.getStreet());
        venue.setImageUrl(venueRequest.getImageUrl());
        venue.setLocation(location);
        return venue;
    }

    public void updateEntity(Venue venue, VenueRequest venueRequest, Location location) {
        venue.setName(venueRequest.getName());
        venue.setStreet(venueRequest.getStreet());
        venue.setImageUrl(venueRequest.getImageUrl());
        venue.setLocation(location);
    }

    public VenueResponse entityToDto(Venue venue) {
        VenueResponse venueResponse = new VenueResponse();
        venueResponse.setId(venue.getId());
        venueResponse.setName(venue.getName());
        venueResponse.setStreet(venue.getStreet());
        venueResponse.setImageUrl(venue.getImageUrl());

        if (venue.getLocation() != null) {
            venueResponse.setCity(venue.getLocation().getCity());
            venueResponse.setCountry(venue.getLocation().getCountry());
        }

        return venueResponse;
    }
}