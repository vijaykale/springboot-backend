package com.turf.turf_management.dto.turf;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TurfRequestDTO {

    @NotBlank(message = "Turf name is required")
    @Size(min = 3, max = 50, message = "Turf Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Turf Location is required")
    private String location;

    @NotBlank(message = "Turf Address is required")
    private String address;

    @Positive(message = "Price per hour must be a positive number")
    private int pricePerHour;

    @Pattern(regexp = "^[0-9]{10}$",
            message = "Contact number must be 10 digits")
    private String contactNumber;

    private String notes;

    private boolean active = true;



}
