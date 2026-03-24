package com.nbp.cinemaapp.entity;

import com.nbp.cinemaapp.enums.PgRating;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column
    private String synopsis;

    @Column
    private Integer duration;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column
    @Enumerated(EnumType.STRING)
    private PgRating pgRating;

    @Column
    private String language;

    @Column
    private String trailerUrl;

    @Column
    private String director;

    @Column
    @CreationTimestamp
    private Instant createdAt;

    @Column
    @UpdateTimestamp
    private Instant updatedAt;

    @OneToMany(mappedBy = "movie", orphanRemoval = true)
    private List<MovieGenre> movieGenres;

    @OneToMany(mappedBy = "movie", orphanRemoval = true)
    private List<MoviePhoto> photos;

    @OneToMany(mappedBy = "movie", orphanRemoval = true)
    @OrderBy("startTime ASC")
    private List<Screening> screenings;

    @OneToMany(mappedBy = "movie", orphanRemoval = true)
    private List<MovieWriter> movieWriters;

    @OneToMany(mappedBy = "movie", orphanRemoval = true)
    private List<Role> roles;

    public Movie() {}

    public Movie(final UUID id,
                 final String title,
                 final String synopsis,
                 final Integer duration,
                 final LocalDate startDate,
                 final LocalDate endDate,
                 final PgRating pgRating,
                 final String language,
                 final String trailerUrl,
                 final String director,
                 final Instant createdAt,
                 final Instant updatedAt,
                 final List<MovieGenre> movieGenres,
                 final List<MoviePhoto> photos,
                 final List<Screening> screenings,
                 final List<MovieWriter> movieWriters,
                 final List<Role> roles) {
        this.id = id;
        this.title = title;
        this.synopsis = synopsis;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pgRating = pgRating;
        this.language = language;
        this.trailerUrl = trailerUrl;
        this.director = director;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.movieGenres = movieGenres;
        this.photos = photos;
        this.screenings = screenings;
        this.movieWriters = movieWriters;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public Integer getDuration() {
        return duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public PgRating getPgRating() {
        return pgRating;
    }

    public String getLanguage() {
        return language;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public String getDirector() {
        return director;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public List<MovieGenre> getMovieGenres() {
        return movieGenres;
    }

    public List<MoviePhoto> getPhotos() {
        return photos;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public List<MovieWriter> getMovieWriters() {
        return movieWriters;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setPgRating(PgRating pgRating) {
        this.pgRating = pgRating;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
}
