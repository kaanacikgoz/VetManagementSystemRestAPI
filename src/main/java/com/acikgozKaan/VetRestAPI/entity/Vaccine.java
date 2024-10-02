package com.acikgozKaan.VetRestAPI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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


    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name = "animal_vaccine",
            joinColumns = {@JoinColumn(name = "vaccine_id")},
            inverseJoinColumns = {@JoinColumn(name = "animal_id")}
    )
    private List<Animal> animalList;

    /*
    @ManyToMany(mappedBy = "vaccineList")
    private List<Animal> animalList;
     */

    public Vaccine(String name, String code, LocalDate protectionStartDate, LocalDate protectionFinishDate, List<Animal> animalList) {
        this.name = name;
        this.code = code;
        this.protectionStartDate = protectionStartDate;
        this.protectionFinishDate = protectionFinishDate;
        this.animalList = animalList;
    }

}
