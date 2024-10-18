package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAnimalService;
import com.acikgozKaan.VetRestAPI.business.abstracts.IVaccineService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dto.request.vaccine.VaccineSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.request.vaccine.VaccineUpdateRequest;
import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Vaccine;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VaccineController.class)
public class VaccineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IVaccineService vaccineService;

    @MockBean
    private IAnimalService animalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    final class saveTest {

        @Test
        void save_Vaccine_Success() throws Exception {
            // given
            Animal animal = Animal.builder()
                    .id(1L)
                    .name("Animal")
                    .build();

            VaccineSaveRequest saveRequest = new VaccineSaveRequest();
            saveRequest.setName("Vaccine");
            saveRequest.setCode("XXX");
            saveRequest.setProtectionStartDate(LocalDate.of(2023,8,20));
            saveRequest.setProtectionFinishDate(LocalDate.of(2024,8,20));
            saveRequest.setAnimalIds(List.of(1L));

            Vaccine savedVaccine = Vaccine.builder()
                    .id(1L)
                    .name("Vaccine")
                    .code("XXX")
                    .protectionStartDate(LocalDate.of(2023,8,20))
                    .protectionFinishDate(LocalDate.of(2024,8,20))
                    .animalList(List.of(animal))
                    .build();

            // when
            when(animalService.findByIds(saveRequest.getAnimalIds())).thenReturn(List.of(animal));
            when(vaccineService.save(any(Vaccine.class))).thenReturn(savedVaccine);

            // then
            mockMvc.perform(post("/v1/vaccines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpectAll(
                            status().isCreated(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.CREATED),
                            jsonPath("$.code").value(201),
                            jsonPath("$.data.id").value(1L),
                            jsonPath("$.data.name").value("Vaccine"),
                            jsonPath("$.data.code").value("XXX"),
                            jsonPath("$.data.protectionStartDate").value("2023-08-20"),
                            jsonPath("$.data.protectionFinishDate").value("2024-08-20"),
                            jsonPath("$.data.animalIds[0]").value(1L)
                    );
        }

        @Test
        void save_VaccineHasNotAnimals_ThrowsEnterDataException() throws Exception {
            // given
            VaccineSaveRequest saveRequest = new VaccineSaveRequest();
            saveRequest.setName("Vaccine");
            saveRequest.setCode("XXX");
            saveRequest.setProtectionStartDate(LocalDate.of(2023,8,20));
            saveRequest.setProtectionFinishDate(LocalDate.of(2024,8,20));
            saveRequest.setAnimalIds(null);

            // when
            when(animalService.findByIds(saveRequest.getAnimalIds())).thenReturn(List.of());

            // then
            mockMvc.perform(post("/v1/vaccines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").value(false),
                            jsonPath("$.message").value("Please enter animalIds"),
                            jsonPath("$.code").value(400),
                            jsonPath("$.data").isEmpty()
                    );
        }
    }

    @Nested
    final class getAllTest {

        @Test
        void getAll_Vaccine_Success() throws Exception {
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

            // when
            when(vaccineService.getAll()).thenReturn(List.of(vaccine));

            // then
            mockMvc.perform(get("/v1/vaccines"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data[0].id").value(1L),
                            jsonPath("$.data[0].name").value("Vaccine")
                    );
        }

        @Test
        void getAll_Vaccines_Success() throws Exception {
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

            // when
            when(vaccineService.getAll()).thenReturn(List.of(vaccine, vaccine2));

            // then
            mockMvc.perform(get("/v1/vaccines"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data[0].id").value(1L),
                            jsonPath("$.data[0].name").value("Vaccine"),
                            jsonPath("$.data[1].id").value(2L),
                            jsonPath("$.data[1].name").value("Vaccine2")
                    );
        }

    }

    @Nested
    final class getVaccinesByAnimalIdTest {

        @Test
        void getVaccinesByAnimalId_Vaccine_Success() throws Exception {
            // given
            Animal animal = Animal.builder()
                    .id(1L)
                    .build();

            Vaccine vaccine = Vaccine.builder()
                    .id(1L)
                    .name("Vaccine")
                    .animalList(List.of(animal))
                    .build();

            // when
            when(vaccineService.findVaccinesByAnimalId(animal.getId())).thenReturn(List.of(vaccine));

            // then
            mockMvc.perform(get("/v1/vaccines/by-animal/{animalId}", animal.getId()))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data[0].id").value(1L),
                            jsonPath("$.data[0].name").value("Vaccine")
                    );
        }

        @Test
        void getVaccinesByAnimalId_VaccineHasNotExistingAnimal_SuccessWithEmptyData() throws Exception {
            // given
            Long nonExistingAnimalId = 10L;

            // when
            when(vaccineService.findVaccinesByAnimalId(nonExistingAnimalId)).thenReturn(List.of());

            // then
            mockMvc.perform(get("/v1/vaccines/by-animal/{animalId}", nonExistingAnimalId))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data").isEmpty()
                    );
        }
    }

    @Nested
    final class getVaccinesByProtectionFinishDateBetweenTest {

        @Test
        void getVaccinesByProtectionFinishDateBetween_Vaccine_Success() throws Exception {
            // given
            Vaccine vaccine = Vaccine.builder()
                    .id(1L)
                    .name("Vaccine")
                    .code("XXX")
                    .protectionStartDate(LocalDate.of(2023,7,1))
                    .protectionFinishDate(LocalDate.of(2024,4,30))
                    .animalList(List.of())
                    .build();

            // when
            when(vaccineService.findVaccinesByProtectionFinishDateBetween(
                    LocalDate.of(2023,5,15),
                    LocalDate.of(2024,5,15))
            ).thenReturn(List.of(vaccine));

            // then
            mockMvc.perform(get("/v1/vaccines/protection-finish-dates")
                            .param("startDate", String.valueOf(LocalDate.of(2023,5,15)))
                            .param("endDate", String.valueOf(LocalDate.of(2024,5,15))))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data[0].id").value(1L),
                            jsonPath("$.data[0].name").value("Vaccine"),
                            jsonPath("$.data[0].code").value("XXX"),
                            jsonPath("$.data[0].protectionStartDate").value("2023-07-01"),
                            jsonPath("$.data[0].protectionFinishDate").value("2024-04-30")
                    );
        }

        @Test
        void getVaccinesByProtectionFinishDateBetween_Vaccine_SuccessWithEmptyData() throws Exception {
            // given
            Vaccine vaccine = Vaccine.builder()
                    .id(1L)
                    .name("Vaccine")
                    .code("XXX")
                    .protectionStartDate(LocalDate.of(2023,7,1))
                    .protectionFinishDate(LocalDate.of(2024,4,30))
                    .animalList(List.of())
                    .build();

            // when
            when(vaccineService.findVaccinesByProtectionFinishDateBetween(
                    LocalDate.of(2022,5,10),
                    LocalDate.of(2023,6,10))
            ).thenReturn(List.of());

            // then
            mockMvc.perform(get("/v1/vaccines/protection-finish-dates")
                            .param("startDate", String.valueOf(LocalDate.of(2022,5,10)))
                            .param("endDate", String.valueOf(LocalDate.of(2023,6,10))))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data").isEmpty()
                    );
        }
    }

    @Nested
    final class updateTest {

        @Test
        void update_Vaccine_Success() throws Exception {
            // given
            Animal animal = Animal.builder()
                    .id(1L)
                    .name("Animal")
                    .build();

            VaccineUpdateRequest updateRequest = new VaccineUpdateRequest();
            updateRequest.setName("UpdatedVaccine");
            updateRequest.setCode("XXX");
            updateRequest.setProtectionStartDate(LocalDate.of(2023,4,14));
            updateRequest.setProtectionFinishDate(LocalDate.of(2024,4,14));
            updateRequest.setAnimalIds(List.of(1L));

            Vaccine updatedVaccine = Vaccine.builder()
                    .id(1L)
                    .name(updateRequest.getName())
                    .code(updateRequest.getCode())
                    .protectionStartDate(updateRequest.getProtectionStartDate())
                    .protectionFinishDate(updateRequest.getProtectionFinishDate())
                    .animalList(List.of(animal))
                    .build();

            // when
            when(animalService.findByIds(updateRequest.getAnimalIds())).thenReturn(List.of(animal));
            when(vaccineService.update(any(Vaccine.class))).thenReturn(updatedVaccine);

            // then
            mockMvc.perform(put("/v1/vaccines/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data.id").value(1L),
                            jsonPath("$.data.name").value("UpdatedVaccine"),
                            jsonPath("$.data.code").value("XXX"),
                            jsonPath("$.data.protectionStartDate").value("2023-04-14"),
                            jsonPath("$.data.protectionFinishDate").value("2024-04-14"),
                            jsonPath("$.data.animalIds[0]").value(1L)
                    );

        }

        @Test
        void update_VaccineHasNotAnimal_ThrowsNotFoundException() throws Exception {
            // given
            VaccineUpdateRequest updateRequest = new VaccineUpdateRequest();
            updateRequest.setName("UpdatedVaccine");
            updateRequest.setCode("XXX");
            updateRequest.setProtectionStartDate(LocalDate.of(2023,4,14));
            updateRequest.setProtectionFinishDate(LocalDate.of(2024,4,14));
            updateRequest.setAnimalIds(null);

            Vaccine updatedVaccine = Vaccine.builder()
                    .id(1L)
                    .name(updateRequest.getName())
                    .code(updateRequest.getCode())
                    .protectionStartDate(updateRequest.getProtectionStartDate())
                    .protectionFinishDate(updateRequest.getProtectionFinishDate())
                    .animalList(null)
                    .build();

            // when
            when(animalService.findByIds(updateRequest.getAnimalIds())).thenReturn(List.of());

            // then
            mockMvc.perform(put("/v1/vaccines/{id}", updatedVaccine.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(false),
                            jsonPath("$.message").value("Animal not found!"),
                            jsonPath("$.code").value(404)
                    );
        }
    }

    @Nested
    final class deleteTest {

        @Test
        void delete_Vaccine_Success() throws Exception {
            // given
            Animal animal = Animal.builder()
                    .id(1L)
                    .build();

            Vaccine vaccine = Vaccine.builder()
                    .id(1L)
                    .name("Vaccine")
                    .animalList(List.of(animal))
                    .build();

            // when
            when(vaccineService.getById(vaccine.getId())).thenReturn(vaccine);

            // then
            mockMvc.perform(delete("/v1/vaccines/{id}", 1L))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.DELETED),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data.id").value(1L),
                            jsonPath("$.data.name").value("Vaccine")
                    );

        }

        @Test
        void delete_NonExistingVaccine_ThrowsNotFoundException() throws Exception {
            // given
            Long nonExistingVaccineId = 10L;

            // when
            when(vaccineService.getById(nonExistingVaccineId)).thenThrow(new NotFoundException(Msg.NOT_FOUND));

            // then
            mockMvc.perform(delete("/v1/vaccines/{id}", nonExistingVaccineId))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(false),
                            jsonPath("$.message").value(Msg.NOT_FOUND),
                            jsonPath("$.code").value(404)
                    );

            verify(vaccineService, never()).delete(nonExistingVaccineId);
        }

    }

}
