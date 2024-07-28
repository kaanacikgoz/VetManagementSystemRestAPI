package com.acikgozKaan.VetRestAPI.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private Long id;

    private LocalDateTime appointmentDate;

    private Long customerId;

    private Long animalId;

    private Long doctorId;

}
