package com.turf.turf_management.model;

import com.turf.turf_management.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;

    private String paymentId;

    private String contributionId;

    private String bookingId;

    private String playerId;

    private String playerName;

    private double amount;

    private PaymentMode paymentMode;

    private String reference;

    private LocalDateTime paymentDate;
}
