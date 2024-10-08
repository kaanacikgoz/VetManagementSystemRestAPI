package com.acikgozKaan.VetRestAPI.business.concretes;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAnimalService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.AnimalRepo;
import com.acikgozKaan.VetRestAPI.dao.CustomerRepo;
import com.acikgozKaan.VetRestAPI.dto.request.animal.AnimalUpdateRequest;
import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnimalManager implements IAnimalService {

    private final AnimalRepo animalRepo;
    private final CustomerRepo customerRepo;

    public AnimalManager(AnimalRepo animalRepo, CustomerRepo customerRepo) {
        this.animalRepo = animalRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    @Transactional
    public Animal save(Animal animal) {
        if (animal.getCustomer() != null) {
            Long customerId = animal.getCustomer().getId();
            if (customerId != null) {
                Customer customer = customerRepo.findById(customerId).orElseThrow(
                        () -> new NotFoundException("Customer not found")
                );
                animal.setCustomer(customer);
            } else {
                animal.setCustomer(null); // Explicitly set customer to null if customerId is null
            }
        }
        return animalRepo.save(animal);
    }

    @Override
    public List<Animal> getAll() {
        return animalRepo.findAll();
    }

    @Override
    public Animal getById(Long id) {
        return animalRepo.findById(id).orElseThrow(
                ()->new NotFoundException(Msg.NOT_FOUND)
        );
    }

    @Override
    @Transactional
    public Animal update(Long id, AnimalUpdateRequest animalUpdateRequest) {
        Animal existingAnimal = animalRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Animal not found")
        );

        existingAnimal.setName(animalUpdateRequest.getName());
        existingAnimal.setSpecies(animalUpdateRequest.getSpecies());
        existingAnimal.setBreed(animalUpdateRequest.getBreed());
        existingAnimal.setGender(animalUpdateRequest.getGender());
        existingAnimal.setColour(animalUpdateRequest.getColour());
        existingAnimal.setDateOfBirth(animalUpdateRequest.getDateOfBirth());

        if (animalUpdateRequest.getCustomerId() != null) {
            Customer customer = customerRepo.findById(animalUpdateRequest.getCustomerId()).orElseThrow(
                    () -> new NotFoundException("Customer not found")
            );
            existingAnimal.setCustomer(customer);
        } else {
            existingAnimal.setCustomer(null); // Explicitly set customer to null if customerId is null
        }

        return animalRepo.save(existingAnimal);
    }

    @Override
    public void delete(Long id) {
        Animal animal = this.getById(id);
        animalRepo.delete(animal);
    }

    @Override
    public List<Animal> findByName(String name) {
        return animalRepo.findByName(name);
    }

    @Override
    public List<Animal> findByCustomerId(Long customerId) {
        return animalRepo.findByCustomerId(customerId);
    }

    @Override
    public List<Animal> findByIds(List<Long> ids) {
        return animalRepo.findAllById(ids);
    }

}
