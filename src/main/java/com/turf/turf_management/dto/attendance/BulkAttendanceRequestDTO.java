package com.turf.turf_management.dto.attendance;

import lombok.Data;

import java.util.List;

@Data
public class BulkAttendanceRequestDTO {
    private String bookingId;
    private List<String> playerIds;
}
