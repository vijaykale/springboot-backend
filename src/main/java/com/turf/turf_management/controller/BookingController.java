package com.turf.turf_management.controller;

import com.turf.turf_management.dto.booking.BookingRequestDTO;
import com.turf.turf_management.dto.booking.BookingResponseDTO;
import com.turf.turf_management.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;


    @PostMapping
    public BookingResponseDTO createBooking(
            @Valid @RequestBody BookingRequestDTO request,
            Authentication auth) {

        return bookingService.createBooking(request);
    }


    @GetMapping
    public List<BookingResponseDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/confirmed")
    public List<BookingResponseDTO> getConfirmedBooking() {
        return bookingService.getAllConfirmBookings();
    }

    @PreAuthorize("hasRole('ADMIN','PLAYER')")
    @GetMapping("/{bookingId}")
    public BookingResponseDTO getBooking(
            @PathVariable String bookingId) {

        return bookingService.getBookingById(bookingId);
    }


    @PutMapping("/{bookingId}")
    public BookingResponseDTO updateBooking(
            @PathVariable String bookingId,
            @RequestBody BookingRequestDTO request) {

        return bookingService.updateBooking(bookingId, request);
    }

    @PutMapping("/cancel/{bookingId}")
    public BookingResponseDTO cancelBooking(@PathVariable String bookingId) {
        return bookingService.cancelBooking(bookingId);
    }

    @PutMapping("/rebook/{id}")
    public BookingResponseDTO rebook(@PathVariable String id) {
        return bookingService.rebook(id);
    }
}
