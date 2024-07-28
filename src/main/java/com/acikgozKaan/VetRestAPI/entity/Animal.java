package com.acikgozKaan.VetRestAPI.entity;

import jakarta.persistence.*;
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

    @ManyToOne()
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private Customer customer;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointmentList;

    @ManyToMany(mappedBy = "animalList",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Vaccine> vaccineList;

    @Getter
    public enum Gender {
        MALE,
        FEMALE
    }

    public Animal(String name, String species, String breed, Gender gender, String colour, LocalDate dateOfBirth, Customer customer) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.gender = gender;
        this.colour = colour;
        this.dateOfBirth = dateOfBirth;
        this.customer = customer;
    }
}
