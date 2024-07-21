package com.acikgozKaan.VetRestAPI.dto.response;

import com.acikgozKaan.VetRestAPI.entity.Animal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    //private List<Vaccine> vaccineList;

    @Getter
    public enum Gender {
        MALE("M"),
        FEMALE("F");

        private final String value;

        Gender(String value) {
            this.value = value;
        }
    }

}
