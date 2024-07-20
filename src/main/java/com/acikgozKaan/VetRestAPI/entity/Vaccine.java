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
@Table(name = "vaccines")
public class Vaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vaccine_name",nullable = false)
    private String name;

    @Column(name = "vaccine_code",nullable = false)
    private String code;

    @Column(name = "vaccine_protection_start_date",nullable = false)
    private LocalDate protectionStartDate;

    @Column(name = "vaccine_protection_finish_date",nullable = false)
    private LocalDate protectionFinishDate;

}
