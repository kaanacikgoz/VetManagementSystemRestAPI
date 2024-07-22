package com.acikgozKaan.VetRestAPI.dto.request.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSaveRequest {

    @NotBlank(message = "Customer name cannot be null or empty")
    private String name;

    @NotBlank(message = "Customer phone cannot be null or empty")
    private String phone;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Customer email cannot be null or empty")
    private String mail;

    @NotBlank(message = "Customer address cannot be null or empty")
    private String address;

    @NotBlank(message = "Customer city cannot be null or empty")
    private String city;

    //private List<Long> animalIds;

    //private List<Long> appointmentIds;

}
