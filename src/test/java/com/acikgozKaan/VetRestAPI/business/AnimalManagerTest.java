package com.acikgozKaan.VetRestAPI.business;

import com.acikgozKaan.VetRestAPI.business.concretes.AnimalManager;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.AnimalRepo;
import com.acikgozKaan.VetRestAPI.dao.CustomerRepo;
import com.acikgozKaan.VetRestAPI.dto.request.animal.AnimalUpdateRequest;
import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Customer;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnimalManagerTest {

    @InjectMocks
    private AnimalManager animalManager;

    @Mock
    private AnimalRepo animalRepo;

    @Mock
    private CustomerRepo customerRepo;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        try {
            if (autoCloseable!=null) {
                autoCloseable.close();
            }
        } catch (Exception e) {
            System.out.println("Resource cannot be closed!");
        }
    }

    @Nested
    class saveAnimalTest {

        @Test
        void save_AnimalWithValidCustomer_Success() {
            // given
            Customer customer = new Customer();
            customer.setId(1L);

            Animal animal = new Animal();
            animal.setCustomer(customer);

            when(customerRepo.findById(customer.getId())).thenReturn(Optional.of(customer));
            when(animalRepo.save(animal)).thenReturn(animal);

            // when
            Animal savedAnimal = animalManager.save(animal);

            // then
            assertNotNull(savedAnimal, "Saved animal should not be null");
            assertEquals(animal.getCustomer(), savedAnimal.getCustomer(), "The customer should match the saved animal's customer");
            verify(animalRepo).save(animal);
        }

        @Test
        void save_AnimalWithNullCustomer_Success() {
            // given
            Animal animal = Animal.builder()
                    .customer(null)
                    .build();

            when(animalRepo.save(animal)).thenReturn(animal);

            // when
            Animal savedAnimal = animalManager.save(animal);

            // then
            assertNotNull(savedAnimal);
            assertNull(savedAnimal.getCustomer());
            verify(animalRepo).save(animal);
        }

        @Test
        void save_AnimalWithValidCustomerNullCustomerId_Success() {
            // given
            Customer customer = Customer.builder()
                    .id(null)
                    .build();

            Animal animal = Animal.builder()
                    .customer(customer)
                    .build();

            when(animalRepo.save(animal)).thenReturn(animal);

            // when
            Animal savedAnimal = animalManager.save(animal);

            // then
            assertNull(animal.getCustomer());
            assertNotNull(animal);
            verify(animalRepo).save(animal);
        }

        @Test
        void save_AnimalWithNonExistingCustomerId_ThrowsNotFoundException() {
            // given
            Customer customer = Customer.builder()
                    .id(1L)
                    .build();

            Animal animal = Animal.builder()
                    .customer(customer)
                    .build();

            when(customerRepo.findById(customer.getId())).thenReturn(Optional.empty());

            // when
            NotFoundException thrown = assertThrows(NotFoundException.class, ()-> animalManager.save(animal));

            // then
            assertEquals(NotFoundException.class, thrown.getClass());
            assertEquals("Customer not found", thrown.getMessage());
            verify(animalRepo, never()).save(animal);
        }
    }

    @Test
    void getAll_Animals_Success() {
        // given
        Animal animal = Animal.builder()
                .name("Animal")
                .build();

        Animal animal2 = Animal.builder()
                .name("Animal2")
                .build();

        Animal animal3 = Animal.builder()
                .name("Animal3")
                .build();

        when(animalRepo.findAll()).thenReturn(List.of(animal,animal2,animal3));

        // when
        List<Animal> foundAnimals = animalManager.getAll();

        // then
        assertThat(foundAnimals).hasSize(3);
        assertThat(foundAnimals).extracting(Animal::getName)
                .containsExactlyInAnyOrder("Animal","Animal2","Animal3");
    }

    @Nested
    class getByIdAnimalTest {

        @Test
        void getById_ExistingAnimal_Success() {
            // given
            Animal animal = Animal.builder()
                    .id(1L)
                    .name("Animal")
                    .build();

            when(animalRepo.findById(animal.getId())).thenReturn(Optional.of(animal));

            // when
            Animal foundAnimal = animalManager.getById(animal.getId());

            // then
            assertThat(foundAnimal).isNotNull();
            assertThat(foundAnimal).extracting(Animal::getName)
                    .isEqualTo("Animal");
        }

        @Test
        void getById_NonExistingAnimal_ThrowsNotFoundException() {
            // given
            Long nonExistingAnimalId = 1L;

            when(animalRepo.findById(nonExistingAnimalId)).thenReturn(Optional.empty());

            // when
            Exception thrown = assertThrows(NotFoundException.class,()->animalManager.getById(nonExistingAnimalId));

            // then
            assertEquals(NotFoundException.class, thrown.getClass());
            assertThat(thrown).extracting(Exception::getMessage)
                    .isEqualTo(Msg.NOT_FOUND);
        }
    }

    @Nested
    class updateAnimalTest {

        @Test
        void update_ExistingAnimalWithValidCustomerId_Success() {
            // given
            Animal animal = Animal.builder()
                    .id(1L)
                    .name("Animal")
                    .build();

            Customer customer = Customer.builder()
                    .id(1L)
                    .build();

            AnimalUpdateRequest animalUpdateRequest = new AnimalUpdateRequest();
            animalUpdateRequest.setCustomerId(customer.getId());
            animalUpdateRequest.setName("UpdatedAnimal");

            when(animalRepo.findById(animal.getId())).thenReturn(Optional.of(animal));
            when(customerRepo.findById(customer.getId())).thenReturn(Optional.of(customer));
            when(animalRepo.save(animal)).thenReturn(animal);

            // when
            Animal updatedAnimal = animalManager.update(animal.getId(), animalUpdateRequest);

            // then
            assertNotNull(updatedAnimal);
            assertEquals("UpdatedAnimal", updatedAnimal.getName());
            assertEquals(customer, updatedAnimal.getCustomer());
            verify(animalRepo).save(animal);
        }

        @Test
        void update_ExistingAnimalWithNullCustomerId_Success() {
            // given
            Animal animal = Animal.builder()
                    .id(1L)
                    .build();

            AnimalUpdateRequest animalUpdateRequest = new AnimalUpdateRequest();
            animalUpdateRequest.setCustomerId(null);

            when(animalRepo.findById(animal.getId())).thenReturn(Optional.of(animal));
            when(animalRepo.save(animal)).thenReturn(animal);

            // when
            Animal updatedAnimal = animalManager.update(animal.getId(), animalUpdateRequest);

            // then
            assertNull(updatedAnimal.getCustomer());
            verify(animalRepo).save(animal);
        }

        @Test
        void update_ExistingAnimalWithInvalidCustomerId_ThrowsNotFoundException() {
            // given
            Customer customer = Customer.builder()
                    .id(100L)
                    .build();

            Animal animal = Animal.builder()
                    .id(1L)
                    .customer(customer)
                    .build();

            AnimalUpdateRequest animalUpdateRequest = new AnimalUpdateRequest();
            animalUpdateRequest.setCustomerId(customer.getId());

            when(animalRepo.findById(animal.getId())).thenReturn(Optional.of(animal));
            when(customerRepo.findById(customer.getId())).thenReturn(Optional.empty());

            // when
            Exception thrown = assertThrows(NotFoundException.class, ()->animalManager.update(animal.getId(), animalUpdateRequest));

            // then
            assertEquals(NotFoundException.class, thrown.getClass());
            assertEquals("Customer not found", thrown.getMessage());
        }

        @Test
        void update_NonExistingAnimal_ThrowsNotFoundException() {
            // given
            Long nonExistingAnimalId = 1L;

            AnimalUpdateRequest animalUpdateRequest = new AnimalUpdateRequest();

            when(animalRepo.findById(nonExistingAnimalId)).thenReturn(Optional.empty());

            // when
            Exception thrown = assertThrows(NotFoundException.class, ()->animalManager.update(nonExistingAnimalId, animalUpdateRequest));

            // then
            assertEquals(NotFoundException.class, thrown.getClass());
            assertEquals("Animal not found", thrown.getMessage());
            verify(animalRepo, never()).save(any(Animal.class));
        }
    }

    @Test
    void delete_Animal_Success() {
        // given
        Animal animal = Animal.builder()
                .id(1L)
                .build();

        when(animalRepo.findById(animal.getId())).thenReturn(Optional.of(animal));

        // when
        animalManager.delete(animal.getId());

        // then
        verify(animalRepo).delete(animal);
    }

    @Nested
    class findByNameAnimalTest {

        @Test
        void findByName_Animal_Success() {
            // given
            Animal animal = Animal.builder()
                    .name("Animal")
                    .build();

            when(animalRepo.findByName(animal.getName())).thenReturn(List.of(animal));

            // when
            List<Animal> foundAnimals = animalManager.findByName(animal.getName());

            // then
            assertThat(foundAnimals).hasSize(1);
            assertThat(foundAnimals).extracting(Animal::getName)
                    .containsExactlyInAnyOrder("Animal");
        }

        @Test
        void findByName_Animals_Success() {
            // given
            Animal animal = Animal.builder()
                    .name("Animal")
                    .build();

            Animal animal2 = Animal.builder()
                    .name("Animal")
                    .build();

            when(animalRepo.findByName(animal.getName())).thenReturn(List.of(animal,animal2));

            // when
            List<Animal> foundAnimals = animalManager.findByName(animal.getName());

            // then
            assertThat(foundAnimals).hasSize(2);
            assertThat(foundAnimals).extracting(Animal::getName)
                    .containsExactlyInAnyOrder("Animal","Animal");
        }
    }

    @Test
    void findByCustomerId_Animal_Success() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .build();

        Animal animal = Animal.builder()
                .customer(customer)
                .build();

        when(animalRepo.findByCustomerId(customer.getId())).thenReturn(List.of(animal));

        // when
        List<Animal> foundAnimals = animalManager.findByCustomerId(customer.getId());

        // then
        assertThat(foundAnimals).hasSize(1);
        assertEquals(1L, animal.getCustomer().getId());
    }

    @Test
    void findByIds_Animals_Success() {
        // given
        Animal animal = Animal.builder()
                .id(1L)
                .build();

        Animal animal2 = Animal.builder()
                .id(2L)
                .build();

        Animal animal3 = Animal.builder()
                .id(3L)
                .build();

        when(animalRepo.findAllById(List.of(1L,2L,3L))).thenReturn(List.of(animal,animal2,animal3));

        // when
        List<Animal> foundAnimals = animalManager.findByIds(List.of(1L,2L,3L));

        // then
        assertThat(foundAnimals).hasSize(3);
    }

}
