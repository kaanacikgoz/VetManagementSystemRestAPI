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
@Table(name = "available_dates")
public class AvailableDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Temporal(TemporalType.DATE) No Necessary
    @Column(name = "available_date",nullable = false)
    private LocalDate availableDate;

    @ManyToMany(mappedBy = "availableDateList")
    private List<Doctor> doctorList;

}
