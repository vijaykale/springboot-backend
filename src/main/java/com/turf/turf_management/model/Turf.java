package com.turf.turf_management.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "turfs")
public class Turf extends BaseEntity {
    @Id
    private String id;

    @Indexed(unique = true)
    private String turfId;

    private String name;

    private String location;

    private String address;

    private double pricePerHour;

    private String contactNumber;

    private double rating;

    private String notes;

    private boolean active = true;
}
