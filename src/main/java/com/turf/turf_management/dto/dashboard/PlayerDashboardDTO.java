package com.turf.turf_management.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerDashboardDTO {

    private String playerId;

    private String name;

    private int matchesPlayed;

    private double totalPaid;

    private double totalOutstanding;

}
