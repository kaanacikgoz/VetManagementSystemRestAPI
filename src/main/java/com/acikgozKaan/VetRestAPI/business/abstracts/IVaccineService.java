package com.acikgozKaan.VetRestAPI.business.abstracts;

import com.acikgozKaan.VetRestAPI.entity.Vaccine;

import java.util.List;

public interface IVaccineService {

    void save(Vaccine vaccine);

    List<Vaccine> getAll();

    Vaccine getById(Long id);

    Vaccine update(Vaccine vaccine);

    void delete(Long id);

}
