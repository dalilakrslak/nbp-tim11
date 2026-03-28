package com.nbp.cinemaapp.dto.request;

import com.nbp.cinemaapp.enums.PgRating;

public class MovieRequest {
    private String title;
    private Integer duration;
    private PgRating pgRating;
    private String language;
    private String director;
    private String trailerUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public PgRating getPgRating() {
        return pgRating;
    }

    public void setPgRating(PgRating pgRating) {
        this.pgRating = pgRating;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
}
