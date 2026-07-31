package com.turf.turf_management.dto.player;

import com.turf.turf_management.enums.Status;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PlayerUpdateRequestDTO {

    @NotBlank(message = "Player Name is required")
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @Past(message = "DOB must be in the past")
    private LocalDate dob;

    private Status status;
    private LocalDate joinedDate;

}
