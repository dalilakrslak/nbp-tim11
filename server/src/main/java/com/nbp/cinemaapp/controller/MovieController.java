package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.dto.MovieRating;
import com.nbp.cinemaapp.dto.request.MovieRequest;
import com.nbp.cinemaapp.dto.response.MovieResponse;
import com.nbp.cinemaapp.entity.Movie;
import com.nbp.cinemaapp.entity.Screening;
import com.nbp.cinemaapp.service.MovieService;
import com.nbp.cinemaapp.service.ScreeningService;
import com.nbp.cinemaapp.util.Pagination;
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
public class MovieController {

    private final MovieService movieService;
    private final ScreeningService screeningService;

    public MovieController(final MovieService movieService, final ScreeningService screeningService) {
        this.movieService = movieService;
        this.screeningService = screeningService;
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<Movie> getMovieById(@PathVariable final UUID movieId) {
        return ResponseEntity.ok(movieService.getMovieById(movieId));
    }

    @GetMapping("/currently-showing")
    public ResponseEntity<Page<Movie>> getCurrentlyShowing(@RequestParam(required = false) final String title,
                                                           @RequestParam(required = false) final List<String> genres,
                                                           @RequestParam(required = false) final String city,
                                                           @RequestParam(required = false) final String cinema,
                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                               final LocalDateTime projectionTime,
                                                           @RequestParam(required = false) final LocalDate date,
                                                           @ModelAttribute final Pagination pagination) {
        return ResponseEntity.ok(movieService.getCurrentlyShowingMovies(title,
                genres,
                city,
                cinema,
                projectionTime,
                date,
                pagination.toPageable()));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Page<Movie>> getUpcoming(@RequestParam(required = false) final String title,
                                                   @RequestParam(required = false) final List<String> genres,
                                                   @RequestParam(required = false) final String city,
                                                   @RequestParam(required = false) final String cinema,
                                                   @ModelAttribute final Pagination pagination) {
        return ResponseEntity.ok(movieService.getUpcomingMovies(title,
                genres,
                city,
                cinema,
                pagination.toPageable()));
    }

    @GetMapping("/latest")
    public ResponseEntity<Page<Movie>> getLatest(@RequestParam(defaultValue = "3") final Integer size) {
        return ResponseEntity.ok(movieService.getLatestMovies(size));
    }

    @GetMapping("/{movieId}/similar")
    public ResponseEntity<Page<Movie>> getSimilarMovies(@PathVariable final UUID movieId,
                                                        @ModelAttribute final Pagination pagination) {

        return ResponseEntity.ok(movieService.getSimilarMovies(movieId, pagination.toPageable()));
    }

    @GetMapping("/{movieId}/ratings")
    public ResponseEntity<List<MovieRating>> getMovieRatings(@PathVariable final UUID movieId) {
        return ResponseEntity.ok(movieService.getMovieRatings(movieId));
    }

    @GetMapping("/movies/{movieId}/screenings")
    public ResponseEntity<List<Screening>> getScreeningsByMovieId(@PathVariable final UUID movieId) {
        return ResponseEntity.ok(screeningService.getScreeningsByMovieId(movieId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<Movie>> getAllMovies(@ModelAttribute final Pagination pagination) {
        return ResponseEntity.ok(movieService.getAllMovies(pagination.toPageable()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(@RequestBody MovieRequest request) {
        return ResponseEntity.ok(movieService.createMovie(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{movieId}")
    public ResponseEntity<MovieResponse> updateMovie(
            @PathVariable UUID movieId,
            @RequestBody MovieRequest request
    ) {
        return ResponseEntity.ok(movieService.updateMovie(movieId, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable UUID movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }
}
