package com.turf.turf_management.service.impl;

import com.turf.turf_management.enums.AttendanceStatus;
import com.turf.turf_management.enums.PaymentStatus;
import com.turf.turf_management.enums.SequenceConstants;
import com.turf.turf_management.exception.ResourceNotFoundException;
import com.turf.turf_management.model.Attendance;
import com.turf.turf_management.model.Booking;
import com.turf.turf_management.model.Contribution;
import com.turf.turf_management.model.Player;
import com.turf.turf_management.repository.AttendanceRepository;
import com.turf.turf_management.repository.BookingRepository;
import com.turf.turf_management.repository.ContributionRepository;
import com.turf.turf_management.repository.PlayerRepository;
import com.turf.turf_management.service.ContributionService;
import com.turf.turf_management.service.NotificationService;
import com.turf.turf_management.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributionServiceImpl implements ContributionService {

    private final ContributionRepository contributionRepository;
    private final BookingRepository bookingRepository;
    private final AttendanceRepository attendanceRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final NotificationService notificationService;
    private final PlayerRepository playerRepository;

    @Override
    public String generateContribution(String bookingId) {

        if (contributionRepository.existsByBookingIdAndActiveTrue(bookingId)) {
            throw new RuntimeException("Contribution already generated");
        }

        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // 🔥 GET ALL PRESENT PLAYERS
        List<Attendance> attendanceList =
                attendanceRepository.findByBookingIdAndStatus(
                        bookingId,
                        AttendanceStatus.PRESENT
                );

        if (attendanceList.isEmpty()) {
            throw new RuntimeException("No players attended");
        }

        int playersPresent = attendanceList.size();

        double perPlayerCost = booking.getTotalCost() / playersPresent;


        for (Attendance attendance : attendanceList) {

            Player player = playerRepository.findByPlayerId(attendance.getPlayerId())
                    .orElseThrow(() -> new RuntimeException("Player not found"));

            String contributionId = "CNT" +
                    String.format("%03d",
                            sequenceGeneratorService.generateSequence(SequenceConstants.CONTRIBUTION_SEQUENCE));

            Contribution contribution = Contribution.builder()
                    .contributionId(contributionId)
                    .bookingId(bookingId)
                    .playerId(attendance.getPlayerId())
                    .playerName(attendance.getPlayerName())
                    .amountDue(Math.ceil(perPlayerCost))
                    .amountPaid(0)
                    .remainingAmount(Math.ceil(perPlayerCost))
                    .paymentStatus(PaymentStatus.PENDING)
                    .active(true)
                    .build();

            contributionRepository.save(contribution);

            notificationService.contributionGenerated(
                    player.getEmail(),
                    player.getName(),
                    perPlayerCost,
                    booking.getTurfName(),
                    booking.getBookingDate().toString(),
                    booking.getStartTime().toString() + "-" + booking.getEndTime().toString()

            );
        }

        return "Contribution generated successfully";
    }

    @Override
    public List<Contribution> getByBooking(String bookingId) {
        return contributionRepository.findByBookingIdAndActiveTrue(bookingId);
    }

    @Override
    public List<Contribution> getByPlayer(String playerId) {
        return contributionRepository.findByPlayerIdAndActiveTrue(playerId);
    }

    @Override
    public Contribution payContribution(String contributionId, double amount) {

        if (amount <= 0) {
            throw new RuntimeException("Payment amount must be greater than zero");
        }

        Contribution contribution = contributionRepository
                .findByContributionId(contributionId)
                .orElseThrow(() -> new ResourceNotFoundException("Contribution not found"));

        double newPaid = contribution.getAmountPaid() + amount;

        if (newPaid > contribution.getAmountDue()) {
            throw new RuntimeException("Payment exceeds due amount");
        }

        contribution.setAmountPaid(newPaid);

        double remaining = contribution.getAmountDue() - newPaid;

        contribution.setRemainingAmount(remaining);

        if (remaining == 0) {
            contribution.setPaymentStatus(PaymentStatus.PAID);
        } else {
            contribution.setPaymentStatus(PaymentStatus.PARTIAL);
        }

        return contributionRepository.save(contribution);
    }

    @Override
    public String deleteContribution(String contributionId) {

        Contribution contribution = contributionRepository
                .findByContributionId(contributionId)
                .orElseThrow(() -> new ResourceNotFoundException("Contribution not found"));

        if (contribution.getAmountPaid() > 0) {
            throw new RuntimeException("Cannot delete contribution after payment");
        }

        contribution.setActive(false);
        contributionRepository.save(contribution);

        return "Contribution deleted successfully";
    }

    @Override
    public double getTotalPaid(String bookingId) {

        List<Contribution> contributions =
                contributionRepository.findByBookingIdAndActiveTrue(bookingId);

        return contributions.stream()
                .mapToDouble(Contribution::getAmountPaid)
                .sum();
    }

    @Override
    public double getTotalRemaining(String bookingId) {

        List<Contribution> contributions =
                contributionRepository.findByBookingIdAndActiveTrue(bookingId);

        return contributions.stream()
                .mapToDouble(Contribution::getRemainingAmount)
                .sum();
    }

    @Override
    public void recalculateContribution(String bookingId) {

        List<Contribution> contributions =
                contributionRepository.findByBookingIdAndActiveTrue(bookingId);

        if (contributions.isEmpty()) {
            return;
        }

        boolean paymentStarted = contributions.stream()
                .anyMatch(c -> c.getAmountPaid() > 0);

        if (paymentStarted) {
            throw new RuntimeException("Cannot recalculate contribution after payment started");
        }

        // deactivate old contributions
        contributions.forEach(c -> {
            c.setActive(false);
            contributionRepository.save(c);
        });

        // generate again
        generateContribution(bookingId);
    }
}
