package com.turf.turf_management.model;

import com.turf.turf_management.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bookings")
public class Booking extends BaseEntity {

    @Id
    private String id;

    private String bookingId;

    private String turfId;

    private String turfName;

    private LocalDate bookingDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private int durationHours;

    private double pricePerHour;

    private double extraExpense;

    private double totalCost;

    private double advancePaid;

    private double remainingAmount;

    private int playersExpected;

    private int playersPresent;

    private String bookedBy;

    private BookingStatus status;

    private double turfCost;

    private String notes;
}
