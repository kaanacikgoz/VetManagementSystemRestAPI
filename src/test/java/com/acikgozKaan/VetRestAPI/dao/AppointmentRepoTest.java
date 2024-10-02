package com.acikgozKaan.VetRestAPI.dao;

import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Appointment;
import com.acikgozKaan.VetRestAPI.entity.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class AppointmentRepoTest {

    @Autowired private AppointmentRepo appointmentRepo;
    @Autowired private DoctorRepo doctorRepo;
    @Autowired private AnimalRepo animalRepo;

    @Test
    void existsAppointmentByDoctorAndAppointmentDate() {
        // given
        Doctor doctor = Doctor.builder()
                .name("Doctor")
                .phone("123123111")
                .mail("doctor@mail.com")
                .address("Address")
                .city("City")
                .build();

        doctorRepo.save(doctor);

        Appointment appointment = Appointment.builder()
                .appointmentDate(LocalDateTime.of(2024,7,6,9,30))
                .doctor(doctor)
                .build();

        appointmentRepo.save(appointment);

        // when
        boolean isExist = appointmentRepo.existsByDoctorAndAppointmentDate(doctor, appointment.getAppointmentDate());
        boolean nonExist = appointmentRepo.existsByDoctorAndAppointmentDate(doctor, LocalDateTime.of(2024, 7, 7, 9, 30));

        // then
        assertTrue(isExist, "Appointment should exist for the given doctor and appointment date");
        assertFalse(nonExist, "Appointment should not exist for this date");
    }

    @Test
    void findAppointmentByDoctorAndAppointmentDateBetween() {
        // given
        Doctor doctor = Doctor.builder()
                .name("Doctor")
                .phone("123123111")
                .mail("doctor@mail.com")
                .address("Address")
                .city("City")
                .build();

        doctorRepo.save(doctor);

        Appointment appointment = Appointment.builder()
                .appointmentDate(LocalDateTime.of(2024,7,6,9,30))
                .doctor(doctor)
                .build();

        appointmentRepo.save(appointment);

        // when
        List<Appointment> foundAppointments = appointmentRepo.findByDoctorAndAppointmentDateBetween(
                doctor,
                LocalDateTime.of(2024,6,12,8,30),
                LocalDateTime.of(2024,8,12,8,30)
        );

        List<Appointment> noAppointments = appointmentRepo.findByDoctorAndAppointmentDateBetween(
                doctor,
                LocalDateTime.of(2023,6,12,8,30),
                LocalDateTime.of(2023,8,12,8,30)
        );

        // then
        assertEquals(1, foundAppointments.size(), "There should be one appointment in this date range");
        assertEquals(appointment.getAppointmentDate(), foundAppointments.get(0).getAppointmentDate(), "The appointment date should match");

        assertTrue(noAppointments.isEmpty(), "No appointments should be found for this date range");
    }

    @Test
    void findAppointmentByAnimalIdAndAppointmentDateBetween() {
        // given
        Animal animal = Animal.builder()
                .name("Animal")
                .species("Dog")
                .breed("Golden")
                .gender(Animal.Gender.FEMALE)
                .colour("Yellow")
                .dateOfBirth(LocalDate.of(2020,3,25))
                .build();

        animalRepo.save(animal);

        Doctor doctor = Doctor.builder()
                .name("Doctor")
                .phone("123123111")
                .mail("doctor@mail.com")
                .address("Address")
                .city("City")
                .build();

        doctorRepo.save(doctor);

        Appointment appointment = Appointment.builder()
                .appointmentDate(LocalDateTime.of(2024,7,6,9,30))
                .doctor(doctor)
                .animal(animal)
                .build();

        appointmentRepo.save(appointment);

        // when
        List<Appointment> foundAppointments = appointmentRepo.findByAnimalIdAndAppointmentDateBetween(
                animal.getId(),
                LocalDateTime.of(2024,6,12,8,30),
                LocalDateTime.of(2024,8,12,8,30)
        );

        List<Appointment> noAppointments = appointmentRepo.findByAnimalIdAndAppointmentDateBetween(
                animal.getId(),
                LocalDateTime.of(2023,6,12,8,30),
                LocalDateTime.of(2023,8,12,8,30)
        );

        // then
        assertEquals(1, foundAppointments.size(), "There should be one appointment in this date range");
        assertEquals(appointment.getAppointmentDate(), foundAppointments.get(0).getAppointmentDate(), "The appointment date should match");

        assertTrue(noAppointments.isEmpty(), "No appointments should be found for this date range");
    }

    @Test
    void findAppointmentAtBoundaryOfDateRange() {
        // given
        Animal animal = Animal.builder()
                .name("Animal")
                .species("Dog")
                .breed("Golden")
                .gender(Animal.Gender.FEMALE)
                .colour("Yellow")
                .dateOfBirth(LocalDate.of(2020,3,25))
                .build();

        animalRepo.save(animal);

        Doctor doctor = Doctor.builder()
                .name("Doctor")
                .phone("123123111")
                .mail("doctor@mail.com")
                .address("Address")
                .city("City")
                .build();

        doctorRepo.save(doctor);

        Appointment appointment = Appointment.builder()
                .appointmentDate(LocalDateTime.of(2024,7,6,9,30))
                .doctor(doctor)
                .animal(animal)
                .build();

        appointmentRepo.save(appointment);

        // when
        List<Appointment> foundAppointments = appointmentRepo.findByDoctorAndAppointmentDateBetween(
                doctor,
                LocalDateTime.of(2024,7,6,9,30),
                LocalDateTime.of(2024,8,6,9,30)
        );

        List<Appointment> foundAppointments2 = appointmentRepo.findByAnimalIdAndAppointmentDateBetween(
                animal.getId(),
                LocalDateTime.of(2024,7,6,9,30),
                LocalDateTime.of(2024,8,6,9,30)
        );

        // then
        assertEquals(1, foundAppointments.size(), "There should be one appointment exactly at the boundary");
        assertEquals(1, foundAppointments2.size(), "There should be one appointment exactly at the boundary");

        assertEquals(appointment.getAppointmentDate(), foundAppointments.get(0).getAppointmentDate(), "The appointment date should match exactly");
        assertEquals(appointment.getAppointmentDate(), foundAppointments2.get(0).getAppointmentDate(), "The appointment date should match exactly");
    }

}
