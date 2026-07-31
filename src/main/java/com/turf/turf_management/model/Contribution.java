package com.turf.turf_management.model;

import com.turf.turf_management.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contributions")
public class Contribution {

    @Id
    private String id;

    private String contributionId;

    private String bookingId;

    private String playerId;

    private String playerName;

    private double amountDue;

    private double amountPaid;

    private double remainingAmount;

    private PaymentStatus paymentStatus;

    private boolean active;
}
