package com.acikgozKaan.VetRestAPI.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_name",nullable = false)
    private String name;

    @Column(name = "doctor_phone",unique = true,nullable = false)
    private String phone;

    @Email
    @Column(name = "doctor_mail",unique = true,nullable = false)
    private String mail;

    @Column(name = "doctor_address",nullable = false)
    private String address;

    @Column(name = "doctor_city",nullable = false)
    private String city;

}
