package com.turf.turf_management.dto.player;

import com.turf.turf_management.enums.Role;
import com.turf.turf_management.enums.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PlayerResponseDTO {
    private String playerId;
    private String name;
    private String phone;
    private String email;

    private LocalDate dob;   // ⭐ NEW

    private Role role;

    private Status status;

    private LocalDate joinedDate;


}
