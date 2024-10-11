package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAnimalService;
import com.acikgozKaan.VetRestAPI.business.abstracts.ICustomerService;
import com.acikgozKaan.VetRestAPI.core.modelMapper.IModelMapperService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
        String json = objectMapper.writeValueAsString(request);
        objectMapper.readValue(json, AnimalSaveRequest.class);

        mockMvc.perform(post("/v1/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))  // Use objectMapper to serialize request to JSON
                .andExpect(status().isCreated())  // expect HTTP 201 Created
                .andExpect(jsonPath("$.data.id").value(1L))  // validate response data
                .andExpect(jsonPath("$.data.name").value("Animal"));
    }



}