package com.acikgozKaan.VetRestAPI.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "available_dates")
public class AvailableDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Temporal(TemporalType.DATE) No Necessary
    @Column(name = "available_date",nullable = false)
    private LocalDate availableDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "doctor_available_date",
            joinColumns = @JoinColumn(name = "available_date_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    private List<Doctor> doctorList;

    public AvailableDate(LocalDate availableDate, List<Doctor> doctorList) {
        this.availableDate = availableDate;
        this.doctorList = doctorList;
    }

}
