package com.acikgozKaan.VetRestAPI.business.abstracts;

import com.acikgozKaan.VetRestAPI.entity.Doctor;

import java.util.List;

public interface IDoctorService {

    void save(Doctor doctor);

    List<Doctor> getAll();

    Doctor getById(Long id);

    Doctor update(Doctor doctor);

    void delete(Long id);

}
