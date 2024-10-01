package com.acikgozKaan.VetRestAPI.dao;

import com.acikgozKaan.VetRestAPI.entity.Doctor;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
public class DoctorRepoTest {

    @Autowired
    private DoctorRepo doctorRepo;

    @Test
    void testFindByMail() {
        // given
        Doctor doctor = Doctor.builder()
                .name("Doctor1")
                .phone("123123111")
                .mail("doctor1@mail.com")
                .address("Address1")
                .city("City1")
                .build();

        doctorRepo.save(doctor);

        // when
        Optional<Doctor> foundDoctors = doctorRepo.findByMail(doctor.getMail());

        // then
        assertThat(foundDoctors).isPresent();

        foundDoctors.ifPresent(doctor1 -> assertThat(doctor1.getMail()).isEqualTo(doctor.getMail()));
    }

    @Test
    void testFindByPhone() {
        // given
        Doctor doctor = Doctor.builder()
                .name("Doctor2")
                .phone("123123222")
                .mail("doctor2@mail.com")
                .address("Address2")
                .city("City2")
                .build();

        doctorRepo.save(doctor);

        // when
        Optional<Doctor> foundDoctors = doctorRepo.findByPhone(doctor.getPhone());

        // then
        assertThat(foundDoctors).isPresent();

        foundDoctors.ifPresent(doctor2 -> assertThat(doctor2.getPhone()).isEqualTo(doctor.getPhone()));
    }

    @Test
    void testFindByNonExistenceMail() {
        // when
        Optional<Doctor> foundDoctor = doctorRepo.findByMail("nonexistent@mail.com");

        // then
        assertThat(foundDoctor).isNotPresent();
    }

    @Test
    void testFindByNonExistencePhone() {
        // when
        Optional<Doctor> foundDoctor = doctorRepo.findByPhone("123123123");

        // then
        assertThat(foundDoctor).isNotPresent();
    }

    @Test
    void testFindByNullMail() {
        // when
        Optional<Doctor> foundDoctors = doctorRepo.findByMail(null);

        // then
        assertThat(foundDoctors).isNotPresent();
    }

    @Test
    void testSaveDoctorWithDuplicateMail() {
        // given
        Doctor doctor = Doctor.builder()
                .name("Doctor1")
                .phone("123123111")
                .mail("doctor1@mail.com")
                .address("Address1")
                .city("City1")
                .build();

        doctorRepo.save(doctor);

        Doctor doctor2 = Doctor.builder()
                .name("Doctor2")
                .phone("123123112")
                .mail("doctor1@mail.com")
                .address("Address2")
                .city("City2")
                .build();

        // then
        assertThatThrownBy(()-> doctorRepo.save(doctor2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testSaveDoctorWithDuplicatePhone() {
        // given
        Doctor doctor = Doctor.builder()
                .name("Doctor1")
                .phone("123123111")
                .mail("doctor1@mail.com")
                .address("Address1")
                .city("City1")
                .build();

        doctorRepo.save(doctor);

        Doctor doctor2 = Doctor.builder()
                .name("Doctor2")
                .phone("123123111")
                .mail("doctor2@mail.com")
                .address("Address2")
                .city("City2")
                .build();

        // then
        assertThatThrownBy(()-> doctorRepo.save(doctor2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void testSaveDoctorWithInvalidMail() {
        // given
        Doctor doctor = Doctor.builder()
                .name("Doctor")
                .phone("123123111")
                .mail("doctor1.com")
                .address("Address")
                .city("City")
                .build();

        // when + then
        assertThatThrownBy(()-> doctorRepo.save(doctor))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must be a well-formed email address");
    }

}
