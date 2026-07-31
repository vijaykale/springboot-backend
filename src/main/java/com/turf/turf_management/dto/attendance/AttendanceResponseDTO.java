package com.turf.turf_management.dto.attendance;

import lombok.Data;

@Data
public class AttendanceResponseDTO {

    private String attendanceId;

    private String bookingId;

    private String playerId;

    private String playerName;

    private String status;
}
