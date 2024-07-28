package com.acikgozKaan.VetRestAPI.business.abstracts;

import com.acikgozKaan.VetRestAPI.dto.request.animal.AnimalUpdateRequest;
import com.acikgozKaan.VetRestAPI.entity.Animal;

import java.util.List;

public interface IAnimalService {

    Animal save(Animal animal);

    List<Animal> getAll();

    Animal getById(Long id);

    Animal update(Long id, AnimalUpdateRequest animalUpdateRequest);

    void delete(Long id);

    List<Animal> findByName(String name);

    List<Animal> findByCustomerId(Long customerId);

    List<Animal> findByIds(List<Long> ids);

}
