package com.acikgozKaan.VetRestAPI.business.abstracts;

import com.acikgozKaan.VetRestAPI.dto.request.vaccine.VaccineSaveRequest;
import com.acikgozKaan.VetRestAPI.entity.Vaccine;

import java.time.LocalDate;
import java.util.List;

public interface IVaccineService {

    Vaccine save(Vaccine vaccine);

    List<Vaccine> getAll();

    Vaccine getById(Long id);

    Vaccine update(Vaccine vaccine);

    void delete(Long id);

    List<Vaccine> findVaccinesByAnimalId(Long animalId);

    public List<Vaccine> findVaccinesByProtectionFinishDateBetween(LocalDate startDate, LocalDate endDate);

}
