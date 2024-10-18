package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAvailableDateService;
import com.acikgozKaan.VetRestAPI.business.abstracts.IDoctorService;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dto.request.availableDate.AvailableDateSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.request.availableDate.AvailableDateUpdateRequest;
import com.acikgozKaan.VetRestAPI.entity.AvailableDate;
import com.acikgozKaan.VetRestAPI.entity.Doctor;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvailableDateController.class)
public class AvailableDateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAvailableDateService availableDateService;

    @MockBean
    private IDoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    final class saveTest {

        @Test
        void save_AvailableDate_Success() throws Exception {
            // given
            Doctor doctor = Doctor.builder()
                    .id(1L)
                    .name("Doctor")
                    .build();

            AvailableDateSaveRequest saveRequest = new AvailableDateSaveRequest();
            saveRequest.setAvailableDate(LocalDate.of(2024,9,18));
            saveRequest.setDoctorIds(List.of(1L));

            AvailableDate availableDate = AvailableDate.builder()
                    .id(1L)
                    .availableDate(saveRequest.getAvailableDate())
                    .doctorList(List.of(doctor))
                    .build();

            // when
            when(doctorService.findByIds(saveRequest.getDoctorIds())).thenReturn(List.of(doctor));
            when(availableDateService.save(any(AvailableDate.class))).thenReturn(availableDate);

            // then
            mockMvc.perform(post("/v1/available-dates")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpectAll(
                            status().isCreated(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.CREATED),
                            jsonPath("$.code").value(201),
                            jsonPath("$.data.id").value(1L),
                            jsonPath("$.data.availableDate").value("2024-09-18"),
                            jsonPath("$.data.doctorIds[0]").value(1L)
                    );
        }

        @Test
        void save_AvailableDateHasNotDoctor_ThrowsNotFoundException() throws Exception {
            // given
            AvailableDateSaveRequest saveRequest = new AvailableDateSaveRequest();
            saveRequest.setAvailableDate(LocalDate.of(2024,9,18));
            saveRequest.setDoctorIds(List.of(1L));

            // when
            when(doctorService.findByIds(saveRequest.getDoctorIds())).thenReturn(List.of());

            // then
            mockMvc.perform(post("/v1/available-dates")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(false),
                            jsonPath("$.message").value("Doctor not found!"),
                            jsonPath("$.code").value(404)
                    );


        }
    }

    @Test
    void getAll_AvailableDate_Success() throws Exception {
        // given
        AvailableDate availableDate = AvailableDate.builder()
                .id(1L)
                .doctorList(List.of())
                .build();
        // when
        when(availableDateService.getAll()).thenReturn(List.of(availableDate));

        // then
        mockMvc.perform(get("/v1/available-dates"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value(true),
                        jsonPath("$.message").value(Msg.OK),
                        jsonPath("$.code").value(200),
                        jsonPath("$.data[0].id").value(1L)
                );
    }

    @Nested
    final class updateTest {

        @Test
        void update_AvailableDate_Success() throws Exception {
            // given
            Doctor doctor = Doctor.builder()
                    .id(1L)
                    .build();

            AvailableDateUpdateRequest updateRequest = new AvailableDateUpdateRequest();
            updateRequest.setAvailableDate(LocalDate.of(2024,10,18));
            updateRequest.setDoctorIds(List.of(1L));

            AvailableDate availableDate = AvailableDate.builder()
                    .id(1L)
                    .availableDate(updateRequest.getAvailableDate())
                    .doctorList(List.of(doctor))
                    .build();

            // when
            when(availableDateService.getById(1L)).thenReturn(availableDate);
            when(doctorService.findByIds(updateRequest.getDoctorIds())).thenReturn(List.of(doctor));
            when(availableDateService.save(availableDate)).thenReturn(availableDate);

            // then
            mockMvc.perform(put("/v1/available-dates/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data.id").value(1L),
                            jsonPath("$.data.availableDate").value("2024-10-18"),
                            jsonPath("$.data.doctorIds[0]").value(1L)
                    );
        }

        @Test
        void update_AvailableDate_ThrowsNotFoundException() throws Exception {
            // given
            AvailableDateUpdateRequest updateRequest = new AvailableDateUpdateRequest();
            updateRequest.setAvailableDate(LocalDate.of(2024,10,18));
            updateRequest.setDoctorIds(List.of());

            Long nonExistingAvailableDateId = 5L;

            // when
            when(availableDateService.getById(nonExistingAvailableDateId)).thenReturn(null);

            // then
            mockMvc.perform(put("/v1/available-dates/{id}", nonExistingAvailableDateId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(false),
                            jsonPath("$.message").value("Available Date not found."),
                            jsonPath("$.code").value(404)
                    );
        }
    }

    @Test
    void delete_AvailableDate_Success() throws Exception {
        // given
        AvailableDate availableDate = AvailableDate.builder()
                .id(1L)
                .doctorList(List.of())
                .build();

        // when
        when(availableDateService.getById(1L)).thenReturn(availableDate);
        doNothing().when(availableDateService).delete(1L);

        // then
        mockMvc.perform(delete("/v1/available-dates/{id}", 1L))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value(true),
                        jsonPath("$.message").value(Msg.DELETED),
                        jsonPath("$.code").value(200),
                        jsonPath("$.data.id").value(1L)
                );
    }

}
