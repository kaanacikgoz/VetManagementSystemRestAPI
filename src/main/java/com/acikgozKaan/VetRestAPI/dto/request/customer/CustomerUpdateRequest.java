package com.acikgozKaan.VetRestAPI.dto.request.customer;

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
public class CustomerUpdateRequest {

    private String name;

    private String phone;

    @Email(message = "Invalid email format")
    private String mail;

    private String address;

    private String city;

    //private List<Long> animalList;

}
