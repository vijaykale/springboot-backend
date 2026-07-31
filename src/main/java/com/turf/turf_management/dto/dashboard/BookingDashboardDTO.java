package com.turf.turf_management.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingDashboardDTO {
    private String bookingId;

    private String turfName;

    private double totalCost;

    private int playersPresent;

    private double contributionPerPlayer;

    private double totalPaid;

    private double remainingAmount;

    private int paidPlayers;

    private int pendingPlayers;
}
