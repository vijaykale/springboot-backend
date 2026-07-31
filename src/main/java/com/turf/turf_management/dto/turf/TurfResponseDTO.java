package com.turf.turf_management.dto.turf;

import lombok.Data;

@Data
public class TurfResponseDTO {
    private String turfId;

    private String name;

    private String location;

    private String address;

    private double pricePerHour;

    private String contactNumber;

    private double rating;

    private boolean active;
}
