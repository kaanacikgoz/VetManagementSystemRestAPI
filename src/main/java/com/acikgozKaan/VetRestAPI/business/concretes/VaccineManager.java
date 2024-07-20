package com.acikgozKaan.VetRestAPI.business.concretes;

import com.acikgozKaan.VetRestAPI.business.abstracts.IVaccineService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.VaccineRepo;
import com.acikgozKaan.VetRestAPI.entity.Vaccine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaccineManager implements IVaccineService {

    private final VaccineRepo vaccineRepo;

    public VaccineManager(VaccineRepo vaccineRepo) {
        this.vaccineRepo = vaccineRepo;
    }

    @Override
    public void save(Vaccine vaccine) {
        vaccineRepo.save(vaccine);
    }

    @Override
    public List<Vaccine> getAll() {
        return vaccineRepo.findAll();
    }

    @Override
    public Vaccine getById(Long id) {
        return vaccineRepo.findById(id).orElseThrow(
                ()->new NotFoundException(Msg.NOT_FOUND)
        );
    }

    @Override
    public Vaccine update(Vaccine vaccine) {
        this.getById(vaccine.getId());
        return this.vaccineRepo.save(vaccine);
    }

    @Override
    public void delete(Long id) {
        Vaccine vaccine = this.getById(id);
        vaccineRepo.delete(vaccine);
    }

}
