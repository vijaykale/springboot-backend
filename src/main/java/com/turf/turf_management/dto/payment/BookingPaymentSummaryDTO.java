package com.turf.turf_management.dto.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingPaymentSummaryDTO {
    private double totalAmount;
    private double totalPaid;
    private double totalRemaining;
}
