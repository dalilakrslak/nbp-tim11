package com.nbp.cinemaapp.service;

import com.nbp.cinemaapp.dto.MovieRating;
import com.nbp.cinemaapp.dto.response.MovieBulkImportResponse;
import com.nbp.cinemaapp.dto.request.MovieRequest;
import com.nbp.cinemaapp.dto.response.MovieResponse;
import com.nbp.cinemaapp.entity.Movie;
import com.nbp.cinemaapp.mapper.MovieMapper;
import com.nbp.cinemaapp.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
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
        return movieRepository.findCurrentlyShowing(title, genres, city, cinema, projectionTime, date, pageable);
    }

    public Page<Movie> getUpcomingMovies(final String title,
                                         final List<String> genres,
                                         final String city,
                                         final String cinema,
                                         final Pageable pageable) {
        return movieRepository.findUpcoming(title, genres, city, cinema, pageable);
    }

    public Page<Movie> getLatestMovies(final Integer size) {
        return movieRepository.findLatest(size);
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

    public MovieBulkImportResponse importMoviesFromXml(final MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("XML file is required");
        }

        final String xmlContent;
        try {
            xmlContent = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Failed to read XML file", e);
        }

        validateXml(xmlContent);

        final int importedMovies = movieRepository.bulkImportMoviesFromXml(xmlContent);
        if (importedMovies == 0) {
            throw new IllegalArgumentException("XML file does not contain any movie entries");
        }

        return new MovieBulkImportResponse(importedMovies);
    }

    private void validateXml(final String xmlContent) {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.newDocumentBuilder().parse(new InputSource(new StringReader(xmlContent)));
        } catch (final Exception e) {
            throw new IllegalArgumentException("Invalid XML file", e);
        }
    }
}
