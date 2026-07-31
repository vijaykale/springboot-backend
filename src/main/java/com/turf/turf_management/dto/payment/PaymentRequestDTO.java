package com.turf.turf_management.dto.payment;

import com.turf.turf_management.enums.PaymentMode;
import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String contributionId;

    private double amount;

    private PaymentMode paymentMode;

    private String reference;

}
