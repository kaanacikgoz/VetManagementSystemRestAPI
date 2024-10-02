package com.acikgozKaan.VetRestAPI.dao;

import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Vaccine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class VaccineRepoTest {

    @Autowired
    private VaccineRepo vaccineRepo;

    @Autowired
    private AnimalRepo animalRepo;

    @Test
    void testFindVaccineByAnimalId() {
        // given
        Animal animal = Animal.builder()
                .name("Animal")
                .species("Golden")
                .breed("Dog")
                .gender(Animal.Gender.MALE)
                .colour("Yellow")
                .dateOfBirth(LocalDate.of(2020,2,12))
                .build();

        animalRepo.save(animal);

        Vaccine vaccine = Vaccine.builder()
                .name("Vaccine")
                .code("XXX1")
                .protectionStartDate(LocalDate.of(2021,4,17))
                .protectionFinishDate(LocalDate.of(2022,4,17))
                .animalList(List.of(animal))
                .build();

        Vaccine vaccine2 = Vaccine.builder()
                .name("Vaccine2")
                .code("XXX2")
                .protectionStartDate(LocalDate.of(2021,6,22))
                .protectionFinishDate(LocalDate.of(2022,6,22))
                .animalList(List.of(animal))
                .build();

        vaccineRepo.save(vaccine);
        vaccineRepo.save(vaccine2);

        // when
        List<Vaccine> foundVaccines = vaccineRepo.findByAnimalId(animal.getId());

        // then
        assertThat(foundVaccines).hasSize(2);
        assertThat(foundVaccines).extracting(Vaccine::getName)
                .containsExactlyInAnyOrder(vaccine.getName(), vaccine2.getName());
    }

    @Test
    void findActiveVaccinesByAnimalIdAndCode() {
        // given
        Animal animal = Animal.builder()
                .name("Animal")
                .species("Golden")
                .breed("Dog")
                .gender(Animal.Gender.MALE)
                .colour("Yellow")
                .dateOfBirth(LocalDate.of(2020,2,12))
                .build();

        animalRepo.save(animal);

        Vaccine vaccine = Vaccine.builder()
                .name("Vaccine")
                .code("XXX1")
                .protectionStartDate(LocalDate.now().minusDays(10))
                .protectionFinishDate(LocalDate.now().plusDays(10))
                .animalList(List.of(animal))
                .build();

        vaccineRepo.save(vaccine);

        // when
        List<Vaccine> foundVaccines = vaccineRepo.findActiveVaccinesByAnimalIdAndCode(animal.getId(), vaccine.getCode(), LocalDate.now());

        // then
        assertThat(foundVaccines).extracting(Vaccine::getName)
                .containsExactlyInAnyOrder(vaccine.getName());
    }

    @Test
    void findNoActiveVaccinesByAnimalIdAndCode() {
        // given
        Animal animal = Animal.builder()
                .name("Animal")
                .species("Golden")
                .breed("Dog")
                .gender(Animal.Gender.MALE)
                .colour("Yellow")
                .dateOfBirth(LocalDate.of(2020,2,12))
                .build();

        animalRepo.save(animal);

        Vaccine vaccine = Vaccine.builder()
                .name("Vaccine")
                .code("XXX1")
                .protectionStartDate(LocalDate.of(2021,8,24))
                .protectionFinishDate(LocalDate.of(2023,8,24))
                .animalList(List.of(animal))
                .build();

        vaccineRepo.save(vaccine);

        // when
        List<Vaccine> foundVaccines = vaccineRepo.findActiveVaccinesByAnimalIdAndCode(animal.getId(), vaccine.getCode(), LocalDate.now());

        // then
        assertThat(foundVaccines).isEmpty();
    }

    @Test
    void findVaccinesByProtectionFinishDateBetween() {
        // given
        Animal animal = Animal.builder()
                .name("Animal")
                .species("Golden")
                .breed("Dog")
                .gender(Animal.Gender.MALE)
                .colour("Yellow")
                .dateOfBirth(LocalDate.of(2020,2,12))
                .build();

        animalRepo.save(animal);

        Vaccine vaccine = Vaccine.builder()
                .name("Vaccine")
                .code("XXX1")
                .protectionStartDate(LocalDate.of(2022,6,26))
                .protectionFinishDate(LocalDate.of(2022,7,26))
                .animalList(List.of(animal))
                .build();

        vaccineRepo.save(vaccine);

        // when
        List<Vaccine> foundVaccines = vaccineRepo.findVaccinesByProtectionFinishDateBetween(
                LocalDate.of(2022,6,25),LocalDate.of(2022,8,28));

        // then
        assertThat(foundVaccines).extracting(Vaccine::getName)
                .containsExactlyInAnyOrder(vaccine.getName());
    }

    @Test
    void findNoVaccinesByProtectionFinishDateBetween() {
        // given
        Animal animal = Animal.builder()
                .name("Animal")
                .species("Golden")
                .breed("Dog")
                .gender(Animal.Gender.MALE)
                .colour("Yellow")
                .dateOfBirth(LocalDate.of(2020,2,12))
                .build();

        animalRepo.save(animal);

        Vaccine vaccine = Vaccine.builder()
                .name("Vaccine")
                .code("XXX1")
                .protectionStartDate(LocalDate.of(2022,6,26))
                .protectionFinishDate(LocalDate.of(2022,7,26))
                .animalList(List.of(animal))
                .build();

        vaccineRepo.save(vaccine);

        // when
        List<Vaccine> foundVaccines = vaccineRepo.findVaccinesByProtectionFinishDateBetween(
                LocalDate.of(2022,7,28),LocalDate.of(2022,8,28));

        // then
        assertThat(foundVaccines).isEmpty();
    }

    @Test
    void testAnimalHasVaccinesAfterSaving() {
        // given
        Animal animal = Animal.builder()
                .name("Animal")
                .species("Golden")
                .breed("Dog")
                .gender(Animal.Gender.MALE)
                .colour("Yellow")
                .dateOfBirth(LocalDate.of(2020, 2, 12))
                .build();

        animalRepo.save(animal);

        Vaccine vaccine = Vaccine.builder()
                .name("Vaccine")
                .code("XXX1")
                .protectionStartDate(LocalDate.of(2021, 4, 17))
                .protectionFinishDate(LocalDate.of(2022, 4, 17))
                .animalList(new ArrayList<>(List.of(animal)))
                .build();

        vaccineRepo.save(vaccine);

        animal.setVaccineList(new ArrayList<>(List.of(vaccine)));

        animalRepo.save(animal);

        // when
        Animal foundAnimal = animalRepo.findById(animal.getId()).get();

        // then
        assertThat(foundAnimal.getVaccineList()).isNotNull();
        assertThat(foundAnimal.getVaccineList()).hasSize(1);
        assertThat(foundAnimal.getVaccineList().get(0).getName()).isEqualTo(vaccine.getName());
    }

}
