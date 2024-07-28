package com.acikgozKaan.VetRestAPI.dto.response;

import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Vaccine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimalResponse {

    private Long id;
    private String name;
    private String species;
    private String breed;
    private Animal.Gender gender;
    private String colour;
    private LocalDate dateOfBirth;
    private Long customerId;

    //private List<Appointment> appointmentList;

    private List<Long> vaccineIds;

    @Getter
    public enum Gender {
        MALE,
        FEMALE;
    }

    public AnimalResponse(Long id, String name, String species, String breed, Animal.Gender gender, String colour, LocalDate dateOfBirth, Long customerId) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.gender = gender;
        this.colour = colour;
        this.dateOfBirth = dateOfBirth;
        this.customerId = customerId;
    }
}
