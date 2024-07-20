package com.acikgozKaan.VetRestAPI.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "animal_id",referencedColumnName = "id")
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "doctor_id",referencedColumnName = "id")
    private Doctor doctor;

}
