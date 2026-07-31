package com.turf.turf_management.controller;

import com.turf.turf_management.dto.attendance.AttendanceRequestDTO;
import com.turf.turf_management.dto.attendance.AttendanceResponseDTO;
import com.turf.turf_management.dto.attendance.AttendanceUpdateRequestDTO;
import com.turf.turf_management.dto.attendance.BulkAttendanceRequestDTO;
import com.turf.turf_management.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

  
    @PostMapping
    public AttendanceResponseDTO markAttendance(
            @Valid @RequestBody AttendanceRequestDTO request) {

        return attendanceService.markAttendance(request);
    }

    @PostMapping("/bulk")
    public List<AttendanceResponseDTO> markBulk(
            @RequestBody BulkAttendanceRequestDTO request) {

        return attendanceService.markBulkAttendance(request);
    }

    @GetMapping("/booking/{bookingId}/present")
    public List<String> getPresentPlayers(@PathVariable String bookingId) {
        return attendanceService.getPresentPlayerIds(bookingId);
    }

    @GetMapping("/booking/{bookingId}/summary")
    public Map<String, Object> getSummary(@PathVariable String bookingId) {
        return attendanceService.getAttendanceSummary(bookingId);
    }

    
    @GetMapping("/booking/{bookingId}")
    public List<AttendanceResponseDTO> getAttendanceByBooking(
            @PathVariable String bookingId) {

        return attendanceService.getAttendanceByBooking(bookingId);
    }

  
    @GetMapping("/player/{playerId}")
    public List<AttendanceResponseDTO> getAttendanceByPlayer(
            @PathVariable String playerId) {

        return attendanceService.getAttendanceByPlayer(playerId);
    }

   
    @GetMapping
    public List<AttendanceResponseDTO> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

   
    @GetMapping("/booking/{bookingId}/count")
    public Map<String, Object> getAttendanceCount(@PathVariable String bookingId) {
        long count = attendanceService.getAttendanceCount(bookingId);
        Map<String, Object> response = new HashMap<>();
        response.put("bookingId", bookingId);
        response.put("attendanceCount", count);
        return response;
    }

   
    @PutMapping("/{attendanceId}")
    public AttendanceResponseDTO updateAttendance(
            @PathVariable String attendanceId,
            @RequestBody AttendanceUpdateRequestDTO request){

        return attendanceService.updateAttendance(attendanceId, request);
    }

   
    @DeleteMapping("/{attendanceId}")
    public String deleteAttendance(@PathVariable String attendanceId) {
        return attendanceService.deleteAttendance(attendanceId);
    }

}
