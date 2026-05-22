package com.nbp.cinemaapp.service;

import com.nbp.cinemaapp.dto.response.MovieCatalogResponse;
import com.nbp.cinemaapp.dto.response.ScreeningAvailabilityResponse;
import com.nbp.cinemaapp.dto.response.TicketSalesReportResponse;
import com.nbp.cinemaapp.repository.ReportsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportsService {

    private final ReportsRepository reportsRepository;

    public ReportsService(final ReportsRepository reportsRepository) {
        this.reportsRepository = reportsRepository;
    }

    public List<MovieCatalogResponse> getMovieCatalog() {
        return reportsRepository.findMovieCatalog();
    }

    public List<ScreeningAvailabilityResponse> getScreeningAvailability() {
        return reportsRepository.findScreeningAvailability();
    }

    public List<TicketSalesReportResponse> getTicketSalesReport() {
        return reportsRepository.findTicketSalesReport();
    }
}