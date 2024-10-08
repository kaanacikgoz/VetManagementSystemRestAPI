package com.acikgozKaan.VetRestAPI.dao;

import com.acikgozKaan.VetRestAPI.entity.Appointment;
import com.acikgozKaan.VetRestAPI.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorAndAppointmentDate(Doctor doctor, LocalDateTime appointmentDate);
    List<Appointment> findByDoctorAndAppointmentDateBetween(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);
    List<Appointment> findByAnimalIdAndAppointmentDateBetween(Long animalId, LocalDateTime startDate, LocalDateTime endDate);

}
