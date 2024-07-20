package com.acikgozKaan.VetRestAPI.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "animal_name",nullable = false)
    private String name;

    @Column(name = "animal_species",nullable = false)
    private String species;

    @Column(name = "animal_breed",nullable = false)
    private String breed;

    @Enumerated(EnumType.STRING)
    @Column(name = "animal_gender",nullable = false)
    private Gender gender;

    @Column(name = "animal_colour",nullable = false)
    private String colour;

    @Column(name = "animal_date_of_birth",nullable = false)
    private LocalDate dateOfBirth;

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
