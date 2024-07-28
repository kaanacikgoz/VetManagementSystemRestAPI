package com.acikgozKaan.VetRestAPI.dao;

import com.acikgozKaan.VetRestAPI.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccineRepo extends JpaRepository<Vaccine, Long> {

    @Query("SELECT v FROM Vaccine v JOIN v.animalList a WHERE a.id = :animalId")
    List<Vaccine> findByAnimalId(@Param(("animalId")) Long animalId);

    @Query("SELECT v FROM Vaccine v JOIN v.animalList a WHERE a.id = :animalId AND v.code = :code AND v.protectionFinishDate > :currentDate")
    List<Vaccine> findActiveVaccinesByAnimalIdAndCode(@Param("animalId") Long animalId, @Param("code") String code, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT v FROM Vaccine v JOIN v.animalList a WHERE v.protectionFinishDate BETWEEN :startDate AND :endDate")
    List<Vaccine> findVaccinesByProtectionFinishDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
