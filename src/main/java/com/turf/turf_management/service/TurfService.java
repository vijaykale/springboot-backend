package com.turf.turf_management.service;

import com.turf.turf_management.dto.turf.TurfRequestDTO;
import com.turf.turf_management.dto.turf.TurfResponseDTO;

import java.util.List;

public interface TurfService {
    TurfResponseDTO createTurf(TurfRequestDTO turfResponseDTO);

    TurfResponseDTO getTurfByTurfId(String turfId);

    TurfResponseDTO updateTurf(String turfId, TurfRequestDTO turfResponseDTO);

    void deleteTurf(String turfId);

    List<TurfResponseDTO> getAllTurfs();
}
