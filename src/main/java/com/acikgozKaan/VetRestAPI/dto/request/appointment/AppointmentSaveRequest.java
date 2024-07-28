package com.acikgozKaan.VetRestAPI.dto.request.appointment;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaveRequest {

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appointmentDate;

    //private Long customerId; This should be come from animal entity

    private Long animalId;

    private Long doctorId;

}
