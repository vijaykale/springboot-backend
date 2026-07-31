package com.turf.turf_management.service.impl;

import com.turf.turf_management.dto.payment.BookingPaymentSummaryDTO;
import com.turf.turf_management.dto.payment.PaymentRequestDTO;
import com.turf.turf_management.enums.PaymentStatus;
import com.turf.turf_management.enums.SequenceConstants;
import com.turf.turf_management.exception.ResourceNotFoundException;
import com.turf.turf_management.model.Contribution;
import com.turf.turf_management.model.Payment;
import com.turf.turf_management.model.Player;
import com.turf.turf_management.repository.ContributionRepository;
import com.turf.turf_management.repository.PaymentRepository;
import com.turf.turf_management.repository.PlayerRepository;
import com.turf.turf_management.service.NotificationService;
import com.turf.turf_management.service.PaymentService;
import com.turf.turf_management.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final PlayerRepository playerRepository;
    private final ContributionRepository contributionRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public Payment makePayment(PaymentRequestDTO request) {
        Contribution contribution = contributionRepository
                .findByContributionId(request.getContributionId())
                .orElseThrow(() -> new ResourceNotFoundException("Contribution not found"));

        double newPaid = contribution.getAmountPaid() + request.getAmount();

        if (newPaid > contribution.getAmountDue()) {
            throw new RuntimeException("Payment exceeds due amount");
        }

        String paymentId = "PAY" +
                String.format("%03d",
                        sequenceGeneratorService.generateSequence(SequenceConstants.PAYMENT_SEQUENCE));


        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .contributionId(contribution.getContributionId())
                .bookingId(contribution.getBookingId())
                .playerId(contribution.getPlayerId())
                .playerName(contribution.getPlayerName())
                .amount(request.getAmount())
                .paymentMode(request.getPaymentMode())
                .reference(request.getReference())
                .paymentDate(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        contribution.setAmountPaid(newPaid);

        double remaining = contribution.getAmountDue() - newPaid;

        contribution.setRemainingAmount(remaining);

        if (remaining == 0) {
            contribution.setPaymentStatus(PaymentStatus.PAID);
        } else {
            contribution.setPaymentStatus(PaymentStatus.PARTIAL);
        }

        Player player = playerRepository.findByPlayerId(contribution.getPlayerId())
                .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        contributionRepository.save(contribution);

        notificationService.paymentReceived(
                player.getEmail(),
                player.getName(),
                request.getAmount()
        );

        return payment;
    }

    @Override
    public List<Payment> getPaymentsByContribution(String contributionId) {
        return paymentRepository.findByContributionId(contributionId);
    }

    @Override
    public List<Payment> getPaymentsByBooking(String bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    @Override
    public List<Payment> getPaymentsByPlayer(String playerId) {
        return paymentRepository.findByPlayerId(playerId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByPaymentDateDesc();
    }

    public BookingPaymentSummaryDTO getBookingSummary(String bookingId) {

        List<Contribution> contributions =
                contributionRepository.findByBookingIdAndActiveTrue(bookingId);

        double total = contributions.stream()
                .mapToDouble(Contribution::getAmountDue)
                .sum();

        double paid = contributions.stream()
                .mapToDouble(Contribution::getAmountPaid)
                .sum();

        double remaining = contributions.stream()
                .mapToDouble(Contribution::getRemainingAmount)
                .sum();

        return BookingPaymentSummaryDTO.builder()
                .totalAmount(total)
                .totalPaid(paid)
                .totalRemaining(remaining)
                .build();
    }
}
