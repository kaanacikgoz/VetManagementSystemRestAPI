package com.acikgozKaan.VetRestAPI.business.abstracts;

import com.acikgozKaan.VetRestAPI.entity.Appointment;

import java.util.List;

public interface IAppointmentService {

    void save(Appointment appointment);

    List<Appointment> getAll();

    Appointment getById(Long id);

    Appointment update(Appointment appointment);

    void delete(Long id);

}
