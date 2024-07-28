package com.acikgozKaan.VetRestAPI.business.concretes;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAppointmentService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.AppointmentRepo;
import com.acikgozKaan.VetRestAPI.dao.DoctorRepo;
import com.acikgozKaan.VetRestAPI.entity.Appointment;
import com.acikgozKaan.VetRestAPI.entity.Doctor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentManager implements IAppointmentService {

    private final AppointmentRepo appointmentRepo;
    private final DoctorRepo doctorRepo;

    public AppointmentManager(AppointmentRepo appointmentRepo, DoctorRepo doctorRepo) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
    }

    @Override
    public Appointment save(Appointment appointment) {
        return appointmentRepo.save(appointment);
    }

    @Override
    public List<Appointment> getAll() {
        return appointmentRepo.findAll();
    }

    @Override
    public Appointment getById(Long id) {
        return appointmentRepo.findById(id).orElseThrow(
                ()->new NotFoundException(Msg.NOT_FOUND)
        );
    }

    @Override
    public Appointment update(Appointment appointment) {
        this.getById(appointment.getId());
        return this.appointmentRepo.save(appointment);
    }

    @Override
    public void delete(Long id) {
        Appointment appointment = this.getById(id);
        appointmentRepo.delete(appointment);
    }

    @Override
    public boolean existsByDoctorAndAppointmentDate(Doctor doctor, LocalDateTime appointmentDate) {
        return appointmentRepo.existsByDoctorAndAppointmentDate(doctor, appointmentDate);
    }

    @Override
    public List<Appointment> findByDoctorAndAppointmentDateBetween(Long doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Doctor not found with id " + doctorId));
        return appointmentRepo.findByDoctorAndAppointmentDateBetween(doctor, startDateTime, endDateTime);
    }

    @Override
    public List<Appointment> getAppointmentsByAnimalAndDateRange(Long animalId, LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentRepo.findByAnimalIdAndAppointmentDateBetween(animalId, startDate, endDate);
    }

    @Override
    public List<Appointment> findByIds(List<Long> appointmentIds) {
        return appointmentRepo.findAllById(appointmentIds);
    }

}
