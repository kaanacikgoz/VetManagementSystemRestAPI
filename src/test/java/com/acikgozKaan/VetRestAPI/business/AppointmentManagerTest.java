package com.acikgozKaan.VetRestAPI.business;

import com.acikgozKaan.VetRestAPI.business.concretes.AppointmentManager;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.AppointmentRepo;
import com.acikgozKaan.VetRestAPI.dao.DoctorRepo;
import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Appointment;
import com.acikgozKaan.VetRestAPI.entity.AvailableDate;
import com.acikgozKaan.VetRestAPI.entity.Doctor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentManagerTest {

    @InjectMocks
    private AppointmentManager appointmentManager;

    @Mock
    private AppointmentRepo appointmentRepo;

    @Mock
    private DoctorRepo doctorRepo;

    @Test
    void save_Appointment_Success() {
        // given
        Appointment appointment = Appointment.builder()
                .id(1L)
                .build();

        // when
        when(appointmentRepo.save(appointment)).thenReturn(appointment);

        Appointment savedAppointment = appointmentManager.save(appointment);

        // then
        verify(appointmentRepo).save(appointment);
        assertThat(savedAppointment.getId()).isEqualTo(1L);
    }

    @Test
    void getAll_Appointments_Success() {
        // given
        Appointment appointment = Appointment.builder()
                .id(1L)
                .build();

        Appointment appointment2 = Appointment.builder()
                .id(2L)
                .build();

        // when
        when(appointmentRepo.findAll()).thenReturn(List.of(appointment, appointment2));

        List<Appointment> foundAppointments = appointmentManager.getAll();
        // then
        assertThat(foundAppointments).hasSize(2);
        assertThat(foundAppointments).extracting(Appointment::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Nested
    final class getByIdTest {

        @Test
        void getById_Appointment_Success() {
            // given
            Appointment appointment = Appointment.builder()
                    .id(1L)
                    .build();

            // when
            when(appointmentRepo.findById(appointment.getId())).thenReturn(Optional.of(appointment));

            Appointment foundAppointment = appointmentManager.getById(appointment.getId());

            // then
            assertThat(foundAppointment).isNotNull();
            assertThat(foundAppointment.getId()).isEqualTo(1L);
        }

        @Test
        void getById_Appointment_ThrowsNotFoundException() {
            // given
            Long nonExistingAppointmentId = 2L;

            // when
            when(appointmentRepo.findById(nonExistingAppointmentId)).thenReturn(Optional.empty());

            Exception thrown = assertThrows(NotFoundException.class, () -> appointmentManager.getById(nonExistingAppointmentId));

            // then
            assertThat(thrown).isInstanceOf(NotFoundException.class);
            assertThat(thrown.getMessage()).isEqualTo(Msg.NOT_FOUND);
        }

    }

    @Test
    void update_Appointment_Success() {
        // given
        Appointment appointment = Appointment.builder()
                .id(1L)
                .appointmentDate(LocalDateTime.of(2024,4,14,10,0))
                .build();

        // when
        when(appointmentRepo.findById(appointment.getId())).thenReturn(Optional.of(appointment));

        appointment.setAppointmentDate(LocalDateTime.of(2024,8,20,9,0));
        when(appointmentRepo.save(appointment)).thenReturn(appointment);

        Appointment updatedAppointment = appointmentManager.update(appointment);

        // then
        verify(appointmentRepo).save(appointment);
        assertThat(updatedAppointment.getId()).isEqualTo(1L);
        assertThat(updatedAppointment.getAppointmentDate()).isEqualTo(LocalDateTime.of(2024,8,20,9,0));
    }

    @Test
    void delete_Appointment_Success() {
        // given
        Appointment appointment = Appointment.builder()
                .id(1L)
                .build();

        // when
        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appointment));

        appointmentManager.delete(1L);

        // then
        verify(appointmentRepo).delete(appointment);
    }

    @Nested
    final class existsByDoctorAndAppointmentDateTest {

        @Test
        void existsByDoctorAndAppointmentDate_Appointment_ReturnTrue() {
            // given
            AvailableDate availableDate = AvailableDate.builder()
                    .id(1L)
                    .availableDate(LocalDate.of(2024,6,1))
                    .build();

            Doctor doctor = Doctor.builder()
                    .id(1L)
                    .name("Doctor")
                    .availableDateList(List.of(availableDate))
                    .build();

            Appointment appointment = Appointment.builder()
                    .id(1L)
                    .doctor(doctor)
                    .appointmentDate(LocalDateTime.of(2024,6,1,15,0))
                    .build();

            // when
            when(appointmentRepo.existsByDoctorAndAppointmentDate(doctor, appointment.getAppointmentDate())).thenReturn(true);

            Boolean isExist = appointmentManager.existsByDoctorAndAppointmentDate(doctor, LocalDateTime.of(2024,6,1,15,0));

            // then
            assertThat(isExist).isTrue();
        }

        @Test
        void existsByDoctorAndAppointmentDate_Appointment_ReturnFalse() {
            // given
            AvailableDate availableDate = AvailableDate.builder()
                    .id(1L)
                    .availableDate(LocalDate.of(2024,6,1))
                    .build();

            Doctor doctor = Doctor.builder()
                    .id(1L)
                    .name("Doctor")
                    .availableDateList(List.of(availableDate))
                    .build();

            Appointment appointment = Appointment.builder()
                    .id(1L)
                    .doctor(doctor)
                    .appointmentDate(LocalDateTime.of(2024,6,1,15,0))
                    .build();

            // when
            when(appointmentRepo.existsByDoctorAndAppointmentDate(doctor, LocalDateTime.of(2023,6,1,15,0))).thenReturn(false);

            Boolean isExist = appointmentManager.existsByDoctorAndAppointmentDate(doctor, LocalDateTime.of(2023,6,1,15,0));

            // then
            assertThat(isExist).isFalse();
        }
    }

    @Nested
    final class findByDoctorAndAppointmentDateBetweenTest {

        @Test
        void findByDoctorAndAppointmentDateBetween_Appointment_Success() {
            // given
            AvailableDate availableDate = AvailableDate.builder()
                    .id(1L)
                    .availableDate(LocalDate.of(2024,2,1))
                    .build();

            Doctor doctor = Doctor.builder()
                    .id(1L)
                    .name("Doctor")
                    .availableDateList(List.of(availableDate))
                    .build();

            Appointment appointment = Appointment.builder()
                    .id(1L)
                    .appointmentDate(LocalDateTime.of(2024,2,1,11,0))
                    .doctor(doctor)
                    .build();

            Appointment appointment2 = Appointment.builder()
                    .id(2L)
                    .appointmentDate(LocalDateTime.of(2024,2,1,15,0))
                    .doctor(doctor)
                    .build();

            // when
            when(doctorRepo.findById(appointment.getDoctor().getId())).thenReturn(Optional.of(doctor));

            when(appointmentRepo.findByDoctorAndAppointmentDateBetween(appointment.getDoctor(),
                    LocalDateTime.of(2023,6,1,15,0),
                    LocalDateTime.of(2024,6,1,15,0)))
                    .thenReturn(List.of(appointment, appointment2));

            List<Appointment> foundAppointments = appointmentManager.findByDoctorAndAppointmentDateBetween(
                    appointment.getDoctor().getId(),
                    LocalDateTime.of(2023,6,1,15,0),
                    LocalDateTime.of(2024,6,1,15,0)
            );

            // then
            assertThat(foundAppointments).hasSize(2);
            assertThat(foundAppointments).extracting(Appointment::getAppointmentDate)
                    .containsExactlyInAnyOrder(
                            LocalDateTime.of(2024,2,1,11,0),
                            LocalDateTime.of(2024,2,1,15,0)
                    );
        }

        @Test
        void findByDoctorAndAppointmentDateBetween_Appointment_ThrowsNotFoundException() {
            // given
            Long nonExistingDoctorId = 2L;

            // when
            when(doctorRepo.findById(nonExistingDoctorId)).thenReturn(Optional.empty());

            Exception thrown = assertThrows(NotFoundException.class, ()->appointmentManager.findByDoctorAndAppointmentDateBetween(
                    nonExistingDoctorId,
                    LocalDateTime.of(2023,6,1,15,0),
                    LocalDateTime.of(2024,6,1,15,0)
            ));

            // then
            assertThat(thrown).isInstanceOf(NotFoundException.class);
            assertThat(thrown.getMessage()).isEqualTo("Doctor not found with id " + nonExistingDoctorId);
        }
    }

    @Test
    void getAppointmentsByAnimalAndDateRange_Appointments_Success() {
        // given
        Animal animal = Animal.builder()
                .id(1L)
                .name("Animal")
                .build();

        Appointment appointment = Appointment.builder()
                .id(1L)
                .animal(animal)
                .appointmentDate(LocalDateTime.of(2024,3,27,14,0))
                .build();

        // when
        when(appointmentRepo.findByAnimalIdAndAppointmentDateBetween(
                appointment.getAnimal().getId(),
                LocalDateTime.of(2024,1,1,14,0),
                LocalDateTime.of(2024,5,27,14,0)
                )).thenReturn(List.of(appointment));

        List<Appointment> foundAppointments = appointmentManager.getAppointmentsByAnimalAndDateRange(
                appointment.getAnimal().getId(),
                LocalDateTime.of(2024,1,1,14,0),
                LocalDateTime.of(2024,5,27,14,0)
        );

        // then
        assertThat(foundAppointments).hasSize(1);
        assertThat(foundAppointments)
                .extracting(Appointment::getAppointmentDate)
                .containsExactlyInAnyOrder(LocalDateTime.of(2024,3,27,14,0));
    }

    @Nested
    final class findByIdsTest {

        @Test
        void findByIds_Appointments_Success() {
            // given
            Appointment appointment = Appointment.builder()
                    .id(1L)
                    .appointmentDate(LocalDateTime.of(2024,3,27,14,0))
                    .build();

            Appointment appointment2 = Appointment.builder()
                    .id(2L)
                    .appointmentDate(LocalDateTime.of(2024,2,10,16,0))
                    .build();

            // when
            when(appointmentRepo.findAllById(List.of(1L,2L))).thenReturn(List.of(appointment, appointment2));

            List<Appointment> foundAppointments = appointmentManager.findByIds(List.of(1L, 2L));

            // then
            assertThat(foundAppointments).hasSize(2);
            assertThat(foundAppointments)
                    .extracting(Appointment::getId)
                    .containsExactlyInAnyOrder(1L, 2L);
            assertThat(foundAppointments)
                    .extracting(Appointment::getAppointmentDate)
                    .containsExactlyInAnyOrder(
                            LocalDateTime.of(2024,3,27,14,0),
                            LocalDateTime.of(2024,2,10,16,0)
                    );
        }

        @Test
        void findByIds_Appointments_ReturnEmpty() {
            // when
            Long nonExistingAppointmentId = 1L;
            Long nonExistingAppointmentId2 = 2L;

            when(appointmentRepo.findAllById(List.of(
                    nonExistingAppointmentId,
                    nonExistingAppointmentId2))
            ).thenReturn(List.of());

            List<Appointment> foundAppointments = appointmentManager.findByIds(List.of(1L, 2L));

            // then
            assertThat(foundAppointments).isEmpty();
        }
    }

}
