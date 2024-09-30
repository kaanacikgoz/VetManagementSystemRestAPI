package com.acikgozKaan.VetRestAPI.dao;

import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class AnimalRepoTest {

    @Autowired
    private AnimalRepo animalRepo;
    @Autowired
    private CustomerRepo customerRepo;

    @Test
    void testFindByName() {
        // given
        Animal animal = new Animal();
        animal.setName("Test");
        animal.setSpecies("Dog");
        animal.setBreed("Golden Retriever");
        animal.setGender(Animal.Gender.MALE);
        animal.setColour("Yellow");
        animal.setDateOfBirth(LocalDate.of(2023, 4, 14));

        animalRepo.save(animal);

        // when
        List<Animal> foundAnimals = animalRepo.findByName("Test");

        // then
        assertThat(foundAnimals).isNotNull();
        assertThat(foundAnimals.get(0).getName()).isEqualTo("Test");
    }

    @Test
    void testFindByCustomerId() {
        // given
        Customer customer = new Customer();
        customer.setName("Customer1");
        customer.setMail("kaan@test.com");
        customer.setCity("Manisa");
        customer.setPhone("123123123");
        customer.setAddress("Address1");

        customerRepo.save(customer);

        Animal animal = new Animal();
        animal.setName("Test2");
        animal.setSpecies("Dog");
        animal.setBreed("Golden Retriever");
        animal.setGender(Animal.Gender.MALE);
        animal.setColour("Yellow");
        animal.setDateOfBirth(LocalDate.of(2021, 2, 18));
        animal.setCustomer(customer);

        animalRepo.save(animal);

        // when
        List<Animal> foundAnimals = animalRepo.findByCustomerId(customer.getId());

        // then
        assertThat(foundAnimals.get(0).getCustomer().getId()).isEqualTo(customer.getId());
    }


}
