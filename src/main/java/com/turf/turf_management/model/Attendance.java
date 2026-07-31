package com.turf.turf_management.model;

import com.turf.turf_management.enums.AttendanceStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "attendance")
public class Attendance extends BaseEntity {

    @Id
    private String id;

    private String attendanceId;

    private String bookingId;

    private String playerId;

    private String playerName;

    private AttendanceStatus status;
}
