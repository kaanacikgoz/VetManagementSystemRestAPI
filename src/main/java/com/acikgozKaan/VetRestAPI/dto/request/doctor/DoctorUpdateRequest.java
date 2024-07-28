package com.acikgozKaan.VetRestAPI.dto.request.doctor;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorUpdateRequest {

    private String name;

    private String phone;

    @Email
    private String mail;

    private String address;

    private String city;

    private List<Long> availableDateIds;

    //private List<Long> appointmentIds;

}
