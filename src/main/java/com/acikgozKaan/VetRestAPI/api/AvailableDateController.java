package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAvailableDateService;
import com.acikgozKaan.VetRestAPI.business.abstracts.IDoctorService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.modelMapper.IModelMapperService;
import com.acikgozKaan.VetRestAPI.core.result.ResultData;
import com.acikgozKaan.VetRestAPI.core.utilies.ResultHelper;
import com.acikgozKaan.VetRestAPI.dto.request.availableDate.AvailableDateSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.request.availableDate.AvailableDateUpdateRequest;
import com.acikgozKaan.VetRestAPI.dto.response.AvailableDateResponse;
import com.acikgozKaan.VetRestAPI.entity.AvailableDate;
import com.acikgozKaan.VetRestAPI.entity.Doctor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/available-dates")
public class AvailableDateController {

    private final IAvailableDateService availableDateService;
    private final IDoctorService doctorService;

    public AvailableDateController(IAvailableDateService availableDateService, IDoctorService doctorService, IModelMapperService modelMapper) {
        this.availableDateService = availableDateService;
        this.doctorService = doctorService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AvailableDateResponse> save(@Valid @RequestBody AvailableDateSaveRequest availableDateSaveRequest) {

        List<Doctor> doctors = doctorService.findByIds(availableDateSaveRequest.getDoctorIds());

        AvailableDate availableDate = new AvailableDate(
                availableDateSaveRequest.getAvailableDate(),
                doctors
        );

        if (doctors.isEmpty()) {
            throw new NotFoundException("Doctor not found!");
        }

        AvailableDate savedAvailableDate = availableDateService.save(availableDate);

        List<Long> doctorIds = savedAvailableDate.getDoctorList().stream().map(
                Doctor::getId
        ).collect(Collectors.toList());

        AvailableDateResponse availableDateResponse = new AvailableDateResponse(
                savedAvailableDate.getId(),
                savedAvailableDate.getAvailableDate(),
                doctorIds
        );

        return ResultHelper.created(availableDateResponse);
    }

    @GetMapping
    private ResultData<List<AvailableDateResponse>> getAll() {
        List<AvailableDate> availableDates = availableDateService.getAll();

        List<AvailableDateResponse> availableDateResponses = availableDates.stream().map(
                availableDate -> new AvailableDateResponse(
                        availableDate.getId(),
                        availableDate.getAvailableDate(),
                        availableDate.getDoctorList().stream().map(Doctor::getId).collect(Collectors.toList())
                )
        ).collect(Collectors.toList());

        return ResultHelper.success(availableDateResponses);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> update(@PathVariable("id") Long id, @Valid @RequestBody AvailableDateUpdateRequest availableDateUpdateRequest) {
        AvailableDate existingAvailableDate = availableDateService.getById(id);

        if (existingAvailableDate == null) {
            return ResultHelper.errorData("Available Date not found.");
        }

        // Update the availableDate field
        existingAvailableDate.setAvailableDate(availableDateUpdateRequest.getAvailableDate());

        // Retrieve the new list of doctors
        List<Doctor> newDoctors = doctorService.findByIds(availableDateUpdateRequest.getDoctorIds());

        // Update the doctors in the available date
        existingAvailableDate.setDoctorList(newDoctors);

        // Save the updated available date
        AvailableDate updatedAvailableDate = availableDateService.save(existingAvailableDate);

        // Prepare the response
        List<Long> doctorIds = updatedAvailableDate.getDoctorList().stream().map(
                Doctor::getId
        ).collect(Collectors.toList());

        AvailableDateResponse availableDateResponse = new AvailableDateResponse(
                updatedAvailableDate.getId(),
                updatedAvailableDate.getAvailableDate(),
                doctorIds
        );

        return ResultHelper.success(availableDateResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> delete(@PathVariable("id") Long id) {
        AvailableDate deletedAvailableDate = availableDateService.getById(id);
        /*
        deletedAvailableDate.getDoctorList().forEach(doctor -> {
            doctor.getAvailableDateList().remove(deletedAvailableDate);
            doctorService.save(doctor);
        });
         */

        availableDateService.delete(id);

        AvailableDateResponse availableDateResponse = new AvailableDateResponse(
                deletedAvailableDate.getId(),
                deletedAvailableDate.getAvailableDate(),
                deletedAvailableDate.getDoctorList().stream().map(Doctor::getId).collect(Collectors.toList())
        );

        return ResultHelper.deleted(availableDateResponse);
    }

}
