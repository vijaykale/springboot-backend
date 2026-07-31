package com.turf.turf_management.dto.booking;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
public class BookingResponseDTO {
    private String bookingId;

    private String turfId;

    private String turfName;

    private LocalDate bookingDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private double turfCost;

    private double totalCost;

    private double advancePaid;

    private double remainingAmount;

    private String bookedBy;

    private String status;

    private double extraExpense;

    private int playersExpected;

    private double pricePerHour;

    private int playersPresent;
}
