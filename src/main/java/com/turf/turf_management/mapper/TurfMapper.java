package com.turf.turf_management.mapper;

import com.turf.turf_management.dto.turf.TurfRequestDTO;
import com.turf.turf_management.dto.turf.TurfResponseDTO;
import com.turf.turf_management.model.Turf;
import org.springframework.stereotype.Component;

@Component
public class TurfMapper {

    public Turf toEntity(TurfRequestDTO turfRequestDTO) {
        Turf turf = new Turf();
        turf.setName(turfRequestDTO.getName());
        turf.setLocation(turfRequestDTO.getLocation());
        turf.setPricePerHour(turfRequestDTO.getPricePerHour());
        turf.setAddress(turfRequestDTO.getAddress());
        turf.setContactNumber(turfRequestDTO.getContactNumber());
        turf.setActive(true);
        return turf;
    }

    public TurfResponseDTO turfResponseDTO(Turf turf) {
        if (turf == null) {
            return null;
        }
        TurfResponseDTO dto = new TurfResponseDTO();
        dto.setTurfId(turf.getTurfId());
        dto.setName(turf.getName());
        dto.setLocation(turf.getLocation());
        dto.setPricePerHour(turf.getPricePerHour());
        dto.setAddress(turf.getAddress());
        dto.setContactNumber(turf.getContactNumber());
        dto.setActive(turf.isActive());
        return dto;
    }
}
