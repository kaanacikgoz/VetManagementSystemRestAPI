package com.acikgozKaan.VetRestAPI.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {

    private Long id;

    private String name;

    private String phone;

    private String mail;

    private String address;

    private String city;

    private List<Long> availableDateIds;

    private List<Long> appointmentIds;

    public DoctorResponse(Long id, String name, String phone, String mail, String address, String city, List<Long> availableDateIds) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
        this.city = city;
        this.availableDateIds = availableDateIds;
    }
}
