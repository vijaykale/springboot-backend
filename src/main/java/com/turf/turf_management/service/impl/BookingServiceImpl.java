package com.turf.turf_management.service.impl;

import com.turf.turf_management.dto.booking.BookingRequestDTO;
import com.turf.turf_management.dto.booking.BookingResponseDTO;
import com.turf.turf_management.enums.SequenceConstants;
import com.turf.turf_management.exception.ResourceNotFoundException;
import com.turf.turf_management.model.Booking;
import com.turf.turf_management.enums.BookingStatus;
import com.turf.turf_management.model.Player;
import com.turf.turf_management.model.Turf;
import com.turf.turf_management.repository.BookingRepository;
import com.turf.turf_management.repository.PlayerRepository;
import com.turf.turf_management.repository.TurfRepository;
import com.turf.turf_management.service.BookingService;
import com.turf.turf_management.service.NotificationService;
import com.turf.turf_management.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final TurfRepository turfRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final NotificationService notificationService;
    private final PlayerRepository playerRepository;

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        log.info("Creating booking: {}", request);

        // ✅ Step 1: Fetch existing bookings
        List<Booking> existingBookings = bookingRepository
                .findByTurfIdAndBookingDateAndStatus(
                        request.getTurfId(),
                        request.getBookingDate(),
                        BookingStatus.CONFIRMED
                );

        // ✅ Step 2: Conflict check
        for (Booking b : existingBookings) {
            boolean isConflict =
                    request.getStartTime().isBefore(b.getEndTime()) &&
                            request.getEndTime().isAfter(b.getStartTime());

            if (isConflict) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "This slot is already booked"
                );
            }
        }

        // ✅ Step 3: Validate time
        long minutes = Duration.between(
                request.getStartTime(),
                request.getEndTime()
        ).toMinutes();

        if (minutes <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End time must be after start time"
            );
        }

        double hours = minutes / 60.0;

        // ✅ Step 4: Fetch turf
        Turf turf = turfRepository.findByTurfId(request.getTurfId())
                .orElseThrow(() -> new ResourceNotFoundException("Turf not found"));

        double pricePerHour = turf.getPricePerHour();
        double turfCost = pricePerHour * hours;
        double totalCost = turfCost + request.getExtraExpense();
        double remaining = totalCost - request.getAdvancePaid();

        // ✅ Step 5: Generate Booking ID
        String bookingId = "BOOK" + String.format("%03d",
                sequenceGeneratorService.generateSequence(SequenceConstants.BOOKING_SEQUENCE));

        // ✅ Step 6: Save Booking
        Booking booking = Booking.builder()
                .bookingId(bookingId)
                .turfId(turf.getTurfId())
                .turfName(turf.getName())
                .bookingDate(request.getBookingDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .durationHours((int) hours)
                .pricePerHour(pricePerHour)
                .turfCost(turfCost)
                .totalCost(totalCost)
                .extraExpense(request.getExtraExpense())
                .advancePaid(request.getAdvancePaid())
                .remainingAmount(remaining)
                .playersExpected(request.getPlayersExpected())
                .playersPresent(0)
                .bookedBy(request.getBookedBy())
                .status(BookingStatus.CONFIRMED)
                .notes(request.getNotes())
                .build();

        Booking saved = bookingRepository.save(booking);


        // ✅ Step 7: Send Notification (SAFE)
        try {
            List<Player> play = playerRepository.findByActiveTrue();

            for (Player player : play) {
                notificationService.bookingCreated(
                        player.getEmail(),
                        player.getName(),
                        turf.getName(),
                        saved.getBookingDate().toString(),
                        saved.getStartTime() + " - " + saved.getEndTime(),
                        turf.getLocation(),
                        request.getNotes()
                );
            }

        } catch (Exception e) {
            log.error("Notification failed for bookingId: {}", bookingId, e);
            // ⚠️ DO NOT FAIL BOOKING if notification fails
        }

        return mapToResponse(saved);
    }

    @Override
    public BookingResponseDTO getBookingById(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return mapToResponse(booking);
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDTO> getAllConfirmBookings() {
        return bookingRepository.findByStatus(BookingStatus.CONFIRMED)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDTO updateBooking(String bookingId, BookingRequestDTO request) {

        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // ✅ update fields
        booking.setTurfId(request.getTurfId());
        booking.setBookingDate(request.getBookingDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setPlayersExpected(request.getPlayersExpected());
        booking.setAdvancePaid(request.getAdvancePaid());
        booking.setNotes(request.getNotes());

        // 🔥 duration
        long hours = Duration.between(request.getStartTime(), request.getEndTime()).toHours();
        booking.setDurationHours((int) hours);

        // 🔥 cost
        double totalCost = booking.getPricePerHour() * hours + request.getExtraExpense();
        booking.setTotalCost(totalCost);
        booking.setRemainingAmount(totalCost - request.getAdvancePaid());

        // ✅ ensure status stays BOOKED
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking updated = bookingRepository.save(booking);

        return mapToResponse(updated);
    }

    @Override
    public BookingResponseDTO cancelBooking(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(BookingStatus.CANCELLED);
        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    public BookingResponseDTO rebook(String id) {
        Booking b = bookingRepository.findByBookingId(id)
                .orElseThrow();
        if (!BookingStatus.CANCELLED.equals(b.getStatus())) {
            throw new RuntimeException("Only cancelled booking can be rebooked");
        }
        // 🔥 change back to active
        b.setStatus(BookingStatus.CONFIRMED);
        return mapToResponse(bookingRepository.save(b));
    }

    private BookingResponseDTO mapToResponse(Booking booking) {

        BookingResponseDTO dto = new BookingResponseDTO();

        dto.setBookingId(booking.getBookingId());
        dto.setTurfId(booking.getTurfId());
        dto.setTurfName(booking.getTurfName());
        dto.setBookingDate(booking.getBookingDate());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setExtraExpense(booking.getExtraExpense());
        dto.setTurfCost(booking.getTurfCost());
        dto.setTotalCost(booking.getTotalCost());
        dto.setAdvancePaid(booking.getAdvancePaid());
        dto.setRemainingAmount(booking.getRemainingAmount());
        dto.setBookedBy(booking.getBookedBy());
        dto.setStatus(booking.getStatus().name());
        dto.setPlayersExpected(booking.getPlayersExpected());
        dto.setPlayersPresent(booking.getPlayersPresent());
        dto.setPricePerHour(booking.getPricePerHour());

        return dto;
    }

}
