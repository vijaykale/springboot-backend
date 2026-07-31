package com.turf.turf_management.service.impl;

import com.turf.turf_management.dto.dashboard.BookingDashboardDTO;
import com.turf.turf_management.dto.dashboard.PlayerDashboardDTO;
import com.turf.turf_management.dto.dashboard.UpcomingBookingDTO;
import com.turf.turf_management.enums.AttendanceStatus;
import com.turf.turf_management.enums.PaymentStatus;
import com.turf.turf_management.exception.ResourceNotFoundException;
import com.turf.turf_management.model.Booking;
import com.turf.turf_management.model.Contribution;
import com.turf.turf_management.model.Player;
import com.turf.turf_management.repository.AttendanceRepository;
import com.turf.turf_management.repository.BookingRepository;
import com.turf.turf_management.repository.ContributionRepository;
import com.turf.turf_management.repository.PlayerRepository;
import com.turf.turf_management.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final BookingRepository bookingRepository;
    private final ContributionRepository contributionRepository;
    private final PlayerRepository playerRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public BookingDashboardDTO getBookingDashboard(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        List<Contribution> contributions = contributionRepository.findByBookingIdAndActiveTrue(bookingId);

        double totalPaid = contributions.stream().mapToDouble(Contribution::getAmountPaid).sum();

        double remainingAmount = contributions.stream().mapToDouble(Contribution::getRemainingAmount).sum();

        long paidPlayersCount = contributions.stream().filter(c -> c.getPaymentStatus() == PaymentStatus.PAID).count();

        long pendingPlayers = contributions.stream().filter(c -> c.getPaymentStatus() != PaymentStatus.PAID).count();

        double perPlayerContribution = booking.getPlayersPresent() == 0 ? 0 : booking.getTotalCost() / booking.getPlayersPresent();

        return BookingDashboardDTO.builder()
                .bookingId(booking.getBookingId())
                .turfName(booking.getTurfName())
                .totalCost(booking.getTotalCost())
                .playersPresent(booking.getPlayersPresent())
                .contributionPerPlayer(perPlayerContribution)
                .totalPaid(totalPaid)
                .remainingAmount(remainingAmount)
                .paidPlayers((int) paidPlayersCount)
                .pendingPlayers((int) pendingPlayers)
                .build();
    }

    @Override
    public PlayerDashboardDTO getPlayerDashboard(String playerId) {

        Player player = playerRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + playerId));

        List<Contribution> contributions = contributionRepository.findByPlayerIdAndActiveTrue(playerId);

        double totalPaid = contributions.stream().mapToDouble(Contribution::getAmountPaid).sum();

        double totalRemaining = contributions.stream().mapToDouble(Contribution::getRemainingAmount).sum();

        long matchesPlayed =
                attendanceRepository.countByPlayerIdAndStatus(playerId, AttendanceStatus.PRESENT);

        return PlayerDashboardDTO.builder()
                .playerId(player.getPlayerId())
                .name(player.getName())
                .matchesPlayed((int) matchesPlayed)
                .totalPaid(totalPaid)
                .totalOutstanding(totalRemaining)
                .build();
    }

    @Override
    public List<UpcomingBookingDTO> getUpcomingBookings() {
        List<Booking> bookings = bookingRepository.findAll();

        return bookings.stream()
                .filter(booking -> booking.getBookingDate().isAfter(LocalDate.now()))
                .map(booking -> UpcomingBookingDTO.builder()
                        .bookingId(booking.getBookingId())
                        .turfName(booking.getTurfName())
                        .bookingDate(booking.getBookingDate())
                        .startTime(booking.getStartTime())
                        .endTime(booking.getEndTime())
                        .duration(booking.getDurationHours())
                        .playerPresent(booking.getPlayersPresent())
                        .totalCost(booking.getTotalCost())
                        .build()
                ).collect(Collectors.toList());
    }
}
