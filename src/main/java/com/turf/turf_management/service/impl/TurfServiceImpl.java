package com.turf.turf_management.service.impl;

import com.turf.turf_management.dto.turf.TurfRequestDTO;
import com.turf.turf_management.dto.turf.TurfResponseDTO;
import com.turf.turf_management.enums.SequenceConstants;
import com.turf.turf_management.exception.DuplicateResourceException;
import com.turf.turf_management.exception.ResourceNotFoundException;
import com.turf.turf_management.mapper.TurfMapper;
import com.turf.turf_management.model.Turf;
import com.turf.turf_management.repository.TurfRepository;
import com.turf.turf_management.service.SequenceGeneratorService;
import com.turf.turf_management.service.TurfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TurfServiceImpl implements TurfService {

    private final TurfRepository turfRepository;
    private final TurfMapper turfMapper;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public TurfResponseDTO createTurf(TurfRequestDTO request) {


        if (turfRepository.existsByContactNumber(request.getContactNumber())) {
            log.error("Turf with contact number {} already exists", request.getContactNumber());
            throw new DuplicateResourceException("Turf with Contact Number " + request.getContactNumber() + " already exists");
        }
        log.info("Creating new turf {}", request.getName());
        Turf turf = turfMapper.toEntity(request);
        String turfId = "TRF" +
                String.format("%03d",
                        sequenceGeneratorService.generateSequence(SequenceConstants.TURF_SEQUENCE));
        turf.setTurfId(turfId);
        turf.setActive(true);
        Turf saved = turfRepository.save(turf);
        return turfMapper.turfResponseDTO(saved);

    }

    @Override
    public TurfResponseDTO getTurfByTurfId(String turfId) {
        Turf turf = turfRepository.findByTurfId(turfId).orElseThrow(() -> new ResourceNotFoundException("Turf with ID " + turfId + " not found"));
        return turfMapper.turfResponseDTO(turf);
    }

    @Override
    public List<TurfResponseDTO> getAllTurfs() {
        return turfRepository.findAll().stream().map(turfMapper::turfResponseDTO).toList();
    }

    @Override
    public TurfResponseDTO updateTurf(String turfId, TurfRequestDTO turfRequestDTO) {
        Turf turf = turfRepository.findByTurfId(turfId).orElseThrow(() -> new ResourceNotFoundException("Turf with ID " + turfId + " not found"));
        turf.setName(turfRequestDTO.getName());
        turf.setLocation(turfRequestDTO.getLocation());
        turf.setPricePerHour(turfRequestDTO.getPricePerHour());
        turf.setAddress(turfRequestDTO.getAddress());
        turf.setContactNumber(turfRequestDTO.getContactNumber());
        turf.setActive(turfRequestDTO.isActive());
        Turf updated = turfRepository.save(turf);
        return turfMapper.turfResponseDTO(updated);
    }

    @Override
    public void deleteTurf(String turfId) {

        Turf turf = turfRepository.findByTurfId(turfId).orElseThrow(() -> new ResourceNotFoundException("Turf with ID " + turfId + " not found"));
        turf.setActive(false);
        turfRepository.save(turf);
    }


}
