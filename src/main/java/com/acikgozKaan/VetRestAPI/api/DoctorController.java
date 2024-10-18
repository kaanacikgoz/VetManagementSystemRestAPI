package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAvailableDateService;
import com.acikgozKaan.VetRestAPI.business.abstracts.IDoctorService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.result.ResultData;
import com.acikgozKaan.VetRestAPI.core.utilies.ResultHelper;
import com.acikgozKaan.VetRestAPI.dto.request.doctor.DoctorSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.request.doctor.DoctorUpdateRequest;
import com.acikgozKaan.VetRestAPI.dto.response.DoctorResponse;
import com.acikgozKaan.VetRestAPI.entity.Appointment;
import com.acikgozKaan.VetRestAPI.entity.AvailableDate;
import com.acikgozKaan.VetRestAPI.entity.Doctor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/doctors")
public class DoctorController {

    private final IDoctorService doctorService;
    private final IAvailableDateService availableDateService;

    public DoctorController(IDoctorService doctorService, IAvailableDateService availableDateService) {
        this.doctorService = doctorService;
        this.availableDateService = availableDateService;
    }

    //Evaluation Form 15
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<DoctorResponse> save(@Valid @RequestBody DoctorSaveRequest doctorSaveRequest) {

        List<AvailableDate> availableDates = availableDateService.findByIds(doctorSaveRequest.getAvailableDateIds());

        if (availableDates.isEmpty()) {
            throw new NotFoundException("Not found availableDate!");
        }

        Doctor doctor = new Doctor(
                doctorSaveRequest.getName(),
                doctorSaveRequest.getPhone(),
                doctorSaveRequest.getMail(),
                doctorSaveRequest.getAddress(),
                doctorSaveRequest.getCity(),
                availableDates
                //appointments
        );

        Doctor savedDoctor = doctorService.save(doctor);

        for (AvailableDate availableDate : availableDates) {
            availableDate.getDoctorList().add(savedDoctor);
            availableDateService.save(availableDate); // Save the updated availableDate to persist the relationship
        }

        DoctorResponse doctorResponse = new DoctorResponse(
                savedDoctor.getId(),
                savedDoctor.getName(),
                savedDoctor.getPhone(),
                savedDoctor.getMail(),
                savedDoctor.getAddress(),
                savedDoctor.getCity(),
                savedDoctor.getAvailableDateList().stream().map(
                        AvailableDate::getId
                ).collect(Collectors.toList())
                /*
                savedDoctor.getAppointmentList().stream().map(
                        Appointment::getId
                ).collect(Collectors.toList())
                 */
        );

        return ResultHelper.created(doctorResponse);
    }

    @GetMapping
    public ResultData<List<DoctorResponse>> getAll() {
        List<Doctor> doctors = doctorService.getAll();

        List<DoctorResponse> doctorResponses = doctors.stream().map(
                doctor -> {
                    List<Long> availableDateIds = doctor.getAvailableDateList().stream()
                            .map(AvailableDate::getId)
                            .collect(Collectors.toList());

                    List<Long> appointmentsIds = doctor.getAppointmentList().stream()
                            .map(Appointment::getId)
                            .collect(Collectors.toList());

                    return new DoctorResponse(
                            doctor.getId(),
                            doctor.getName(),
                            doctor.getPhone(),
                            doctor.getMail(),
                            doctor.getAddress(),
                            doctor.getCity(),
                            availableDateIds,
                            appointmentsIds
                    );
                }).collect(Collectors.toList());

        return ResultHelper.success(doctorResponses);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> update(@PathVariable("id") Long id, @Valid @RequestBody DoctorUpdateRequest doctorUpdateRequest) {
        Doctor existingDoctor = doctorService.getById(id);

        if (existingDoctor == null) {
            throw new NotFoundException("Doctor not found.");
        }

        existingDoctor.setName(doctorUpdateRequest.getName());
        existingDoctor.setPhone(doctorUpdateRequest.getPhone());
        existingDoctor.setMail(doctorUpdateRequest.getMail());
        existingDoctor.setAddress(doctorUpdateRequest.getAddress());
        existingDoctor.setCity(doctorUpdateRequest.getCity());

        List<AvailableDate> updatedAvailableDates = availableDateService.findByIds(doctorUpdateRequest.getAvailableDateIds());
        existingDoctor.setAvailableDateList(updatedAvailableDates);

        // Handle appointment updates if necessary
        // (Assuming you have a method to find appointments by ids if needed)
        // List<Appointment> updatedAppointments = appointmentService.findByIds(doctorUpdateRequest.getAppointmentIds());
        // existingDoctor.setAppointmentList(updatedAppointments);

        Doctor updatedDoctor = doctorService.save(existingDoctor);

        DoctorResponse doctorResponse = new DoctorResponse(
                updatedDoctor.getId(),
                updatedDoctor.getName(),
                updatedDoctor.getPhone(),
                updatedDoctor.getMail(),
                updatedDoctor.getAddress(),
                updatedDoctor.getCity(),
                updatedDoctor.getAvailableDateList().stream().map(
                        AvailableDate::getId
                ).collect(Collectors.toList()),
                updatedDoctor.getAppointmentList().stream().map(
                        Appointment::getId
                ).collect(Collectors.toList())
        );

        return ResultHelper.success(doctorResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> delete(@PathVariable("id") Long id) {
        Doctor deletedDoctor = doctorService.getById(id);

        List<AvailableDate> availableDates = deletedDoctor.getAvailableDateList();

        for (AvailableDate availableDate : availableDates) {
            availableDate.getDoctorList().remove(deletedDoctor);
            availableDateService.save(availableDate);
        }

        this.doctorService.delete(id);

        DoctorResponse doctorResponse = new DoctorResponse(
                id,
                deletedDoctor.getName(),
                deletedDoctor.getPhone(),
                deletedDoctor.getMail(),
                deletedDoctor.getAddress(),
                deletedDoctor.getCity(),
                deletedDoctor.getAvailableDateList().stream().map(
                        AvailableDate::getId
                ).collect(Collectors.toList()),
                deletedDoctor.getAppointmentList().stream().map(
                        Appointment::getId
                ).collect(Collectors.toList())
        );

        return ResultHelper.deleted(doctorResponse);
    }

}
