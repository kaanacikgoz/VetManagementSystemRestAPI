package com.acikgozKaan.VetRestAPI.business.concretes;

import com.acikgozKaan.VetRestAPI.business.abstracts.IDoctorService;
import com.acikgozKaan.VetRestAPI.core.exception.DuplicateEntryException;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.DoctorRepo;
import com.acikgozKaan.VetRestAPI.entity.Customer;
import com.acikgozKaan.VetRestAPI.entity.Doctor;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorManager implements IDoctorService {

    private final DoctorRepo doctorRepo;

    public DoctorManager(DoctorRepo doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    @Override
    public Doctor save(Doctor doctor) {
        boolean emailExists = doctorRepo.findByMail(doctor.getMail()).isPresent();
        boolean phoneExists = doctorRepo.findByPhone(doctor.getPhone()).isPresent();

        if (emailExists || phoneExists) {
            throw new DuplicateEntryException("Duplicate Error: Email or Phone number already exists.");
        } else {
            return doctorRepo.save(doctor);
        }
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

        /*
        Optional<Doctor> existingDoctorByEmail = doctorRepo.findByMail(doctor.getMail());
        Optional<Doctor> existingDoctorByPhone = doctorRepo.findByPhone(doctor.getPhone());

        boolean emailExists = existingDoctorByEmail.isPresent() && !existingDoctorByEmail.get().getId().equals(doctor.getId());
        boolean phoneExists = existingDoctorByPhone.isPresent() && !existingDoctorByPhone.get().getId().equals(doctor.getId());

        if (emailExists || phoneExists) {
            throw new DuplicateEntryException("Duplicate Error: Email or Phone number already exists.");
        }

         */

        return doctorRepo.save(doctor);
    }

    @Override
    public void delete(Long id) {
        Doctor doctor = this.getById(id);
        doctorRepo.delete(doctor);
    }

    @Override
    public List<Doctor> findByIds(List<Long> ids) {
        return doctorRepo.findAllById(ids);
    }

}
