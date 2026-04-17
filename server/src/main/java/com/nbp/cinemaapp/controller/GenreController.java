package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.entity.Genre;
import com.nbp.cinemaapp.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@Tag(name = "Genres", description = "Operacije za pregled dostupnih filmskih žanrova")
public class GenreController {

    private final GenreService genreService;

    public GenreController(final GenreService genreService) {
        this.genreService = genreService;
    }

    @Operation(
            summary = "Dohvaća sve žanrove",
            description = "Vraća listu svih dostupnih filmskih žanrova u sistemu."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista žanrova uspješno vraćena"),
            @ApiResponse(responseCode = "500", description = "Greška prilikom dohvaćanja žanrova")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Genre>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }
}
