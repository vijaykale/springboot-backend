package com.turf.turf_management.dto.contribution;

import com.turf.turf_management.enums.PaymentStatus;
import lombok.Data;

@Data
public class ContributionResponseDTO {
    private String contributionId;

    private String bookingId;

    private String playerId;

    private String playerName;

    private double amountDue;

    private double amountPaid;

    private double remainingAmount;

    private PaymentStatus paymentStatus;
}
