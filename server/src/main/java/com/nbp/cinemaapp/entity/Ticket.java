package com.nbp.cinemaapp.entity;

import com.nbp.cinemaapp.enums.TicketStatus;
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
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column
    @CreationTimestamp
    private LocalDate createdAt;

    @Column
    @UpdateTimestamp
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @OneToMany(mappedBy = "ticket")
    private Set<SeatBooking> seatBookings;

    public Ticket() {}

    public Ticket(final UUID id,
                  final Integer price,
                  final TicketStatus status,
                  final LocalDate bookingDate,
                  final LocalDate createdAt,
                  final LocalDate updatedAt,
                  final User user,
                  final Screening screening) {
        this.id = id;
        this.price = price;
        this.status = status;
        this.bookingDate = bookingDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.screening = screening;
    }

    public UUID getId() {
        return id;
    }

    public Integer getPrice() {
        return price;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    public Screening getScreening() {
        return screening;
    }

    public Set<SeatBooking> getSeatBookings() {
        return seatBookings;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public void setPrice(final Integer price) {
        this.price = price;
    }

    public void setStatus(final TicketStatus status) {
        this.status = status;
    }

    public void setBookingDate(final LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public void setScreening(final Screening screening) {
        this.screening = screening;
    }

    public void setSeatBookings(final Set<SeatBooking> seatBookings) {
        this.seatBookings = seatBookings;
    }
}
