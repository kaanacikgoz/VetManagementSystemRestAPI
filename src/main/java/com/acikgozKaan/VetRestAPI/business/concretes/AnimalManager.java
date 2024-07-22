package com.acikgozKaan.VetRestAPI.business.concretes;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAnimalService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.AnimalRepo;
import com.acikgozKaan.VetRestAPI.entity.Animal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalManager implements IAnimalService {

    private final AnimalRepo animalRepo;

    public AnimalManager(AnimalRepo animalRepo) {
        this.animalRepo = animalRepo;
    }

    @Override
    public Animal save(Animal animal) {
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
    public Animal update(Animal animal) {
        this.getById(animal.getId());
        return this.animalRepo.save(animal);
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

}
