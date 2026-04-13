package com.nbp.cinemaapp.entity;

import com.nbp.cinemaapp.enums.SeatType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String seatCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @Column
    @CreationTimestamp
    private LocalDate createdAt;

    @Column
    @UpdateTimestamp
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @OneToMany(mappedBy = "seat")
    @JsonIgnore
    private Set<SeatBooking> seatBookings;

    public Seat() {}

    public Seat(final UUID id,
                final String seatCode,
                final SeatType seatType,
                final LocalDate createdAt,
                final LocalDate updatedAt,
                final Hall hall) {
        this.id = id;
        this.seatCode = seatCode;
        this.seatType = seatType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.hall = hall;
    }

    public UUID getId() {
        return id;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public Hall getHall() {
        return hall;
    }

    public void setId(final UUID id) {
        this.id = id;
    }
}
