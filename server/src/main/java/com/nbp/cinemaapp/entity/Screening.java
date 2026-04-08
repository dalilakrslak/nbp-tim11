package com.nbp.cinemaapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "screenings")
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @JsonIgnoreProperties({"screenings", "synopsis", "trailerUrl", "director", "createdAt", "updatedAt", "movieGenres", "movieWriters", "roles"})
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @Column(nullable = false)
    private LocalDate startTime;

    @Column
    @CreationTimestamp
    private LocalDate createdAt;

    @Column
    @UpdateTimestamp
    private LocalDate updatedAt;

    public Screening() {}

    public Screening(final UUID id,
                     final Movie movie,
                     final Hall hall,
                     final LocalDate startTime,
                     final LocalDate createdAt,
                     final LocalDate updatedAt) {
        this.id = id;
        this.movie = movie;
        this.hall = hall;
        this.startTime = startTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(final Movie movie) {
        this.movie = movie;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(final Hall hall) {
        this.hall = hall;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(final LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
