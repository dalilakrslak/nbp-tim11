package com.nbp.cinemaapp.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public class ScreeningRequest {
    private UUID movieId;
    private UUID hallId;
    private LocalDateTime startTime;

    public UUID getMovieId() {
        return movieId;
    }

    public void setMovieId(UUID movieId) {
        this.movieId = movieId;
    }

    public UUID getHallId() {
        return hallId;
    }

    public void setHallId(UUID hallId) {
        this.hallId = hallId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}