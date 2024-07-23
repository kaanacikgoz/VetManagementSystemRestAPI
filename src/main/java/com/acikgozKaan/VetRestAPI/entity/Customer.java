package com.acikgozKaan.VetRestAPI.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name",nullable = false)
    private String name;

    @Column(name = "customer_phone",unique = true,nullable = false)
    private String phone;

    @Email
    @Column(name = "customer_mail",unique = true,nullable = false)
    private String mail;

    @Column(name = "customer_address",nullable = false)
    private String address;

    @Column(name = "customer_city",nullable = false)
    private String city;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animal> animalList;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointmentList;

}
