package com.acikgozKaan.VetRestAPI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* FROM THE DOCUMENTATION;
    So, there’s no need to explicitly specify @Temporal annotation:
    LocalDate is mapped to DATE.
    LocalTime and OffsetTime are mapped to TIME.
    Instant, LocalDateTime, OffsetDateTime and ZonedDateTime are mapped to TIMESTAMP
     */
    //@Temporal(TemporalType.TIMESTAMP) No Necessary
    @Column(name = "appointment_date",nullable = false)
    private LocalDateTime appointmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id",referencedColumnName = "id")
    private Animal animal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id",referencedColumnName = "id")
    private Doctor doctor;

    public Appointment(LocalDateTime appointmentDate, Customer customer, Animal animal, Doctor doctor) {
        this.appointmentDate = appointmentDate;
        this.customer = customer;
        this.animal = animal;
        this.doctor = doctor;
    }

}
