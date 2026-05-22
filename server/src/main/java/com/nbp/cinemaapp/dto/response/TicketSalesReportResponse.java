package com.nbp.cinemaapp.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TicketSalesReportResponse {

    private LocalDate saleDate;
    private String venueName;
    private String hallName;
    private String movieTitle;
    private String status;
    private Integer ticketCount;
    private Integer bookedSeatCount;
    private BigDecimal totalRevenue;
    private BigDecimal averageTicketPrice;
    private Integer uniqueCustomers;

    public TicketSalesReportResponse() {
    }

    public TicketSalesReportResponse(final LocalDate saleDate,
                                     final String venueName,
                                     final String hallName,
                                     final String movieTitle,
                                     final String status,
                                     final Integer ticketCount,
                                     final Integer bookedSeatCount,
                                     final BigDecimal totalRevenue,
                                     final BigDecimal averageTicketPrice,
                                     final Integer uniqueCustomers) {
        this.saleDate = saleDate;
        this.venueName = venueName;
        this.hallName = hallName;
        this.movieTitle = movieTitle;
        this.status = status;
        this.ticketCount = ticketCount;
        this.bookedSeatCount = bookedSeatCount;
        this.totalRevenue = totalRevenue;
        this.averageTicketPrice = averageTicketPrice;
        this.uniqueCustomers = uniqueCustomers;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(final LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(final String venueName) {
        this.venueName = venueName;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(final String hallName) {
        this.hallName = hallName;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(final String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Integer getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(final Integer ticketCount) {
        this.ticketCount = ticketCount;
    }

    public Integer getBookedSeatCount() {
        return bookedSeatCount;
    }

    public void setBookedSeatCount(final Integer bookedSeatCount) {
        this.bookedSeatCount = bookedSeatCount;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(final BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getAverageTicketPrice() {
        return averageTicketPrice;
    }

    public void setAverageTicketPrice(final BigDecimal averageTicketPrice) {
        this.averageTicketPrice = averageTicketPrice;
    }

    public Integer getUniqueCustomers() {
        return uniqueCustomers;
    }

    public void setUniqueCustomers(final Integer uniqueCustomers) {
        this.uniqueCustomers = uniqueCustomers;
    }
}