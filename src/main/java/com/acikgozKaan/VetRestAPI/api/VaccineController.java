package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAnimalService;
import com.acikgozKaan.VetRestAPI.business.abstracts.IVaccineService;
import com.acikgozKaan.VetRestAPI.core.exception.EnterDataException;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.modelMapper.IModelMapperService;
import com.acikgozKaan.VetRestAPI.core.result.ResultData;
import com.acikgozKaan.VetRestAPI.core.utilies.ResultHelper;
import com.acikgozKaan.VetRestAPI.dto.request.vaccine.VaccineSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.request.vaccine.VaccineUpdateRequest;
import com.acikgozKaan.VetRestAPI.dto.response.DoctorResponse;
import com.acikgozKaan.VetRestAPI.dto.response.VaccineResponse;
import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Doctor;
import com.acikgozKaan.VetRestAPI.entity.Vaccine;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/vaccines")
public class VaccineController {

    private final IVaccineService vaccineService;
    private final IAnimalService animalService;

    public VaccineController(IVaccineService vaccineService, IAnimalService animalService) {
        this.vaccineService = vaccineService;
        this.animalService = animalService;
    }

    //Evaluation Form 21
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<VaccineResponse> save(@Valid @RequestBody VaccineSaveRequest vaccineSaveRequest) {
        List<Animal> animals = animalService.findByIds(vaccineSaveRequest.getAnimalIds());

        if (animals.isEmpty()) {
            throw new EnterDataException("Please enter animalIds");
        }

        Vaccine vaccine = new Vaccine(
                vaccineSaveRequest.getName(),
                vaccineSaveRequest.getCode(),
                vaccineSaveRequest.getProtectionStartDate(),
                vaccineSaveRequest.getProtectionFinishDate(),
                animals
        );

        Vaccine savedVaccine = vaccineService.save(vaccine);

        VaccineResponse vaccineResponse = new VaccineResponse(
                vaccine.getId(),
                vaccine.getName(),
                vaccine.getCode(),
                vaccine.getProtectionStartDate(),
                vaccine.getProtectionFinishDate(),
                savedVaccine.getAnimalList().stream().map(
                        Animal::getId
                ).collect(Collectors.toList())
        );

        return ResultHelper.created(vaccineResponse);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getAll() {
        List<Vaccine> vaccines = vaccineService.getAll();

        List<VaccineResponse> vaccineResponses = vaccines.stream().map(
                vaccine -> {
                    List<Long> animalIds = vaccine.getAnimalList().stream()
                            .map(Animal::getId)
                            .collect(Collectors.toList());

                    return new VaccineResponse(
                            vaccine.getId(),
                            vaccine.getName(),
                            vaccine.getCode(),
                            vaccine.getProtectionStartDate(),
                            vaccine.getProtectionFinishDate(),
                            animalIds
                    );
                }).collect(Collectors.toList());

        return ResultHelper.success(vaccineResponses);
    }

    //Evaluation Form 24
    @GetMapping("/by-animal/{animalId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccinesByAnimalId(@PathVariable("animalId") Long animalId) {
        List<Vaccine> vaccines = vaccineService.findVaccinesByAnimalId(animalId);

        List<VaccineResponse> vaccineResponses = vaccines.stream().map(
                vaccine -> new VaccineResponse(
                        vaccine.getId(),
                        vaccine.getName(),
                        vaccine.getCode(),
                        vaccine.getProtectionStartDate(),
                        vaccine.getProtectionFinishDate(),
                        vaccine.getAnimalList().stream().map(Animal::getId).collect(Collectors.toList())
                )
        ).collect(Collectors.toList());

        return ResultHelper.success(vaccineResponses);
    }

    //Evaluation Form 23
    @GetMapping("/protection-finish-dates")
    public ResultData<List<VaccineResponse>> getVaccinesByProtectionFinishDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Vaccine> vaccines = vaccineService.findVaccinesByProtectionFinishDateBetween(startDate, endDate);

        List<VaccineResponse> vaccineResponses = vaccines.stream()
                .map(vaccine -> new VaccineResponse(
                        vaccine.getId(),
                        vaccine.getName(),
                        vaccine.getCode(),
                        vaccine.getProtectionStartDate(),
                        vaccine.getProtectionFinishDate(),
                        vaccine.getAnimalList().stream().map(
                                Animal::getId
                        ).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return ResultHelper.success(vaccineResponses);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> update(@PathVariable("id") Long id, @Valid @RequestBody VaccineUpdateRequest vaccineUpdateRequest) {

        List<Animal> animals = animalService.findByIds(vaccineUpdateRequest.getAnimalIds());

        Vaccine updateVaccine = new Vaccine(
                id,
                vaccineUpdateRequest.getName(),
                vaccineUpdateRequest.getCode(),
                vaccineUpdateRequest.getProtectionStartDate(),
                vaccineUpdateRequest.getProtectionFinishDate(),
                animals
        );

        if (animals.isEmpty()) {
            throw new NotFoundException("Animal not found!");
        }

        Vaccine updatedVaccine = vaccineService.update(updateVaccine);

        VaccineResponse vaccineResponse = new VaccineResponse(
                updatedVaccine.getId(),
                updatedVaccine.getName(),
                updatedVaccine.getCode(),
                updatedVaccine.getProtectionStartDate(),
                updatedVaccine.getProtectionFinishDate(),
                updatedVaccine.getAnimalList().stream().map(
                        Animal::getId
                ).collect(Collectors.toList())
        );

        return ResultHelper.success(vaccineResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> delete(@PathVariable("id") Long id) {
        Vaccine deletedVaccine = vaccineService.getById(id);
        this.vaccineService.delete(id);

        VaccineResponse vaccineResponse = new VaccineResponse(
                deletedVaccine.getId(),
                deletedVaccine.getName(),
                deletedVaccine.getCode(),
                deletedVaccine.getProtectionStartDate(),
                deletedVaccine.getProtectionFinishDate(),
                deletedVaccine.getAnimalList().stream().map(
                        Animal::getId
                ).collect(Collectors.toList())
        );

        return ResultHelper.deleted(vaccineResponse);
    }

}
