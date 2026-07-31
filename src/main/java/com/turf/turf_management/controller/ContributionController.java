package com.turf.turf_management.controller;

import com.turf.turf_management.dto.contribution.PaymentRequestDTO;
import com.turf.turf_management.model.Contribution;
import com.turf.turf_management.service.ContributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contributions")
@RequiredArgsConstructor
public class ContributionController {

    private final ContributionService contributionService;

   
    @PostMapping("/generate/{bookingId}")
    public String generate(@PathVariable String bookingId) {
        return contributionService.generateContribution(bookingId);
    }

    @GetMapping("/booking/{bookingId}")
    public List<Contribution> getByBooking(@PathVariable String bookingId) {
        return contributionService.getByBooking(bookingId);
    }

    
    @GetMapping("/player/{playerId}")
    public List<Contribution> getByPlayer(@PathVariable String playerId) {
        return contributionService.getByPlayer(playerId);
    }

    @PutMapping("/pay/{contributionId}")
    public Contribution pay(
            @PathVariable String contributionId,
            @RequestBody PaymentRequestDTO request) {

        return contributionService.payContribution(contributionId, request.getAmount());
    }

   
    @DeleteMapping("/{contributionId}")
    public String delete(@PathVariable String contributionId) {
        return contributionService.deleteContribution(contributionId);
    }

    @GetMapping("/paid/{bookingId}")
    public double getTotalPaid(@PathVariable String bookingId){
        return contributionService.getTotalPaid(bookingId);
    }

    @GetMapping("/remaining/{bookingId}")
    public double getTotalRemaining(@PathVariable String bookingId){
        return contributionService.getTotalRemaining(bookingId);
    }

    @PostMapping("/recalculate/{bookingId}")
    public String recalculate(@PathVariable String bookingId) {
        contributionService.recalculateContribution(bookingId);
        return "Contribution recalculated successfully";
    }


}
