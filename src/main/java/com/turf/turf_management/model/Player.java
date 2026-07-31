package com.turf.turf_management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.turf.turf_management.enums.Role;
import com.turf.turf_management.enums.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "players")
public class Player extends BaseEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String playerId;

    private String name;

    @Indexed(unique = true)
    private String phone;

    @Indexed(unique = true)
    private String email;

    private Role role;

    private Status status;

    private LocalDate joinedDate;

    @JsonIgnore
    private String password;

    private LocalDate dob;

    private boolean active = true;
}