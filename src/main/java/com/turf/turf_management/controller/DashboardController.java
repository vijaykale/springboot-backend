package com.turf.turf_management.controller;

import com.turf.turf_management.dto.dashboard.BookingDashboardDTO;
import com.turf.turf_management.dto.dashboard.PlayerDashboardDTO;
import com.turf.turf_management.dto.dashboard.UpcomingBookingDTO;
import com.turf.turf_management.enums.Role;
import com.turf.turf_management.model.Contribution;
import com.turf.turf_management.repository.BookingRepository;
import com.turf.turf_management.repository.ContributionRepository;
import com.turf.turf_management.repository.PlayerRepository;
import com.turf.turf_management.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final ContributionRepository contributionRepository;
    private final PlayerRepository playerRepository;
    private final BookingRepository bookingRepository;

   
    @GetMapping("/booking/{bookingId}")
    public BookingDashboardDTO getBookingDashboard(@PathVariable String bookingId){
        return dashboardService.getBookingDashboard(bookingId);
    }

  
    @GetMapping("/player/{playerId}")
    public PlayerDashboardDTO getPlayerDashboard(@PathVariable String playerId){
        return dashboardService.getPlayerDashboard(playerId);
    }

    @GetMapping("/upcoming")
    public List<UpcomingBookingDTO> getUpcomingBookings(){
        return dashboardService.getUpcomingBookings();
    }

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {

        long totalPlayers = playerRepository.countByRole(Role.PLAYER);
        long totalBookings = bookingRepository.count();

        List<Contribution> contributions = contributionRepository.findAll();

        double totalPaid = contributions.stream()
                .mapToDouble(Contribution::getAmountPaid)
                .sum();

        double totalPending = contributions.stream()
                .mapToDouble(Contribution::getRemainingAmount)
                .sum();

        Map<String, Object> res = new HashMap<>();
        res.put("totalPlayers", totalPlayers);
        res.put("totalBookings", totalBookings);
        res.put("totalPaid", totalPaid);
        res.put("totalPending", totalPending);

        return res;
    }

    @GetMapping("/payments/{bookingId}")
    public List<Map<String, Object>> getPaymentDashboard(@PathVariable String bookingId) {

        List<Contribution> contributions =
                contributionRepository.findByBookingIdAndActiveTrue(bookingId);

        return contributions.stream().map(c -> {

            Map<String, Object> map = new HashMap<>();
            map.put("contributionId", c.getContributionId());
            map.put("playerId", c.getPlayerId());
            map.put("playerName", c.getPlayerName());
            map.put("total", c.getAmountDue());
            map.put("paid", c.getAmountPaid());
            map.put("remaining", c.getRemainingAmount());
            map.put("status", c.getPaymentStatus().name()); // 🔥 KEY

            return map;

        }).toList();
    }
}
