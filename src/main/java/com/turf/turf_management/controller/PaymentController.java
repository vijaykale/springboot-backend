package com.turf.turf_management.controller;

import com.turf.turf_management.dto.payment.BookingPaymentSummaryDTO;
import com.turf.turf_management.dto.payment.PaymentRequestDTO;
import com.turf.turf_management.model.Payment;
import com.turf.turf_management.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

  
    @PostMapping
    public Payment makePayment(@RequestBody PaymentRequestDTO request){
        return paymentService.makePayment(request);
    }

    @GetMapping("/summary/{bookingId}")
    public BookingPaymentSummaryDTO getSummary(@PathVariable String bookingId) {
        return paymentService.getBookingSummary(bookingId);
    }

    @GetMapping("/contribution/{contributionId}")
    public List<Payment> getByContribution(@PathVariable String contributionId){
        return paymentService.getPaymentsByContribution(contributionId);
    }


    @GetMapping("/booking/{bookingId}")
    public List<Payment> getByBooking(@PathVariable String bookingId){
        return paymentService.getPaymentsByBooking(bookingId);
    }

    
    @GetMapping("/player/{playerId}")
    public List<Payment> getByPlayer(@PathVariable String playerId){
        return paymentService.getPaymentsByPlayer(playerId);
    }


    @GetMapping
    public List<Payment> getAllPayments(){
        return paymentService.getAllPayments();
    }
}
