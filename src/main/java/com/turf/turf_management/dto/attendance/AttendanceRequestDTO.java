package com.turf.turf_management.dto.attendance;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AttendanceRequestDTO {

    @NotBlank
    private String bookingId;

    @NotBlank
    private String playerId;

    private String playerName;
}
