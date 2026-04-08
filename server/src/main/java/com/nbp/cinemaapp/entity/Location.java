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
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column
    @CreationTimestamp
    private LocalDate createdAt;

    @Column
    @UpdateTimestamp
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private List<Venue> venues;

    public Location() {}

    public Location(final UUID id,
                    final String city,
                    final String country,
                    final LocalDate createdAt,
                    final LocalDate updatedAt,
                    final List<Venue> venues) {
        this.id = id;
        this.city = city;
        this.country = country;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.venues = venues;
    }

    public UUID getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public List<Venue> getVenues() {
        return venues;
    }
}
