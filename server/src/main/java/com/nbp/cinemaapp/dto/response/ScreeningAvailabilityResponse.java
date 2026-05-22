package com.nbp.cinemaapp.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ScreeningAvailabilityResponse {
    private String screeningId;
    private String movieTitle;
    private Integer duration;
    private String pgRating;
    private LocalDateTime startTime;
    private LocalDateTime estimatedEndTime;
    private String hallName;
    private String venueName;
    private String street;
    private String city;
    private String country;
    private Integer totalSeats;
    private Integer bookedSeats;
    private Integer availableSeats;
    private BigDecimal occupancyPercentage;

    public ScreeningAvailabilityResponse() {
    }

    public ScreeningAvailabilityResponse(final String screeningId,
                                         final String movieTitle,
                                         final Integer duration,
                                         final String pgRating,
                                         final LocalDateTime startTime,
                                         final LocalDateTime estimatedEndTime,
                                         final String hallName,
                                         final String venueName,
                                         final String street,
                                         final String city,
                                         final String country,
                                         final Integer totalSeats,
                                         final Integer bookedSeats,
                                         final Integer availableSeats,
                                         final BigDecimal occupancyPercentage) {
        this.screeningId = screeningId;
        this.movieTitle = movieTitle;
        this.duration = duration;
        this.pgRating = pgRating;
        this.startTime = startTime;
        this.estimatedEndTime = estimatedEndTime;
        this.hallName = hallName;
        this.venueName = venueName;
        this.street = street;
        this.city = city;
        this.country = country;
        this.totalSeats = totalSeats;
        this.bookedSeats = bookedSeats;
        this.availableSeats = availableSeats;
        this.occupancyPercentage = occupancyPercentage;
    }

    public String getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(final String screeningId) {
        this.screeningId = screeningId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(final String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(final Integer duration) {
        this.duration = duration;
    }

    public String getPgRating() {
        return pgRating;
    }

    public void setPgRating(final String pgRating) {
        this.pgRating = pgRating;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(final LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEstimatedEndTime() {
        return estimatedEndTime;
    }

    public void setEstimatedEndTime(final LocalDateTime estimatedEndTime) {
        this.estimatedEndTime = estimatedEndTime;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(final String hallName) {
        this.hallName = hallName;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(final String venueName) {
        this.venueName = venueName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(final Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(final Integer bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(final Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public BigDecimal getOccupancyPercentage() {
        return occupancyPercentage;
    }

    public void setOccupancyPercentage(final BigDecimal occupancyPercentage) {
        this.occupancyPercentage = occupancyPercentage;
    }
}