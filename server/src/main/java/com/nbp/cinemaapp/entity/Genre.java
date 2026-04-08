package com.nbp.cinemaapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    @CreationTimestamp
    private LocalDate createdAt;

    @Column
    @UpdateTimestamp
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "genre", orphanRemoval = true)
    @JsonIgnore
    private List<MovieGenre> movieGenres;

    public Genre() {}

    public Genre(final UUID id,
                 final String name,
                 final LocalDate createdAt,
                 final LocalDate updatedAt,
                 final List<MovieGenre> movieGenres) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.movieGenres = movieGenres;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public List<MovieGenre> getMovieGenres() {
        return movieGenres;
    }
}
