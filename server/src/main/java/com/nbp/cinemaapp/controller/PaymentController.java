package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.dto.request.ChargeRequest;
import com.nbp.cinemaapp.dto.response.ChargeResponse;
import com.nbp.cinemaapp.service.PaymentService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Operacije za obradu online plaćanja i naplatu karata")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(
            summary = "Izvršava plaćanje",
            description = "Vrši online naplatu korištenjem dostavljenih podataka o plaćanju i vraća rezultat transakcije."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plaćanje uspješno izvršeno"),
            @ApiResponse(responseCode = "400", description = "Neispravan zahtjev za plaćanje"),
            @ApiResponse(responseCode = "402", description = "Plaćanje odbijeno ili nije moguće izvršiti"),
            @ApiResponse(responseCode = "500", description = "Greška prilikom obrade plaćanja")
    })
    @PostMapping("/charge")
    public ResponseEntity<ChargeResponse> makePayment(@RequestBody final ChargeRequest request) throws StripeException {
        return ResponseEntity.ok(paymentService.chargeNewCard(request));
    }
}
