package com.nbp.cinemaapp.service;

import com.nbp.cinemaapp.dto.MovieRating;
import com.nbp.cinemaapp.dto.request.MovieRequest;
import com.nbp.cinemaapp.dto.response.MovieResponse;
import com.nbp.cinemaapp.entity.Movie;
import com.nbp.cinemaapp.mapper.MovieMapper;
import com.nbp.cinemaapp.repository.MovieRepository;
import com.nbp.cinemaapp.specification.MovieSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;
    private final String omdbApiKey;
    private final MovieMapper movieMapper;

    public MovieService(final MovieRepository movieRepository,
                        final RestTemplate restTemplate,
                        @Value("${omdb.api.key}") final String omdbApiKey,
                        final MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.restTemplate = restTemplate;
        this.omdbApiKey = omdbApiKey;
        this.movieMapper = movieMapper;
    }

    public Movie getMovieById(final UUID movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie with ID " + movieId + " not found"));
    }

    public Page<Movie> getCurrentlyShowingMovies(final String title,
                                                 final List<String> genres,
                                                 final String city,
                                                 final String cinema,
                                                 final LocalDateTime projectionTime,
                                                 final LocalDate date,
                                                 final Pageable pageable) {

        final Specification<Movie> specification = Specification.where(MovieSpecification.hasTitle(title))
                .and(MovieSpecification.hasGenres(genres))
                .and(MovieSpecification.hasCity(city))
                .and(MovieSpecification.hasCinema(cinema))
                .and(MovieSpecification.hasProjectionTime(projectionTime))
                .and(MovieSpecification.isShowingOnDate(date));

        return movieRepository.findAll(specification, pageable);
    }

    public Page<Movie> getUpcomingMovies(final String title,
                                         final List<String> genres,
                                         final String city,
                                         final String cinema,
                                         final Pageable pageable) {

        final Specification<Movie> specification = Specification.where(MovieSpecification.hasTitle(title))
                .and(MovieSpecification.hasGenres(genres))
                .and(MovieSpecification.hasCity(city))
                .and(MovieSpecification.hasCinema(cinema))
                .and(MovieSpecification.isUpcoming());

        return movieRepository.findAll(specification, pageable);
    }

    public Page<Movie> getLatestMovies(final Integer size) {
        final Specification<Movie> specification = MovieSpecification.isShowingOnDate(null);

        final Pageable pageable = PageRequest.of(0, size, Sort.by("startDate").descending());

        return movieRepository.findAll(specification, pageable);
    }

    public Page<Movie> getSimilarMovies(final UUID movieId, final Pageable pageable) {
        final Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie with ID " + movieId + " not found"));

        final List<UUID> genreIds = movie.getMovieGenres().stream()
                .map(movieGenre -> movieGenre.getGenre().getId())
                .collect(Collectors.toList());

        if(genreIds.isEmpty()) {

            return Page.empty();
        }

        return movieRepository.findSimilarMovies(genreIds, movieId, pageable);
    }

    public List<MovieRating> getMovieRatings(final UUID movieId) {
        final Movie movie = getMovieById(movieId);
        final String movieTitle = movie.getTitle();

        // Build OMDB API URL with movie title and API key
        final String url = "http://www.omdbapi.com/?t=" + movieTitle + "&apikey=" + omdbApiKey;

        // Make HTTP request to OMDB API
        final Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        final List<MovieRating> ratings = new ArrayList<>();

        // OMDB API returns "Response": "True" for successful requests
        final boolean isSuccessfulFetch = response != null && "True".equals(response.get("Response"));

        if (isSuccessfulFetch && response.containsKey("Ratings")) {
            // Extract ratings array from API response
            final List<Map<String, String>> apiRatings = (List<Map<String, String>>) response.get("Ratings");

            // Process each rating from the API response
            for (Map<String, String> rating : apiRatings) {
                final MovieRating movieRating = new MovieRating(
                        rating.get("Source"),
                        rating.get("Value")
                );
                ratings.add(movieRating);
            }
        }

        return ratings;
    }

    public Page<Movie> getAllMovies(final Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public MovieResponse createMovie(MovieRequest request) {
        Movie movie = movieMapper.dtoToEntity(request);
        return movieMapper.entityToDto(movieRepository.save(movie));
    }

    public MovieResponse updateMovie(UUID movieId, MovieRequest request) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));

        movieMapper.updateEntity(movie, request);
        return movieMapper.entityToDto(movieRepository.save(movie));
    }

    public void deleteMovie(UUID movieId) {
        movieRepository.deleteById(movieId);
    }
}
