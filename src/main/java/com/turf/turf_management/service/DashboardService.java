package com.turf.turf_management.service;

import com.turf.turf_management.dto.dashboard.BookingDashboardDTO;
import com.turf.turf_management.dto.dashboard.PlayerDashboardDTO;
import com.turf.turf_management.dto.dashboard.UpcomingBookingDTO;

import java.util.List;

public interface DashboardService {
    BookingDashboardDTO getBookingDashboard(String bookingId);

    PlayerDashboardDTO getPlayerDashboard(String playerId);

    List<UpcomingBookingDTO> getUpcomingBookings();
}
