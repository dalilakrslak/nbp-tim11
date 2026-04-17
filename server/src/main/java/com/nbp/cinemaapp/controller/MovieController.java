package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.dto.MovieRating;
import com.nbp.cinemaapp.dto.request.MovieRequest;
import com.nbp.cinemaapp.dto.response.MovieResponse;
import com.nbp.cinemaapp.entity.Movie;
import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.service.MovieService;
import com.nbp.cinemaapp.service.ScreeningService;
import com.nbp.cinemaapp.util.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/movies")
@Tag(name = "Movies", description = "Operacije za pregled, pretragu i upravljanje filmovima")
public class MovieController {

    private final MovieService movieService;
    private final ScreeningService screeningService;

    public MovieController(final MovieService movieService, final ScreeningService screeningService) {
        this.movieService = movieService;
        this.screeningService = screeningService;
    }

    @Operation(
            summary = "Dohvaća film po ID-u",
            description = "Vraća detalje filma na osnovu njegovog UUID identifikatora."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Film uspješno pronađen"),
            @ApiResponse(responseCode = "404", description = "Film nije pronađen")
    })
    @GetMapping("/{movieId}")
    public ResponseEntity<Movie> getMovieById(
            @Parameter(description = "UUID identifikator filma", required = true)
            @PathVariable final UUID movieId) {
        return ResponseEntity.ok(movieService.getMovieById(movieId));
    }

    @Operation(
            summary = "Dohvaća filmove koji se trenutno prikazuju",
            description = "Vraća paginiranu listu filmova koji se trenutno prikazuju, uz opcionalno filtriranje po naslovu, žanru, gradu, kinu, vremenu projekcije i datumu."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista filmova uspješno vraćena"),
            @ApiResponse(responseCode = "400", description = "Neispravni parametri filtera ili paginacije")
    })
    @GetMapping("/currently-showing")
    public ResponseEntity<Page<Movie>> getCurrentlyShowing(
            @Parameter(description = "Naziv filma")
            @RequestParam(required = false) final String title,

            @Parameter(description = "Lista žanrova za filtriranje")
            @RequestParam(required = false) final List<String> genres,

            @Parameter(description = "Naziv grada")
            @RequestParam(required = false) final String city,

            @Parameter(description = "Naziv kina")
            @RequestParam(required = false) final String cinema,

            @Parameter(description = "Vrijeme projekcije", example = "2026-04-15T20:00:00")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            final LocalDateTime projectionTime,

            @Parameter(description = "Datum prikazivanja", example = "2026-04-15")
            @RequestParam(required = false) final LocalDate date,

            @ModelAttribute final Pagination pagination) {
        return ResponseEntity.ok(movieService.getCurrentlyShowingMovies(
                title,
                genres,
                city,
                cinema,
                projectionTime,
                date,
                pagination.toPageable()));
    }

    @Operation(
            summary = "Dohvaća nadolazeće filmove",
            description = "Vraća paginiranu listu nadolazećih filmova uz opcionalno filtriranje po naslovu, žanru, gradu i kinu."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista nadolazećih filmova uspješno vraćena"),
            @ApiResponse(responseCode = "400", description = "Neispravni parametri filtera ili paginacije")
    })
    @GetMapping("/upcoming")
    public ResponseEntity<Page<Movie>> getUpcoming(
            @Parameter(description = "Naziv filma")
            @RequestParam(required = false) final String title,

            @Parameter(description = "Lista žanrova za filtriranje")
            @RequestParam(required = false) final List<String> genres,

            @Parameter(description = "Naziv grada")
            @RequestParam(required = false) final String city,

            @Parameter(description = "Naziv kina")
            @RequestParam(required = false) final String cinema,

            @ModelAttribute final Pagination pagination) {
        return ResponseEntity.ok(movieService.getUpcomingMovies(
                title,
                genres,
                city,
                cinema,
                pagination.toPageable()));
    }

    @Operation(
            summary = "Dohvaća najnovije filmove",
            description = "Vraća ograničenu listu najnovije dodanih filmova."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista najnovijih filmova uspješno vraćena"),
            @ApiResponse(responseCode = "400", description = "Neispravna veličina liste")
    })
    @GetMapping("/latest")
    public ResponseEntity<Page<Movie>> getLatest(
            @Parameter(description = "Broj filmova koje treba vratiti", example = "3")
            @RequestParam(defaultValue = "3") final Integer size) {
        return ResponseEntity.ok(movieService.getLatestMovies(size));
    }

    @Operation(
            summary = "Dohvaća slične filmove",
            description = "Vraća paginiranu listu filmova sličnih odabranom filmu."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista sličnih filmova uspješno vraćena"),
            @ApiResponse(responseCode = "404", description = "Polazni film nije pronađen")
    })
    @GetMapping("/{movieId}/similar")
    public ResponseEntity<Page<Movie>> getSimilarMovies(
            @Parameter(description = "UUID identifikator filma", required = true)
            @PathVariable final UUID movieId,
            @ModelAttribute final Pagination pagination) {

        return ResponseEntity.ok(movieService.getSimilarMovies(movieId, pagination.toPageable()));
    }

    @Operation(
            summary = "Dohvaća ocjene filma",
            description = "Vraća listu ocjena i izvora ocjenjivanja za odabrani film."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ocjene filma uspješno vraćene"),
            @ApiResponse(responseCode = "404", description = "Film nije pronađen")
    })
    @GetMapping("/{movieId}/ratings")
    public ResponseEntity<List<MovieRating>> getMovieRatings(
            @Parameter(description = "UUID identifikator filma", required = true)
            @PathVariable final UUID movieId) {
        return ResponseEntity.ok(movieService.getMovieRatings(movieId));
    }

    @Operation(
            summary = "Dohvaća projekcije za film",
            description = "Vraća sve projekcije povezane sa odabranim filmom."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista projekcija uspješno vraćena"),
            @ApiResponse(responseCode = "404", description = "Film nije pronađen")
    })
    @GetMapping("/movies/{movieId}/screenings")
    public ResponseEntity<List<Screening>> getScreeningsByMovieId(
            @Parameter(description = "UUID identifikator filma", required = true)
            @PathVariable final UUID movieId) {
        return ResponseEntity.ok(screeningService.getScreeningsByMovieId(movieId));
    }

    @Operation(
            summary = "Dohvaća sve filmove",
            description = "Vraća paginiranu listu svih filmova. Dostupno samo administratorima."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista filmova uspješno vraćena"),
            @ApiResponse(responseCode = "403", description = "Pristup dozvoljen samo administratoru")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<Movie>> getAllMovies(@ModelAttribute final Pagination pagination) {
        return ResponseEntity.ok(movieService.getAllMovies(pagination.toPageable()));
    }

    @Operation(
            summary = "Kreira novi film",
            description = "Kreira novi film u sistemu. Dostupno samo administratorima."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Film uspješno kreiran"),
            @ApiResponse(responseCode = "400", description = "Neispravni podaci za kreiranje filma"),
            @ApiResponse(responseCode = "403", description = "Pristup dozvoljen samo administratoru")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(@RequestBody MovieRequest request) {
        return ResponseEntity.ok(movieService.createMovie(request));
    }

    @Operation(
            summary = "Ažurira postojeći film",
            description = "Ažurira podatke o filmu na osnovu njegovog identifikatora. Dostupno samo administratorima."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Film uspješno ažuriran"),
            @ApiResponse(responseCode = "400", description = "Neispravni podaci za ažuriranje filma"),
            @ApiResponse(responseCode = "403", description = "Pristup dozvoljen samo administratoru"),
            @ApiResponse(responseCode = "404", description = "Film nije pronađen")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{movieId}")
    public ResponseEntity<MovieResponse> updateMovie(
            @Parameter(description = "UUID identifikator filma", required = true)
            @PathVariable UUID movieId,
            @RequestBody MovieRequest request) {
        return ResponseEntity.ok(movieService.updateMovie(movieId, request));
    }

    @Operation(
            summary = "Briše film",
            description = "Briše film iz sistema na osnovu identifikatora. Dostupno samo administratorima."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Film uspješno obrisan"),
            @ApiResponse(responseCode = "403", description = "Pristup dozvoljen samo administratoru"),
            @ApiResponse(responseCode = "404", description = "Film nije pronađen")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(
            @Parameter(description = "UUID identifikator filma", required = true)
            @PathVariable UUID movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }
}