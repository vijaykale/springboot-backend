package com.turf.turf_management.service;

import com.turf.turf_management.dto.contribution.ContributionPaymentDTO;
import com.turf.turf_management.dto.contribution.ContributionResponseDTO;
import com.turf.turf_management.model.Contribution;

import java.util.List;

public interface ContributionService {
    String generateContribution(String bookingId);

    List<Contribution> getByBooking(String bookingId);

    List<Contribution> getByPlayer(String playerId);

    Contribution payContribution(String contributionId, double amount);

    String deleteContribution(String contributionId);

    double getTotalPaid(String bookingId);

    double getTotalRemaining(String bookingId);

    void recalculateContribution(String bookingId);
}
