package com.turf.turf_management.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class UpcomingBookingDTO {
    private String bookingId;

    private String turfName;

    private LocalDate bookingDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private int duration;

    private double totalCost;

    private int playerPresent;
}
