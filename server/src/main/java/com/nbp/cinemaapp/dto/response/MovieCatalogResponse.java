package com.nbp.cinemaapp.dto.response;

import java.time.LocalDate;

public class MovieCatalogResponse {
    private String movieId;
    private String title;
    private String synopsis;
    private Integer duration;
    private String language;
    private String pgRating;
    private String director;
    private String trailerUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private String coverImageUrl;
    private String genres;
    private String castMembers;
    private String writers;

    public MovieCatalogResponse() {
    }

    public MovieCatalogResponse(final String movieId,
                                final String title,
                                final String synopsis,
                                final Integer duration,
                                final String language,
                                final String pgRating,
                                final String director,
                                final String trailerUrl,
                                final LocalDate startDate,
                                final LocalDate endDate,
                                final String coverImageUrl,
                                final String genres,
                                final String castMembers,
                                final String writers) {
        this.movieId = movieId;
        this.title = title;
        this.synopsis = synopsis;
        this.duration = duration;
        this.language = language;
        this.pgRating = pgRating;
        this.director = director;
        this.trailerUrl = trailerUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.coverImageUrl = coverImageUrl;
        this.genres = genres;
        this.castMembers = castMembers;
        this.writers = writers;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(final String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(final String synopsis) {
        this.synopsis = synopsis;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(final Integer duration) {
        this.duration = duration;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public String getPgRating() {
        return pgRating;
    }

    public void setPgRating(final String pgRating) {
        this.pgRating = pgRating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(final String director) {
        this.director = director;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(final String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(final String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(final String genres) {
        this.genres = genres;
    }

    public String getCastMembers() {
        return castMembers;
    }

    public void setCastMembers(final String castMembers) {
        this.castMembers = castMembers;
    }

    public String getWriters() {
        return writers;
    }

    public void setWriters(final String writers) {
        this.writers = writers;
    }
}