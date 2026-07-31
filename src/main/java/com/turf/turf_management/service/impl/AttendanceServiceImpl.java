package com.turf.turf_management.service.impl;

import com.turf.turf_management.dto.attendance.AttendanceRequestDTO;
import com.turf.turf_management.dto.attendance.AttendanceResponseDTO;
import com.turf.turf_management.dto.attendance.AttendanceUpdateRequestDTO;
import com.turf.turf_management.dto.attendance.BulkAttendanceRequestDTO;
import com.turf.turf_management.enums.AttendanceStatus;
import com.turf.turf_management.enums.BookingStatus;
import com.turf.turf_management.enums.SequenceConstants;
import com.turf.turf_management.exception.ResourceNotFoundException;
import com.turf.turf_management.model.Attendance;
import com.turf.turf_management.model.Booking;
import com.turf.turf_management.model.Player;
import com.turf.turf_management.repository.AttendanceRepository;
import com.turf.turf_management.repository.BookingRepository;
import com.turf.turf_management.repository.PlayerRepository;
import com.turf.turf_management.service.AttendanceService;
import com.turf.turf_management.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final BookingRepository bookingRepository;
    private final PlayerRepository playerRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public AttendanceResponseDTO markAttendance(AttendanceRequestDTO request) {
        // 1️⃣ Check booking exists
        Booking booking = bookingRepository.findByBookingId(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // 2️⃣ Validate booking status
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Attendance not allowed for this booking");
        }

        // 3️⃣ Check player exists
        Player player = playerRepository.findByPlayerId(request.getPlayerId())
                .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        // 4️⃣ Prevent duplicate attendance
        boolean alreadyMarked = attendanceRepository
                .existsByBookingIdAndPlayerId(request.getBookingId(), request.getPlayerId());

        if (alreadyMarked) {
            throw new RuntimeException("Player attendance already marked");
        }

        // 5️⃣ Generate attendanceId
        String attendanceId = "ATT" +
                String.format("%03d",
                        sequenceGeneratorService.generateSequence(SequenceConstants.ATTENDANCE_SEQUENCE));

        Attendance attendance = Attendance.builder()
                .attendanceId(attendanceId)
                .bookingId(request.getBookingId())
                .playerId(player.getPlayerId())
                .playerName(player.getName())
                .status(AttendanceStatus.PRESENT)
                .build();

        Attendance saved = attendanceRepository.save(attendance);

        // 6️⃣ Update playersPresent
        booking.setPlayersPresent(booking.getPlayersPresent() + 1);
        bookingRepository.save(booking);

        return mapToDTO(saved);
    }

    @Override
    public List<AttendanceResponseDTO> markBulkAttendance(BulkAttendanceRequestDTO request) {

        Booking booking = bookingRepository.findByBookingId(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Attendance not allowed");
        }

        List<AttendanceResponseDTO> responseList = new java.util.ArrayList<>();

        for (String playerId : request.getPlayerIds()) {

            boolean alreadyMarked = attendanceRepository
                    .existsByBookingIdAndPlayerId(request.getBookingId(), playerId);

            if (alreadyMarked) continue; // skip duplicates

            Player player = playerRepository.findByPlayerId(playerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

            String attendanceId = "ATT" +
                    String.format("%03d",
                            sequenceGeneratorService.generateSequence(SequenceConstants.ATTENDANCE_SEQUENCE));

            Attendance attendance = Attendance.builder()
                    .attendanceId(attendanceId)
                    .bookingId(request.getBookingId())
                    .playerId(playerId)
                    .playerName(player.getName())

                    .status(AttendanceStatus.PRESENT)
                    .build();

            Attendance saved = attendanceRepository.save(attendance);

            responseList.add(mapToDTO(saved));
        }

        // update playersPresent
        long count = attendanceRepository.countByBookingId(request.getBookingId());
        booking.setPlayersPresent((int) count);
        bookingRepository.save(booking);

        return responseList;
    }

    @Override
    public List<AttendanceResponseDTO> getAttendanceByBooking(String bookingId) {
        return attendanceRepository.findByBookingId(bookingId)
                .stream()
                .map(a -> {
                    AttendanceResponseDTO dto = new AttendanceResponseDTO();
                    dto.setAttendanceId(a.getAttendanceId());
                    dto.setBookingId(a.getBookingId());
                    dto.setPlayerId(a.getPlayerId());
                    dto.setPlayerName(a.getPlayerName());
                    dto.setStatus(a.getStatus().name());
                    return dto;
                }).toList();
    }

    @Override
    public List<AttendanceResponseDTO> getAttendanceByPlayer(String playerId) {
        return attendanceRepository.findByPlayerId(playerId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<AttendanceResponseDTO> getAllAttendance() {
        return attendanceRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public long getAttendanceCount(String bookingId) {
        return attendanceRepository.countByBookingId(bookingId);
    }

    @Override
    public AttendanceResponseDTO updateAttendance(String attendanceId, AttendanceUpdateRequestDTO request) {
        Attendance attendance = attendanceRepository.findByAttendanceId(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        AttendanceStatus status = AttendanceStatus.valueOf(request.getAttendanceStatus());
        attendance.setStatus(status);
        Attendance updated = attendanceRepository.save(attendance);
        return mapToDTO(updated);
    }

    @Override
    public List<String> getPresentPlayerIds(String bookingId) {
        return attendanceRepository.findByBookingId(bookingId)
                .stream()
                .map(Attendance::getPlayerId)
                .toList();
    }

    @Override
    public Map<String, Object> getAttendanceSummary(String bookingId) {

        long present = attendanceRepository.countByBookingId(bookingId);

        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        Map<String, Object> map = new HashMap<>();
        map.put("bookingId", bookingId);
        map.put("expected", booking.getPlayersExpected());
        map.put("present", present);
        map.put("absent", booking.getPlayersExpected() - present);

        return map;
    }

    @Override
    public String deleteAttendance(String attendanceId) {
        Attendance attendance = attendanceRepository.findByAttendanceId(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        // Update playersPresent count in Booking
        Booking booking = bookingRepository.findByBookingId(attendance.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (attendance.getStatus() == AttendanceStatus.PRESENT) {
            booking.setPlayersPresent(Math.max(0, booking.getPlayersPresent() - 1)); // Ensure it doesn't go negative
            bookingRepository.save(booking);
        }

        attendanceRepository.delete(attendance);
        return "Attendance with ID: " + attendanceId + " has been deleted successfully.";
    }

    private AttendanceResponseDTO mapToDTO(Attendance attendance) {

        AttendanceResponseDTO dto = new AttendanceResponseDTO();
        dto.setAttendanceId(attendance.getAttendanceId());
        dto.setBookingId(attendance.getBookingId());
        dto.setPlayerId(attendance.getPlayerId());
        dto.setPlayerName(attendance.getPlayerName());
        dto.setStatus(attendance.getStatus().name());
        return dto;
    }
}
