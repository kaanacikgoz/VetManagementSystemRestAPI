package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAnimalService;
import com.acikgozKaan.VetRestAPI.business.abstracts.IAppointmentService;
import com.acikgozKaan.VetRestAPI.business.abstracts.ICustomerService;
import com.acikgozKaan.VetRestAPI.business.abstracts.IDoctorService;
import com.acikgozKaan.VetRestAPI.core.exception.AppointmentException;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.result.ResultData;
import com.acikgozKaan.VetRestAPI.core.utilies.ResultHelper;
import com.acikgozKaan.VetRestAPI.dto.request.appointment.AppointmentSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.request.appointment.AppointmentUpdateRequest;
import com.acikgozKaan.VetRestAPI.dto.response.AppointmentResponse;
import com.acikgozKaan.VetRestAPI.entity.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/appointments")
public class AppointmentController {

    private final IAppointmentService appointmentService;
    private final IDoctorService doctorService;
    private final IAnimalService animalService;

    public AppointmentController(IAppointmentService appointmentService, IDoctorService doctorService, IAnimalService animalService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.animalService = animalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save(@RequestBody AppointmentSaveRequest appointmentSaveRequest) {
        // Doktor kontrolü
        Doctor doctor = doctorService.getById(appointmentSaveRequest.getDoctorId());
        if (doctor == null) {
            throw new AppointmentException("Doctor not found.");
        }

        // Randevu saati kontrolü
        LocalDateTime appointmentDateTime = appointmentSaveRequest.getAppointmentDate();
        if (appointmentDateTime.getMinute() != 0 || appointmentDateTime.getSecond() != 0) {
            throw new AppointmentException("Appointments can only be scheduled on the hour.");
        }

        // Doktorun müsait olduğu tarih kontrolü
        boolean isAvailableDate = doctor.getAvailableDateList().stream()
                .anyMatch(availableDate -> availableDate.getAvailableDate().isEqual(appointmentDateTime.toLocalDate()));
        if (!isAvailableDate) {
            throw new AppointmentException("No available date found for the doctor.");
        }

        // Seçilen zaman diliminde başka bir randevu olup olmadığını kontrol et
        boolean isSlotTaken = appointmentService.existsByDoctorAndAppointmentDate(doctor, appointmentDateTime);
        if (isSlotTaken) {
            throw new AppointmentException("The selected time slot is already taken.");
        }

        // Hayvan ve müşteri bilgilerini al
        Animal animal = animalService.getById(appointmentSaveRequest.getAnimalId());
        if (animal == null) {
            throw new AppointmentException("Animal not found.");
        }
        Customer customer = animal.getCustomer();

        // Randevu oluştur
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(appointmentDateTime);
        appointment.setCustomer(customer);
        appointment.setAnimal(animal);
        appointment.setDoctor(doctor);

        // Randevuyu kaydet
        Appointment savedAppointment = appointmentService.save(appointment);

        // Randevu yanıtını oluştur
        AppointmentResponse appointmentResponse = new AppointmentResponse(
                savedAppointment.getId(),
                savedAppointment.getAppointmentDate(),
                savedAppointment.getCustomer().getId(),
                savedAppointment.getAnimal().getId(),
                savedAppointment.getDoctor().getId()
        );

        return ResultHelper.created(appointmentResponse);
    }


    @GetMapping
    public ResultData<List<AppointmentResponse>> getAll() {
        List<Appointment> appointments = appointmentService.getAll();

        List<AppointmentResponse> appointmentResponses = appointments.stream().map(
                appointment -> new AppointmentResponse(
                appointment.getId(),
                appointment.getAppointmentDate(),
                appointment.getCustomer().getId(),
                appointment.getAnimal().getId(),
                appointment.getDoctor().getId()
        )).collect(Collectors.toList());

        return ResultHelper.success(appointmentResponses);
    }

    @GetMapping("/filter/doctor-id")
    public ResultData<List<AppointmentResponse>> filterAppointmentsByDoctorId(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd") LocalDate endDate) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentService.findByDoctorAndAppointmentDateBetween(doctorId, startDateTime, endDateTime);

        List<AppointmentResponse> appointmentResponses = appointments.stream().map(appointment -> new AppointmentResponse(
                appointment.getId(),
                appointment.getAppointmentDate(),
                appointment.getCustomer().getId(),
                appointment.getAnimal().getId(),
                appointment.getDoctor().getId()
        )).collect(Collectors.toList());

        return ResultHelper.success(appointmentResponses);
    }

    @GetMapping("/filter/animal-id")
    public ResultData<List<AppointmentResponse>> filterAppointmentsByAnimalId(
            @RequestParam("animalId") Long animalId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd") LocalDate endDate) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentService.getAppointmentsByAnimalAndDateRange(animalId, startDateTime, endDateTime);

        List<AppointmentResponse> response = appointments.stream()
                .map(appointment -> new AppointmentResponse(
                        appointment.getId(),
                        appointment.getAppointmentDate(),
                        appointment.getCustomer().getId(),
                        appointment.getAnimal().getId(),
                        appointment.getDoctor().getId()
                ))
                .collect(Collectors.toList());

        return ResultHelper.success(response);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> update(@PathVariable("id") Long id, @Valid @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) {

        Animal animal = animalService.getById(appointmentUpdateRequest.getAnimalId());
        Customer customer = animal.getCustomer();
        Doctor doctor = doctorService.getById(appointmentUpdateRequest.getDoctorId());

        Appointment updateAppointment = new Appointment(
                id,
                appointmentUpdateRequest.getAppointmentDate(),
                customer,
                animal,
                doctor
        );

        Appointment updatedAppointment = appointmentService.update(updateAppointment);

        AppointmentResponse appointmentResponse = new AppointmentResponse(
                id,
                updatedAppointment.getAppointmentDate(),
                updatedAppointment.getCustomer().getId(),
                updatedAppointment.getAnimal().getId(),
                updatedAppointment.getDoctor().getId()
        );

        return ResultHelper.success(appointmentResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> delete(@PathVariable("id") Long id) {
        Appointment deletedAppointment = appointmentService.getById(id);
        this.appointmentService.delete(id);

        AppointmentResponse appointmentResponse = new AppointmentResponse(
                deletedAppointment.getId(),
                deletedAppointment.getAppointmentDate(),
                deletedAppointment.getCustomer().getId(),
                deletedAppointment.getAnimal().getId(),
                deletedAppointment.getDoctor().getId()
        );

        return ResultHelper.deleted(appointmentResponse);
    }




}
