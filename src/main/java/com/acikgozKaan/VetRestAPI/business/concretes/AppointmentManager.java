package com.acikgozKaan.VetRestAPI.business.concretes;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAppointmentService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.AppointmentRepo;
import com.acikgozKaan.VetRestAPI.entity.Appointment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentManager implements IAppointmentService {

    private final AppointmentRepo appointmentRepo;

    public AppointmentManager(AppointmentRepo appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    @Override
    public void save(Appointment appointment) {
        appointmentRepo.save(appointment);
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

}
