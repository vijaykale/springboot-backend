package com.turf.turf_management.dto.player;

import com.turf.turf_management.enums.Role;
import com.turf.turf_management.enums.Status;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PlayerRequestDTO {

    @NotBlank(message = "Player Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid format")
    private String email;

    @Past(message = "DOB must be in the past")
    private LocalDate dob;   // ⭐ NEW

    private Role role;

    private Status status;

    private LocalDate joinedDate;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

}
