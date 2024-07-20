package com.acikgozKaan.VetRestAPI.business.abstracts;

import com.acikgozKaan.VetRestAPI.entity.Animal;

import java.util.List;

public interface IAnimalService {

    void save(Animal animal);

    List<Animal> getAll();

    Animal getById(Long id);

    Animal update(Animal animal);

    void delete(Long id);

}
