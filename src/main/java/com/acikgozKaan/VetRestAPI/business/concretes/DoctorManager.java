package com.acikgozKaan.VetRestAPI.business.concretes;

import com.acikgozKaan.VetRestAPI.business.abstracts.IDoctorService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.DoctorRepo;
import com.acikgozKaan.VetRestAPI.entity.Doctor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorManager implements IDoctorService {

    private final DoctorRepo doctorRepo;

    public DoctorManager(DoctorRepo doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    @Override
    public void save(Doctor doctor) {
        doctorRepo.save(doctor);
    }

    @Override
    public List<Doctor> getAll() {
        return doctorRepo.findAll();
    }

    @Override
    public Doctor getById(Long id) {
        return doctorRepo.findById(id).orElseThrow(
                ()->new NotFoundException(Msg.NOT_FOUND)
        );
    }

    @Override
    public Doctor update(Doctor doctor) {
        this.getById(doctor.getId());
        return this.doctorRepo.save(doctor);
    }

    @Override
    public void delete(Long id) {
        Doctor doctor = this.getById(id);
        doctorRepo.delete(doctor);
    }

}
