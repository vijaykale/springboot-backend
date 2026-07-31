package com.turf.turf_management.controller;

import com.turf.turf_management.dto.turf.TurfRequestDTO;
import com.turf.turf_management.dto.turf.TurfResponseDTO;
import com.turf.turf_management.model.Turf;
import com.turf.turf_management.service.TurfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/turfs")
@RequiredArgsConstructor
@Slf4j
public class TurfController {

    private final TurfService turfService;

   
    @PostMapping
    public TurfResponseDTO createTurf(@Valid @RequestBody TurfRequestDTO turf) {
        log.info("Received request to create turf: {}", turf);
        TurfResponseDTO savedTurf = turfService.createTurf(turf);
        log.info("Turf saved successfully: {}", savedTurf);
        return savedTurf;
    }

    
    @GetMapping
    public List<TurfResponseDTO> getAllTurfs() {
        log.info("Received request to get all turfs");
        return turfService.getAllTurfs();
    }

    
    @GetMapping("/{turfId}")
    public TurfResponseDTO getTurf(@PathVariable String turfId) {
        log.info("Received request to get turf with ID: {}", turfId);
        return turfService.getTurfByTurfId(turfId);
    }

   
    @PutMapping("/{turfId}")
    public TurfResponseDTO updateTurf(
            @PathVariable String turfId,
            @Valid @RequestBody TurfRequestDTO turf) {
        log.info("API called: update turf");
        return turfService.updateTurf(turfId, turf);
    }

   
    @DeleteMapping("/{turfId}")
    public ResponseEntity<String> deleteTurf(@PathVariable String turfId) {
        log.info("API called: delete turf with ID: {}", turfId);
        turfService.deleteTurf(turfId);
        return ResponseEntity.ok("Turf with ID " + turfId + " deactivated successfully");
    }

}
