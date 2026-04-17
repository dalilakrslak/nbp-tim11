package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.entity.Location;
import com.nbp.cinemaapp.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@Tag(name = "Locations", description = "Operacije za pregled dostupnih lokacija")
public class LocationController {

    private final LocationService locationService;

    public LocationController(final LocationService locationService) {
        this.locationService = locationService;
    }

    @Operation(
            summary = "Dohvaća sve lokacije",
            description = "Vraća listu svih dostupnih lokacija u sistemu."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista lokacija uspješno vraćena"),
            @ApiResponse(responseCode = "500", description = "Greška prilikom dohvaćanja lokacija")
    })
    @GetMapping("/all")
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }
}