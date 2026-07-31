package com.turf.turf_management.repository;

import com.turf.turf_management.enums.BookingStatus;
import com.turf.turf_management.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends MongoRepository<Booking, String> {
    Optional<Booking> findByBookingId(String bookingId);

    List<Booking> findByTurfIdAndBookingDateAndStatus(
            String turfId,
            LocalDate bookingDate,
            BookingStatus status
    );

    List<Booking> findByStatus(BookingStatus status);
}
