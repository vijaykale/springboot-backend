package com.turf.turf_management.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRequestDTO {

    @NotBlank(message = "Turf ID is required")
    private String turfId;

    @NotNull(message = "Booking date is required")
    private LocalDate bookingDate;

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Min(value = 1, message = "Players expected must be at least 1")
    private int playersExpected;

    @PositiveOrZero(message = "Advance paid cannot be negative")
    private double advancePaid;

    @NotBlank(message = "BookedBy playerId is required")
    private String bookedBy;

    @PositiveOrZero(message = "Advance paid cannot be negative")
    private double extraExpense;   // ⭐ NEW FIELD

    private String notes;

}
