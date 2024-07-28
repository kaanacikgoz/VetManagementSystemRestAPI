package com.acikgozKaan.VetRestAPI.business.abstracts;

import com.acikgozKaan.VetRestAPI.entity.Appointment;
import com.acikgozKaan.VetRestAPI.entity.Doctor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService {

    Appointment save(Appointment appointment);

    List<Appointment> getAll();

    Appointment getById(Long id);

    Appointment update(Appointment appointment);

    void delete(Long id);

    boolean existsByDoctorAndAppointmentDate(Doctor doctor, LocalDateTime appointmentDateTime);

    List<Appointment> findByDoctorAndAppointmentDateBetween(Long doctorId, LocalDateTime startDate, LocalDateTime endDate);

    List<Appointment> getAppointmentsByAnimalAndDateRange(Long animalId, LocalDateTime startDate, LocalDateTime endDate);

    List<Appointment> findByIds(List<Long> appointmentIds);

}
