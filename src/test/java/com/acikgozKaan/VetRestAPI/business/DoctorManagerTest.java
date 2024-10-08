package com.acikgozKaan.VetRestAPI.business;

import com.acikgozKaan.VetRestAPI.business.concretes.DoctorManager;
import com.acikgozKaan.VetRestAPI.core.exception.DuplicateEntryException;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.DoctorRepo;
import com.acikgozKaan.VetRestAPI.entity.Doctor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorManagerTest {

    @InjectMocks
    private DoctorManager doctorManager;

    @Mock
    private DoctorRepo doctorRepo;

    @Nested
    final class saveTest {

        @Test
        void save_Doctor_Success() {
            // given
            Doctor existingDoctor = Doctor.builder()
                    .id(1L)
                    .name("Doctor")
                    .mail("doctor@mail.com")
                    .phone("123123111")
                    .build();

            Doctor willSaveDoctor = Doctor.builder()
                    .id(2L)
                    .name("Doctor2")
                    .mail("doctor2@mail.com")
                    .phone("123123222")
                    .build();

            // when
            when(doctorRepo.findByMail("doctor2@mail.com")).thenReturn(Optional.empty());
            when(doctorRepo.findByPhone("123123222")).thenReturn(Optional.empty());
            when(doctorRepo.save(willSaveDoctor)).thenReturn(willSaveDoctor);

            Doctor savedDoctor = doctorManager.save(willSaveDoctor);

            // then
            assertThat(savedDoctor).isNotNull();
            assertThat(savedDoctor.getName()).isEqualTo("Doctor2");
            verify(doctorRepo).save(willSaveDoctor);
        }

        @Test
        void save_DoctorWithDuplicateMail_ThrowsDuplicateEntryException() {
            // given
            Doctor existingDoctor = Doctor.builder()
                    .id(1L)
                    .name("Doctor")
                    .mail("doctor@mail.com")
                    .phone("123123111")
                    .build();

            Doctor willSaveDoctor = Doctor.builder()
                    .id(2L)
                    .name("Doctor2")
                    .mail("doctor@mail.com")
                    .phone("123123222")
                    .build();

            // when
            when(doctorRepo.findByMail("doctor@mail.com")).thenReturn(Optional.of(existingDoctor));
            when(doctorRepo.findByPhone("123123222")).thenReturn(Optional.empty());

            Exception thrown = assertThrows(DuplicateEntryException.class, ()->doctorManager.save(willSaveDoctor));

            // then
            assertThat(thrown).isInstanceOf(DuplicateEntryException.class);
            assertThat(thrown.getMessage()).isEqualTo("Duplicate Error: Email or Phone number already exists.");
            verify(doctorRepo, never()).save(any(Doctor.class));
        }

        @Test
        void save_DoctorWithDuplicatePhone_ThrowsDuplicateEntryException() {
            // given
            Doctor existingDoctor = Doctor.builder()
                    .id(1L)
                    .name("Doctor")
                    .mail("doctor@mail.com")
                    .phone("123123111")
                    .build();

            Doctor willSaveDoctor = Doctor.builder()
                    .id(2L)
                    .name("Doctor2")
                    .mail("doctor2@mail.com")
                    .phone("123123111")
                    .build();

            // when
            when(doctorRepo.findByMail("doctor2@mail.com")).thenReturn(Optional.empty());
            when(doctorRepo.findByPhone("123123111")).thenReturn(Optional.of(existingDoctor));

            Exception thrown = assertThrows(DuplicateEntryException.class, ()->doctorManager.save(willSaveDoctor));

            // then
            assertThat(thrown).isInstanceOf(DuplicateEntryException.class);
            assertThat(thrown.getMessage()).isEqualTo("Duplicate Error: Email or Phone number already exists.");
            verify(doctorRepo, never()).save(willSaveDoctor);
        }

    }


    @Test
    void getAll_Doctors_Success() {
        // given
        Doctor doctor = Doctor.builder()
                .id(1L)
                .name("Doctor")
                .build();

        Doctor doctor2 = Doctor.builder()
                .id(2L)
                .name("Doctor2")
                .build();

        // when
        when(doctorRepo.findAll()).thenReturn(List.of(doctor, doctor2));

        List<Doctor> foundDoctors = doctorManager.getAll();

        // then
        assertThat(foundDoctors).hasSize(2);
        assertThat(foundDoctors).extracting(Doctor::getName)
                .containsExactlyInAnyOrder("Doctor","Doctor2");
    }

    @Nested
    final class getByIdTest {

        @Test
        void getById_Doctor_Success() {
            // given
            Doctor doctor = Doctor.builder()
                    .id(1L)
                    .name("Doctor")
                    .build();

            // when
            when(doctorRepo.findById(doctor.getId())).thenReturn(Optional.of(doctor));

            Doctor foundDoctor = doctorManager.getById(doctor.getId());

            // then
            assertThat(foundDoctor.getId()).isEqualTo(1L);
            assertThat(foundDoctor.getName()).isEqualTo("Doctor");
        }

        @Test
        void getById_Doctor_ThrowsNotFoundException() {
            // given
            Long nonExistenceDoctorId = 2L;

            // when
            when(doctorRepo.findById(nonExistenceDoctorId)).thenReturn(Optional.empty());

            Exception thrown = assertThrows(NotFoundException.class, ()->doctorManager.getById(nonExistenceDoctorId));

            // then
            assertThat(thrown).isInstanceOf(NotFoundException.class);
            assertThat(thrown.getMessage()).isEqualTo(Msg.NOT_FOUND);
        }
    }

    @Test
    void update() {
        // given
        Doctor existingDoctor = Doctor.builder()
                .id(1L)
                .name("Doctor")
                .build();

        // when
        when(doctorRepo.findById(existingDoctor.getId())).thenReturn(Optional.of(existingDoctor));
        existingDoctor.setName("UpdatedDoctor");
        when(doctorRepo.save(existingDoctor)).thenReturn(existingDoctor);

        Doctor updateDoctor = doctorManager.update(existingDoctor);

        // then
        verify(doctorRepo).save(existingDoctor);
        assertThat(updateDoctor.getName()).isEqualTo("UpdatedDoctor");
    }

    @Test
    void delete_Doctor_Success() {
        // given
        Doctor doctor = Doctor.builder()
                .id(1L)
                .name("Doctor")
                .build();

        // when
        when(doctorRepo.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        doctorManager.delete(doctor.getId());

        // then
        verify(doctorRepo).delete(doctor);
        verify(doctorRepo).findById(doctor.getId());
    }

    @Test
    void findByIds_Doctors_Success() {
        // given
        Doctor doctor = Doctor.builder()
                .id(1L)
                .name("Doctor")
                .build();

        Doctor doctor2 = Doctor.builder()
                .id(2L)
                .name("Doctor2")
                .build();

        // when
        when(doctorRepo.findAllById(List.of(1L, 2L))).thenReturn(List.of(doctor, doctor2));

        List<Doctor> foundDoctors = doctorManager.findByIds(List.of(1L,2L));

        // then
        assertThat(foundDoctors).hasSize(2);
        assertThat(foundDoctors).extracting(Doctor::getName)
                .containsExactlyInAnyOrder("Doctor", "Doctor2");
    }


}
