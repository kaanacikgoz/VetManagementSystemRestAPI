package com.acikgozKaan.VetRestAPI.business;

import com.acikgozKaan.VetRestAPI.business.concretes.VaccineManager;
import com.acikgozKaan.VetRestAPI.core.exception.DuplicateEntryException;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.AnimalRepo;
import com.acikgozKaan.VetRestAPI.dao.VaccineRepo;
import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Vaccine;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VaccineManagerTest {

    @InjectMocks
    private VaccineManager vaccineManager;

    @Mock
    private VaccineRepo vaccineRepo;

    @Mock
    private AnimalRepo animalRepo;

    /*
    The vaccine must be saved with animals;
     */

    @Nested
    final class saveTest {

        @Test
        void save_Vaccine_Success() {
            // given
            Animal animal = Animal.builder()
                    .id(1L)
                    .name("Animal")
                    .build();

            Vaccine vaccine = Vaccine.builder()
                    .id(1L)
                    .name("Vaccine")
                    .code("XXX")
                    .animalList(List.of(animal))
                    .build();

            when(vaccineRepo.findActiveVaccinesByAnimalIdAndCode(
                    vaccine.getAnimalList().get(0).getId(), vaccine.getCode(), LocalDate.now()
            )).thenReturn(List.of());
            when(vaccineRepo.save(vaccine)).thenReturn(vaccine);

            // when
            Vaccine savedVaccine = vaccineManager.save(vaccine);

            // then
            assertThat(savedVaccine).isNotNull();
            assertThat(savedVaccine.getName()).isEqualTo("Vaccine");
            verify(vaccineRepo).save(vaccine);
        }

        @Test
        void save_Vaccine_ThrowsDuplicateEntryException() {
            // given
            Animal animal = Animal.builder()
                    .id(1L)
                    .name("Animal")
                    .build();

            Vaccine vaccine = Vaccine.builder()
                    .id(1L)
                    .name("Vaccine")
                    .animalList(List.of(animal))
                    .build();

            Vaccine vaccine2 = Vaccine.builder()
                    .id(2L)
                    .name("Vaccine2")
                    .animalList(List.of(animal))
                    .build();

            when(vaccineRepo.findActiveVaccinesByAnimalIdAndCode(
                    vaccine.getAnimalList().get(0).getId(), vaccine.getCode(), LocalDate.now()
            )).thenReturn(List.of(vaccine));

            // when
            Exception thrown = assertThrows(DuplicateEntryException.class, ()->vaccineManager.save(vaccine2));

            // then
            assertThat(thrown).isInstanceOf(DuplicateEntryException.class);
            assertThat(thrown.getMessage()).isEqualTo("This animal already has an active vaccine of the same type.");
            verify(vaccineRepo, never()).save(vaccine2);
        }

    }

    @Test
    void getAll_Vaccines_Success() {
        // given
        Vaccine vaccine = Vaccine.builder()
                .id(1L)
                .name("Vaccine")
                .build();

        Vaccine vaccine2 = Vaccine.builder()
                .id(2L)
                .name("Vaccine2")
                .build();

        when(vaccineRepo.findAll()).thenReturn(List.of(vaccine, vaccine2));

        // when
        List<Vaccine> foundVaccines = vaccineManager.getAll();

        // then
        assertThat(foundVaccines).hasSize(2);
        assertThat(foundVaccines).extracting(Vaccine::getName)
                .containsExactlyInAnyOrder("Vaccine","Vaccine2");
    }

    @Nested
    final class getByIdTest {

        @Test
        void getById_Vaccine_Success() {
            // given
            Vaccine vaccine = Vaccine.builder()
                    .id(1L)
                    .name("Vaccine")
                    .build();

            when(vaccineRepo.findById(1L)).thenReturn(Optional.of(vaccine));

            // when
            Vaccine foundVaccine = vaccineManager.getById(1L);

            // then
            assertThat(foundVaccine).isNotNull();
            assertThat(foundVaccine.getName()).isEqualTo("Vaccine");
        }

        @Test
        void getById_Vaccine_ThrowsNotFoundException() {
            // given
            Long nonExistentVaccineId = 1L;

            when(vaccineRepo.findById(nonExistentVaccineId)).thenReturn(Optional.empty());

            // when
            Exception thrown = assertThrows(NotFoundException.class, ()->vaccineManager.getById(nonExistentVaccineId));

            // then
            verify(vaccineRepo).findById(nonExistentVaccineId);
            assertThat(thrown.getMessage()).isEqualTo(Msg.NOT_FOUND);
        }

    }

    @Nested
    final class updateTest {

        @Test
        void update_Vaccine_Success() {
            // given

            // when

            // then
        }

        @Test
        void update_Vaccine_ThrowsNotFoundException() {
            // given

            // when

            // then
        }

        @Test
        void update_Vaccine_ThrowsDuplicateEntryException() {
            // given

            // when

            // then
        }

    }

    @Test
    void update_Vaccine_Success() {

    }

    @Test
    void delete_Vaccine_Success() {
        // given
        Vaccine vaccine = Vaccine.builder()
                .id(1L)
                .name("Vaccine")
                .build();

        when(vaccineRepo.findById(1L)).thenReturn(Optional.of(vaccine));

        // when
        vaccineManager.delete(1L);

        // then
        verify(vaccineRepo).findById(1L);
        verify(vaccineRepo).delete(vaccine);
    }

    @Nested
    final class findVaccinesByAnimalIdTest {

        @Test
        void findVaccinesByAnimalId_Vaccine_Success() {
            // given
            Animal animal = Animal.builder()
                    .id(1L)
                    .name("Animal")
                    .build();

            Vaccine vaccine = Vaccine.builder()
                    .id(1L)
                    .name("Vaccine")
                    .animalList(List.of(animal))
                    .build();

            when(vaccineRepo.findByAnimalId(1L)).thenReturn(List.of(vaccine));

            // when
            List<Vaccine> foundVaccines = vaccineManager.findVaccinesByAnimalId(1L);

            // then
            assertThat(foundVaccines).hasSize(1);
            assertThat(foundVaccines).extracting(Vaccine::getName)
                    .containsExactlyInAnyOrder("Vaccine");
        }

        @Test
        void findVaccinesByAnimalId_Vaccines_Success() {
            // given
            Animal animal = Animal.builder()
                    .id(1L)
                    .name("Animal")
                    .build();

            Vaccine vaccine = Vaccine.builder()
                    .id(1L)
                    .name("Vaccine")
                    .animalList(List.of(animal))
                    .build();

            Vaccine vaccine2 = Vaccine.builder()
                    .id(2L)
                    .name("Vaccine2")
                    .animalList(List.of(animal))
                    .build();

            when(vaccineRepo.findByAnimalId(1L)).thenReturn(List.of(vaccine, vaccine2));

            // when
            List<Vaccine> foundVaccines = vaccineManager.findVaccinesByAnimalId(1L);

            // then
            assertThat(foundVaccines).hasSize(2);
            assertThat(foundVaccines).extracting(Vaccine::getName)
                    .containsExactlyInAnyOrder("Vaccine","Vaccine2");
        }

    }

    @Test
    void findVaccinesByProtectionFinishDateBetween_Vaccine_Success() {
        // given
        Vaccine vaccine = Vaccine.builder()
                .id(1L)
                .name("Vaccine")
                .protectionStartDate(LocalDate.of(2024,1,1))
                .protectionFinishDate(LocalDate.of(2024,3,1))
                .build();

        when(vaccineRepo.findVaccinesByProtectionFinishDateBetween(
                LocalDate.of(2024,1,1),
                LocalDate.of(2024,2,1))
        ).thenReturn(List.of(vaccine));

        // when
        List<Vaccine> foundVaccines = vaccineManager.findVaccinesByProtectionFinishDateBetween(
                LocalDate.of(2024,1,1),
                LocalDate.of(2024,2,1)
        );

        // then
        assertThat(foundVaccines).isNotEmpty();
        assertThat(foundVaccines).extracting(Vaccine::getName)
                .containsExactlyInAnyOrder("Vaccine");
    }

}
