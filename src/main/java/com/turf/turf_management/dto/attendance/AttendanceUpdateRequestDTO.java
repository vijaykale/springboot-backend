package com.turf.turf_management.dto.attendance;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttendanceUpdateRequestDTO {
    @NotNull(message = "Attendance status is required")
    private String attendanceStatus;
}
