package com.nbp.cinemaapp.dto.response;

import com.nbp.cinemaapp.enums.PgRating;

import java.util.UUID;

public class MovieResponse {
    private UUID id;
    private String title;
    private Integer duration;
    private PgRating pgRating;
    private String language;
    private String director;
    private String trailerUrl;

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public PgRating getPgRating() {
        return pgRating;
    }

    public void setPgRating(PgRating pgRating) {
        this.pgRating = pgRating;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
}
