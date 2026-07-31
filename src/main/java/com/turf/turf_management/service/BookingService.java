package com.turf.turf_management.service;

import com.turf.turf_management.dto.booking.BookingRequestDTO;
import com.turf.turf_management.dto.booking.BookingResponseDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BookingService {

    BookingResponseDTO createBooking(BookingRequestDTO request);

    BookingResponseDTO getBookingById(String bookingId);

    List<BookingResponseDTO> getAllBookings();

    List<BookingResponseDTO> getAllConfirmBookings();

    BookingResponseDTO updateBooking(String bookingId, BookingRequestDTO request);

    BookingResponseDTO cancelBooking(String bookingId);

    BookingResponseDTO rebook(String id);


}
