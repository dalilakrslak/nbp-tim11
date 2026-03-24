package com.nbp.cinemaapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "movie_photos")
public class MoviePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Boolean isCoverImage;

    @Column
    @CreationTimestamp
    private Instant createdAt;

    @Column
    @UpdateTimestamp
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @JsonIgnore
    private Movie movie;

    public MoviePhoto() {}

    public MoviePhoto(final UUID id,
                      final String url,
                      final Boolean isCoverImage,
                      final Instant createdAt,
                      final Instant updatedAt,
                      final Movie movie) {
        this.id = id;
        this.url = url;
        this.isCoverImage = isCoverImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.movie = movie;
    }

    public UUID getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getIsCoverImage() {
        return isCoverImage;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Movie getMovie() {
        return movie;
    }
}
