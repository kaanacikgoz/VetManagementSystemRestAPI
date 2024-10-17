package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAnimalService;
import com.acikgozKaan.VetRestAPI.business.abstracts.ICustomerService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.modelMapper.IModelMapperService;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dto.request.animal.AnimalSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.request.animal.AnimalUpdateRequest;
import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(AnimalController.class)
public class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAnimalService animalService;

    @MockBean
    private ICustomerService customerService;

    @MockBean
    private IModelMapperService modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void save_Animal_Success() throws Exception {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .build();

        AnimalSaveRequest request = AnimalSaveRequest.builder()
                .name("Animal")
                .species("Dog")
                .breed("Golden")
                .gender(Animal.Gender.MALE)
                .colour("Yellow")
                .dateOfBirth(LocalDate.of(2020,10,25))
                .customerId(customer.getId())
                .build();

        Animal savedAnimal = Animal.builder()
                .id(1L)
                .name("Animal")
                .customer(customer)
                .build();

        // when
        when(customerService.getById(1L)).thenReturn(customer);
        when(animalService.save(any(Animal.class))).thenReturn(savedAnimal);

        // then
        mockMvc.perform(post("/v1/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                jsonPath("$.status").value(true),
                jsonPath("$.message").value(Msg.CREATED),
                jsonPath("$.data.id").value(1L),
                jsonPath("$.data.name").value("Animal")
        );
    }

    @Test
    void getAll_Animals_Success() throws Exception {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .build();

        Customer customer2 = Customer.builder()
                .id(2L)
                .build();

        Animal animal = Animal.builder()
                .id(1L)
                .name("Animal")
                .vaccineList(List.of())
                .customer(customer)
                .build();

        Animal animal2 = Animal.builder()
                .id(2L)
                .name("Animal2")
                .vaccineList(List.of())
                .customer(customer2)
                .build();

        // when
        when(animalService.getAll()).thenReturn(List.of(animal, animal2));

        // then
        mockMvc.perform(get("/v1/animals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value(Msg.OK))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[1].id").value(2L));
    }

    @Nested
    final class findByNameTest {

        @Test
        void findByName_Animal_Success() throws Exception {
            // given
            Customer customer = Customer.builder()
                    .id(1L)
                    .build();

            Animal animal = Animal.builder()
                    .id(1L)
                    .name("Animal")
                    .species("Dog")
                    .breed("Golden")
                    .gender(Animal.Gender.MALE)
                    .colour("Yellow")
                    .dateOfBirth(LocalDate.of(2024,3,10))
                    .customer(customer)
                    .vaccineList(List.of())
                    .build();

            String searchName = "Animal";

            // when
            when(animalService.findByName(searchName)).thenReturn(List.of(animal));

            // then
            mockMvc.perform(get("/v1/animals/search?")
                            .param("name", searchName)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.data").isArray(),
                            jsonPath("$.data[0].id").value(1L),
                            jsonPath("$.data[0].name").value("Animal"),
                            jsonPath("$.data[0].species").value("Dog"),
                            jsonPath("$.data[0].breed").value("Golden"),
                            jsonPath("$.data[0].gender").value("MALE"),
                            jsonPath("$.data[0].colour").value("Yellow"),
                            jsonPath("$.data[0].dateOfBirth").value("2024-03-10"),
                            jsonPath("$.data[0].customerId").value(1L)
                    );
        }

        @Test
        void findByName_NonExistingAnimal_EmptyResult() throws Exception {
            // given
            String nonExistingName = "Animal";

            // when
            when(animalService.findByName(nonExistingName)).thenReturn(List.of());

            // then
            mockMvc.perform(get("/v1/animals/search")
                            .param("name", nonExistingName)
                            .contentType(MediaType.APPLICATION_JSON))
                    //.andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.data").isEmpty()
                    );
        }

    }

    @Test
    void findByCustomerId_Animal_Success() throws Exception {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .build();

        Animal animal = Animal.builder()
                .id(2L)
                .name("Animal")
                .species("Dog")
                .breed("Golden")
                .gender(Animal.Gender.MALE)
                .colour("Yellow")
                .dateOfBirth(LocalDate.of(2024,6,20))
                .customer(customer)
                .vaccineList(List.of())
                .build();

        // when
        when(animalService.findByCustomerId(1L)).thenReturn(List.of(animal));

        // then
        mockMvc.perform(get("/v1/animals/owner/{customerId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value(true),
                        jsonPath("$.message").value(Msg.OK),
                        jsonPath("$.data").isArray(),
                        jsonPath("$.data[0].id").value(2L),
                        jsonPath("$.data[0].name").value("Animal"),
                        jsonPath("$.data[0].customerId").value(1L)
                );
    }

    @Nested
    final class updateTests {

        @Test
        void update_Animal_Success() throws Exception {
            // given
            Customer customer = Customer.builder()
                    .id(1L)
                    .build();

            AnimalUpdateRequest updateRequest = AnimalUpdateRequest.builder()
                    .name("UpdatedAnimal")
                    .species("Dog")
                    .breed("Golden")
                    .gender(Animal.Gender.MALE)
                    .colour("Yellow")
                    .dateOfBirth(LocalDate.of(2024,1,30))
                    .customerId(1L)
                    .build();

            Animal updatedAnimal = Animal.builder()
                    .id(1L)
                    .name("UpdatedAnimal")
                    .species("Dog")
                    .breed("Golden")
                    .gender(Animal.Gender.MALE)
                    .colour("Yellow")
                    .dateOfBirth(LocalDate.of(2024,1,30))
                    .customer(customer)
                    .vaccineList(List.of())
                    .build();
            /*
            AnimalResponse animalResponse = new AnimalResponse(
                    updatedAnimal.getId(),
                    updatedAnimal.getName(),
                    updatedAnimal.getSpecies(),
                    updatedAnimal.getBreed(),
                    updatedAnimal.getGender(),
                    updatedAnimal.getColour(),
                    updatedAnimal.getDateOfBirth(),
                    updatedAnimal.getCustomer().getId()
            );
            */

            // when
            /*
            ModelMapper modelMapperMock = Mockito.mock(ModelMapper.class);

            when(modelMapper.forRequest()).thenReturn(modelMapperMock);
            when(modelMapper.forResponse()).thenReturn(modelMapperMock);

             */

            //when(modelMapperMock.map(any(AnimalUpdateRequest.class), eq(Animal.class))).thenReturn(updatedAnimal);
            when(animalService.update(eq(1L), any(AnimalUpdateRequest.class))).thenReturn(updatedAnimal);
            //when(modelMapperMock.map(eq(updatedAnimal), eq(AnimalResponse.class))).thenReturn(animalResponse);

            // then

            mockMvc.perform(put("/v1/animals/{id}",1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest))
                    )
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.data.id").value(1L),
                            jsonPath("$.data.name").value("UpdatedAnimal"),
                            jsonPath("$.data.species").value("Dog"),
                            jsonPath("$.data.breed").value("Golden"),
                            jsonPath("$.data.gender").value("MALE"),
                            jsonPath("$.data.colour").value("Yellow"),
                            jsonPath("$.data.dateOfBirth").value("2024-01-30"),
                            jsonPath("$.data.customerId").value(1L)
                    );
        }

        @Test
        void update_Animal_NotFoundError() throws Exception {
            // given
            Long nonExistingAnimalId = 25L;

            AnimalUpdateRequest animalUpdateRequest = AnimalUpdateRequest.builder()
                    .name("UpdatedAnimal")
                    .species("Dog")
                    .breed("Golden")
                    .gender(Animal.Gender.FEMALE)
                    .colour("Yellow")
                    .dateOfBirth(LocalDate.of(2022, 10, 15))
                    .customerId(1L)
                    .build();

            // when
            when(animalService.update(anyLong(), any(AnimalUpdateRequest.class)))
                    .thenThrow(new NotFoundException("Animal not found"));

            // then
            mockMvc.perform(put("/v1/animals/{id}", nonExistingAnimalId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(animalUpdateRequest)))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(false),
                            jsonPath("$.message").value("Animal not found"),
                            jsonPath("$.code").value(404),
                            jsonPath("$.data").isEmpty()
                    );
        }

        @Test
        void update_Animal_InternalServerError() throws Exception {
            // given
            Long nonExistingAnimalId = 1L;

            AnimalUpdateRequest animalUpdateRequest = AnimalUpdateRequest.builder()
                    .name("UpdatedAnimal")
                    .species("Dog")
                    .breed("Golden")
                    .gender(Animal.Gender.FEMALE)
                    .colour("Yellow")
                    .dateOfBirth(LocalDate.of(2022, 10, 15))
                    .customerId(1L)
                    .build();

            // when
            when(animalService.update(anyLong(), any(AnimalUpdateRequest.class)))
                    .thenThrow(new RuntimeException("Unexpected error"));

            // then
            mockMvc.perform(put("/v1/animals/{id}", nonExistingAnimalId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(animalUpdateRequest)))
                    .andExpectAll(
                            status().isInternalServerError(),
                            jsonPath("$.status").value(false),
                            jsonPath("$.message").value("Unexpected error"),
                            jsonPath("$.code").value(500),
                            jsonPath("$.data").isEmpty()
                    );
        }

    }

    @Test
    void delete_Animal_Success() throws Exception {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .build();

        Animal animalToDelete = Animal.builder()
                .id(3L)
                .name("Animal")
                .species("Dog")
                .breed("Golden")
                .gender(Animal.Gender.FEMALE)
                .colour("Yellow")
                .dateOfBirth(LocalDate.of(2024,1,30))
                .customer(customer)
                .vaccineList(List.of())
                .build();

        // when
        when(animalService.getById(3L)).thenReturn(animalToDelete);
        doNothing().when(animalService).delete(3L);

        // then
        mockMvc.perform(delete("/v1/animals/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value(Msg.DELETED))
                .andExpect(jsonPath("$.data.id").value(3L))
                .andExpect(jsonPath("$.data.name").value("Animal"))
                .andExpect(jsonPath("$.data.species").value("Dog"))
                .andExpect(jsonPath("$.data.breed").value("Golden"))
                .andExpect(jsonPath("$.data.gender").value("FEMALE"))
                .andExpect(jsonPath("$.data.colour").value("Yellow"))
                .andExpect(jsonPath("$.data.dateOfBirth").value("2024-01-30"))
                .andExpect(jsonPath("$.data.customerId").value(1L));

        verify(animalService).delete(3L);
    }

}