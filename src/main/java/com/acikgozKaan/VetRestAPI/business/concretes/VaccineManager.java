package com.acikgozKaan.VetRestAPI.business.concretes;

import com.acikgozKaan.VetRestAPI.business.abstracts.IVaccineService;
import com.acikgozKaan.VetRestAPI.core.exception.DuplicateEntryException;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.AnimalRepo;
import com.acikgozKaan.VetRestAPI.dao.VaccineRepo;
import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Vaccine;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VaccineManager implements IVaccineService {

    private final VaccineRepo vaccineRepo;
    private final AnimalRepo animalRepo;

    public VaccineManager(VaccineRepo vaccineRepo, AnimalRepo animalRepo) {
        this.vaccineRepo = vaccineRepo;
        this.animalRepo = animalRepo;
    }

    //Evaluation Form 22
    @Override
    public Vaccine save(Vaccine vaccine) {
        Long animalId = vaccine.getAnimalList().get(0).getId(); // Assuming there's at least one animal
        List<Vaccine> existingVaccines = vaccineRepo.findActiveVaccinesByAnimalIdAndCode(animalId, vaccine.getCode(), LocalDate.now());

        if (!existingVaccines.isEmpty()) {
            throw new DuplicateEntryException("This animal already has an active vaccine of the same type.");
        }

        return vaccineRepo.save(vaccine);
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

    //Evaluation Form 22
    @Override
    public Vaccine update(Vaccine vaccine) {
        // Ensure the vaccine exists
        Vaccine existingVaccine = this.getById(vaccine.getId());

        // Check each new animal for existing active vaccines of the same type and if the animal exists
        for (Animal animal : vaccine.getAnimalList()) {
            Long animalId = animal.getId();

            // Check if the animal exists
            if (!animalRepo.existsById(animalId)) {
                throw new NotFoundException("Animal with ID " + animalId + " not found.");
            }

            List<Vaccine> existingVaccines = vaccineRepo.findActiveVaccinesByAnimalIdAndCode(animalId, vaccine.getCode(), LocalDate.now());

            // Exclude the current vaccine from the check to allow updates
            existingVaccines.removeIf(existingVaccineEntry -> existingVaccineEntry.getId().equals(vaccine.getId()));

            if (!existingVaccines.isEmpty()) {
                throw new DuplicateEntryException("Animal with ID " + animalId + " already has an active vaccine of the same type.");
            }
        }

        return vaccineRepo.save(vaccine);
    }

    @Override
    public void delete(Long id) {
        Vaccine vaccine = this.getById(id);
        vaccineRepo.delete(vaccine);
    }

    @Override
    public List<Vaccine> findVaccinesByAnimalId(Long animalId) {
        return vaccineRepo.findByAnimalId(animalId);
    }

    public List<Vaccine> findVaccinesByProtectionFinishDateBetween(LocalDate startDate, LocalDate endDate) {
        return vaccineRepo.findVaccinesByProtectionFinishDateBetween(startDate, endDate);
    }

}
