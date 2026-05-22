package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.dto.response.MovieCatalogResponse;
import com.nbp.cinemaapp.dto.response.ScreeningAvailabilityResponse;
import com.nbp.cinemaapp.dto.response.TicketSalesReportResponse;
import com.nbp.cinemaapp.service.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Operacije za pregled izvještaja i agregiranih podataka iz sistema")
public class ReportsController {

    private final ReportsService reportsService;

    public ReportsController(final ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @Operation(
            summary = "Dohvaća katalog filmova",
            description = "Vraća listu filmova sa osnovnim informacijama, naslovnom slikom, žanrovima, glumcima i piscima."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Katalog filmova uspješno vraćen"),
            @ApiResponse(responseCode = "500", description = "Greška prilikom dohvaćanja kataloga filmova")
    })
    @GetMapping("/movie-catalog")
    public List<MovieCatalogResponse> getMovieCatalog() {
        return reportsService.getMovieCatalog();
    }

    @Operation(
            summary = "Dohvaća dostupnost projekcija",
            description = "Vraća pregled projekcija sa informacijama o filmu, vremenu prikazivanja, lokaciji, broju sjedišta, zauzetim sjedištima, slobodnim sjedištima i procentu popunjenosti."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pregled dostupnosti projekcija uspješno vraćen"),
            @ApiResponse(responseCode = "500", description = "Greška prilikom dohvaćanja dostupnosti projekcija")
    })
    @GetMapping("/screening-availability")
    public List<ScreeningAvailabilityResponse> getScreeningAvailability() {
        return reportsService.getScreeningAvailability();
    }

    @Operation(
            summary = "Dohvaća izvještaj o prodaji karata",
            description = "Vraća agregirani izvještaj o prodaji karata po datumu, kinu, sali, filmu i statusu karte, uključujući broj karata, broj rezervisanih sjedišta, ukupan prihod, prosječnu cijenu karte i broj jedinstvenih korisnika."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Izvještaj o prodaji karata uspješno vraćen"),
            @ApiResponse(responseCode = "500", description = "Greška prilikom dohvaćanja izvještaja o prodaji karata")
    })
    @GetMapping("/ticket-sales")
    public List<TicketSalesReportResponse> getTicketSalesReport() {
        return reportsService.getTicketSalesReport();
    }
}