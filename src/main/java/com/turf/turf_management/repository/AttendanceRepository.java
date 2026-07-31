package com.turf.turf_management.repository;

import com.turf.turf_management.enums.AttendanceStatus;
import com.turf.turf_management.model.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    List<Attendance> findByBookingId(String bookingId);

    List<Attendance> findByPlayerId(String playerId);

    boolean existsByBookingIdAndPlayerId(String bookingId, String playerId);

    long countByBookingId(String bookingId);

    long countByPlayerIdAndStatus(String playerId, AttendanceStatus status);

    Optional<Attendance> findByAttendanceId(String attendanceId);

    List<Attendance> findByBookingIdAndStatus(String bookingId, AttendanceStatus status);

}
