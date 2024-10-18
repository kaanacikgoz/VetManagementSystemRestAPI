package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAvailableDateService;
import com.acikgozKaan.VetRestAPI.business.abstracts.IDoctorService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dto.request.doctor.DoctorSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.request.doctor.DoctorUpdateRequest;
import com.acikgozKaan.VetRestAPI.entity.Appointment;
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

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDoctorService doctorService;

    @MockBean
    private IAvailableDateService availableDateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    final class saveTest {

        @Test
        void save_Doctor_Success() throws Exception {
            // given
            Doctor doctor = Doctor.builder()
                    .id(1L)
                    .build();

            List<Doctor> doctorList = new ArrayList<>();
            doctorList.add(doctor);

            AvailableDate availableDate = AvailableDate.builder()
                    .id(1L)
                    .doctorList(doctorList)
                    .build();

            DoctorSaveRequest saveRequest = new DoctorSaveRequest();
            saveRequest.setName("Doctor");
            saveRequest.setMail("doctor@mail.com");
            saveRequest.setPhone("123123123");
            saveRequest.setCity("City");
            saveRequest.setAddress("Address");
            saveRequest.setAvailableDateIds(List.of(availableDate.getId()));

            Doctor savedDoctor = Doctor.builder()
                    .id(1L)
                    .name(saveRequest.getName())
                    .availableDateList(List.of(availableDate))
                    .build();

            // when
            when(availableDateService.findByIds(saveRequest.getAvailableDateIds())).thenReturn(List.of(availableDate));
            when(doctorService.save(any(Doctor.class))).thenReturn(savedDoctor);

            // then
            mockMvc.perform(post("/v1/doctors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andDo(print())
                    .andExpectAll(
                            status().isCreated(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.CREATED),
                            jsonPath("$.code").value(201),
                            jsonPath("$.data.id").value(1L),
                            jsonPath("$.data.name").value("Doctor")
                    );
        }

        @Test
        void save_DoctorHasNotAvailableDate_ThrowNotFoundException() throws Exception {
            // given
            DoctorSaveRequest saveRequest = new DoctorSaveRequest();
            saveRequest.setName("Doctor");
            saveRequest.setMail("doctor@mail.com");
            saveRequest.setPhone("123123123");
            saveRequest.setCity("City");
            saveRequest.setAddress("Address");
            saveRequest.setAvailableDateIds(List.of());

            // when
            when(availableDateService.findByIds(List.of(anyLong()))).thenReturn(null);

            // then
            mockMvc.perform(post("/v1/doctors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveRequest)))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(false),
                            jsonPath("$.message").value("Not found availableDate!"),
                            jsonPath("$.code").value(404)
                    );
        }
    }

    @Test
    void getAll_Doctor_Success() throws Exception {
        // given
        Doctor doctor = Doctor.builder()
                .id(1L)
                .name("Doctor")
                .availableDateList(List.of())
                .appointmentList(List.of())
                .build();

        // when
        when(doctorService.getAll()).thenReturn(List.of(doctor));

        // then
        mockMvc.perform(get("/v1/doctors"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value(true),
                        jsonPath("$.message").value(Msg.OK),
                        jsonPath("$.code").value(200),
                        jsonPath("$.data[0].id").value(1L),
                        jsonPath("$.data[0].name").value("Doctor")
                );
    }

    @Nested
    final class updateTest {

        @Test
        void update_Doctor_Success() throws Exception {
            // given
            AvailableDate availableDate = AvailableDate.builder()
                    .id(1L)
                    .build();

            Appointment appointment = Appointment.builder()
                    .id(1L)
                    .build();

            DoctorUpdateRequest updateRequest = new DoctorUpdateRequest();
            updateRequest.setName("UpdatedDoctor");
            updateRequest.setMail("doctor@mail.com");
            updateRequest.setPhone("123123123");
            updateRequest.setCity("City");
            updateRequest.setAddress("Address");
            updateRequest.setAvailableDateIds(List.of(1L));

            Doctor existingDoctor = Doctor.builder()
                    .id(1L)
                    .build();

            existingDoctor.setName(updateRequest.getName());
            existingDoctor.setMail(updateRequest.getMail());
            existingDoctor.setPhone(updateRequest.getPhone());
            existingDoctor.setCity(updateRequest.getCity());
            existingDoctor.setAddress(updateRequest.getAddress());
            existingDoctor.setAvailableDateList(List.of(availableDate));
            existingDoctor.setAppointmentList(List.of(appointment));

            // when
            when(doctorService.getById(1L)).thenReturn(existingDoctor);
            when(availableDateService.findByIds(updateRequest.getAvailableDateIds())).thenReturn(List.of(availableDate));
            when(doctorService.save(existingDoctor)).thenReturn(existingDoctor);

            // then
            mockMvc.perform(put("/v1/doctors/{id}",1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data.id").value(1L),
                            jsonPath("$.data.name").value("UpdatedDoctor"),
                            jsonPath("$.data.availableDateIds[0]").value(1L),
                            jsonPath("$.data.appointmentIds[0]").value(1L)
                    );

        }

        @Test
        void update_NonExistingDoctor_ThrowsNotFoundException() throws Exception {
            // given
            Long nonExistingDoctorId = 10L;

            DoctorUpdateRequest updateRequest = new DoctorUpdateRequest();
            updateRequest.setName("UpdatedDoctor");
            updateRequest.setMail("doctor@mail.com");
            updateRequest.setPhone("123123123");
            updateRequest.setCity("City");
            updateRequest.setAddress("Address");
            updateRequest.setAvailableDateIds(List.of(1L));

            // when
            when(doctorService.getById(nonExistingDoctorId)).thenReturn(null);

            // then
            mockMvc.perform(put("/v1/doctors/{id}", nonExistingDoctorId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(false),
                            jsonPath("$.message").value("Doctor not found."),
                            jsonPath("$.code").value(404)
                    );
        }
    }

    @Test
    void delete_Doctor_Success() throws Exception {
        // given
        AvailableDate availableDate = AvailableDate.builder()
                .id(1L)
                .doctorList(new ArrayList<>())
                .build();

        Appointment appointment = Appointment.builder()
                .id(1L)
                .build();

        List<AvailableDate> availableDateList = new ArrayList<>();
        availableDateList.add(availableDate);

        Doctor doctor = Doctor.builder()
                .id(1L)
                .name("Doctor")
                .availableDateList(availableDateList)
                .appointmentList(List.of(appointment))
                .build();

        // when
        when(doctorService.getById(1L)).thenReturn(doctor);
        doNothing().when(doctorService).delete(1L);
        when(availableDateService.save(any(AvailableDate.class))).thenReturn(availableDate);

        // then
        mockMvc.perform(delete("/v1/doctors/{id}", 1L))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value(true),
                        jsonPath("$.message").value(Msg.DELETED),
                        jsonPath("$.code").value(200),
                        jsonPath("$.data.id").value(1L),
                        jsonPath("$.data.name").value("Doctor"),
                        jsonPath("$.data.availableDateIds[0]").value(1L),
                        jsonPath("$.data.appointmentIds[0]").value(1L)
                );
    }

}
