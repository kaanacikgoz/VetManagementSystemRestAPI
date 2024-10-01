package com.acikgozKaan.VetRestAPI.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ManyToMany(mappedBy = "doctorList", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<AvailableDate> availableDateList;

    @OneToMany(mappedBy = "doctor",fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointmentList;

    public Doctor(String name, String phone, String mail, String address, String city, List<AvailableDate> availableDateList) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
        this.city = city;
        this.availableDateList = availableDateList;
    }

}
