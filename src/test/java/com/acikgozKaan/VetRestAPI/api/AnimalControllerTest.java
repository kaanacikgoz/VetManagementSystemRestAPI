package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAnimalService;
import com.acikgozKaan.VetRestAPI.business.abstracts.ICustomerService;
import com.acikgozKaan.VetRestAPI.core.modelMapper.IModelMapperService;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dto.request.animal.AnimalSaveRequest;
import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private IModelMapperService modelMapperService;

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
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value(Msg.CREATED))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Animal"));
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