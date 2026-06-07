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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
        final Document document;
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xmlContent)));
        } catch (final Exception e) {
            throw new IllegalArgumentException("Invalid XML file", e);
        }

        validateRequiredImportFields(document);
    }

    private void validateRequiredImportFields(final Document document) {
        final NodeList movies = document.getElementsByTagNameNS("*", "movie");
        if (movies.getLength() == 0) {
            throw new IllegalArgumentException("XML file does not contain any movie entries");
        }

        for (int i = 0; i < movies.getLength(); i++) {
            final Element movie = (Element) movies.item(i);
            final String title = requireDirectText(movie, "title", "Each movie must include title.");

            requireDirectText(movie, "duration", "Movie '" + title + "' must include duration.");
            requireDirectText(movie, "pgRating", "Movie '" + title + "' must include pgRating.");
            requireDirectText(movie, "language", "Movie '" + title + "' must include language.");
            requireDirectText(movie, "trailerUrl", "Movie '" + title + "' must include trailerUrl.");
            requireDirectText(movie, "director", "Movie '" + title + "' must include director.");
            validateMovieGenres(movie, title);
            validateMovieWriters(movie, title);
            validateMoviePhotos(movie, title);
        }
    }

    private void validateMovieGenres(final Element movie, final String title) {
        final Element genres = getDirectChild(movie, "genres");
        if (genres == null) {
            throw new IllegalArgumentException("Movie '" + title + "' must include at least one genre.");
        }

        final List<Element> genreElements = getDirectChildren(genres, "genre");
        if (genreElements.isEmpty()) {
            throw new IllegalArgumentException("Movie '" + title + "' must include at least one genre.");
        }

        for (final Element genre : genreElements) {
            final String genreName = firstNonBlank(
                    getDirectText(genre, "name"),
                    genre.getAttribute("name"),
                    genre.getTextContent()
            );
            if (genreName == null) {
                throw new IllegalArgumentException("Each genre for movie '" + title + "' must include a name.");
            }
        }
    }

    private void validateMovieWriters(final Element movie, final String title) {
        final Element writers = getDirectChild(movie, "writers");
        if (writers == null) {
            throw new IllegalArgumentException("Movie '" + title + "' must include at least one writer.");
        }

        final List<Element> writerElements = getDirectChildren(writers, "writer");
        if (writerElements.isEmpty()) {
            throw new IllegalArgumentException("Movie '" + title + "' must include at least one writer.");
        }

        for (final Element writer : writerElements) {
            final String firstName = firstNonBlank(
                    getDirectText(writer, "firstName"),
                    getDirectText(writer, "first_name"),
                    writer.getAttribute("firstName"),
                    writer.getAttribute("first_name")
            );
            final String lastName = firstNonBlank(
                    getDirectText(writer, "lastName"),
                    getDirectText(writer, "last_name"),
                    writer.getAttribute("lastName"),
                    writer.getAttribute("last_name")
            );
            final String fullName = firstNonBlank(
                    getDirectText(writer, "fullName"),
                    writer.getAttribute("fullName"),
                    writer.getTextContent()
            );

            if ((firstName == null || lastName == null) && !hasFirstAndLastName(fullName)) {
                throw new IllegalArgumentException(
                        "Each writer for movie '" + title + "' must include firstName and lastName, or a fullName containing both names."
                );
            }
        }
    }

    private void validateMoviePhotos(final Element movie, final String title) {
        final Element photos = getDirectChild(movie, "photos");
        if (photos == null) {
            return;
        }

        for (final Element photo : getDirectChildren(photos, "photo")) {
            final String url = firstNonBlank(getDirectText(photo, "url"), photo.getAttribute("url"));
            if (url == null) {
                throw new IllegalArgumentException("Each photo for movie '" + title + "' must include a url.");
            }
        }
    }

    private String requireDirectText(final Element element, final String childName, final String message) {
        final String value = getDirectText(element, childName);
        if (value == null) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    private String getDirectText(final Element element, final String childName) {
        final Element child = getDirectChild(element, childName);
        return child == null ? null : firstNonBlank(child.getTextContent());
    }

    private Element getDirectChild(final Element element, final String childName) {
        final NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);
            if (child instanceof Element childElement && childName.equals(childElement.getLocalName())) {
                return childElement;
            }
        }

        return null;
    }

    private List<Element> getDirectChildren(final Element element, final String childName) {
        final List<Element> matchingChildren = new ArrayList<>();
        final NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);
            if (child instanceof Element childElement && childName.equals(childElement.getLocalName())) {
                matchingChildren.add(childElement);
            }
        }

        return matchingChildren;
    }

    private String firstNonBlank(final String... values) {
        for (final String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }

        return null;
    }

    private boolean hasFirstAndLastName(final String fullName) {
        return fullName != null && fullName.trim().contains(" ");
    }
}
