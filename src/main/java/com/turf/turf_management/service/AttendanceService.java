package com.turf.turf_management.service;

import com.turf.turf_management.dto.attendance.AttendanceRequestDTO;
import com.turf.turf_management.dto.attendance.AttendanceResponseDTO;
import com.turf.turf_management.dto.attendance.AttendanceUpdateRequestDTO;
import com.turf.turf_management.dto.attendance.BulkAttendanceRequestDTO;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

public interface AttendanceService {

    AttendanceResponseDTO markAttendance(AttendanceRequestDTO request);

    List<AttendanceResponseDTO> getAttendanceByBooking(String bookingId);

    List<AttendanceResponseDTO> getAttendanceByPlayer(String playerId);

    List<AttendanceResponseDTO> getAllAttendance();

    long getAttendanceCount(String bookingId);

    AttendanceResponseDTO updateAttendance(String attendanceId, AttendanceUpdateRequestDTO request);

    String deleteAttendance(String attendanceId);

    List<AttendanceResponseDTO> markBulkAttendance(BulkAttendanceRequestDTO request);

    List<String> getPresentPlayerIds(String bookingId);

    Map<String, Object> getAttendanceSummary(String bookingId);


}
