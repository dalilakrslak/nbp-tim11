package com.nbp.cinemaapp.controller;

import com.nbp.cinemaapp.dto.request.ChargeRequest;
import com.nbp.cinemaapp.dto.response.ChargeResponse;
import com.nbp.cinemaapp.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/charge")
    public ResponseEntity<ChargeResponse> makePayment(@RequestBody final ChargeRequest request) throws StripeException {
        return ResponseEntity.ok(paymentService.chargeNewCard(request));
    }
}
