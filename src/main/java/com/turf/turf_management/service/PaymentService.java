package com.turf.turf_management.service;

import com.turf.turf_management.dto.payment.BookingPaymentSummaryDTO;
import com.turf.turf_management.dto.payment.PaymentRequestDTO;
import com.turf.turf_management.model.Payment;

import java.util.List;

public interface PaymentService {

    Payment makePayment(PaymentRequestDTO request);

    List<Payment> getPaymentsByContribution(String contributionId);

    List<Payment> getPaymentsByBooking(String bookingId);

    List<Payment> getPaymentsByPlayer(String playerId);

    List<Payment> getAllPayments();

    BookingPaymentSummaryDTO getBookingSummary(String bookingId);
}
