package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAnimalService;
import com.acikgozKaan.VetRestAPI.business.abstracts.ICustomerService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.result.ResultData;
import com.acikgozKaan.VetRestAPI.core.utilies.ResultHelper;
import com.acikgozKaan.VetRestAPI.dto.request.animal.AnimalSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.request.animal.AnimalUpdateRequest;
import com.acikgozKaan.VetRestAPI.dto.response.AnimalResponse;
import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Customer;
import com.acikgozKaan.VetRestAPI.entity.Vaccine;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/animals")
public class AnimalController {

    private final IAnimalService animalService;
    private final ICustomerService customerService;

    public AnimalController(IAnimalService animalService, ICustomerService customerService) {
        this.animalService = animalService;
        this.customerService = customerService;
    }

    //Evaluation Form 12
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AnimalResponse> save(@Valid @RequestBody AnimalSaveRequest animalSaveRequest) {

        Customer customer = customerService.getById(animalSaveRequest.getCustomerId());

        Animal animal = new Animal(
                animalSaveRequest.getName(),
                animalSaveRequest.getSpecies(),
                animalSaveRequest.getBreed(),
                animalSaveRequest.getGender(),
                animalSaveRequest.getColour(),
                animalSaveRequest.getDateOfBirth(),
                customer
        );

        Animal savedAnimal = animalService.save(animal);

        AnimalResponse animalResponse = new AnimalResponse(
                savedAnimal.getId(),
                savedAnimal.getName(),
                savedAnimal.getSpecies(),
                savedAnimal.getBreed(),
                savedAnimal.getGender(),
                savedAnimal.getColour(),
                savedAnimal.getDateOfBirth(),
                savedAnimal.getCustomer().getId()
        );

        return ResultHelper.created(animalResponse);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAll() {
        List<Animal> animals = animalService.getAll();

        List<AnimalResponse> animalResponses = animals.stream().map(
                animal -> {
                    List<Long> vaccineIds = animal.getVaccineList().stream()
                            .map(Vaccine::getId)
                            .collect(Collectors.toList());

                    return new AnimalResponse(
                            animal.getId(),
                            animal.getName(),
                            animal.getSpecies(),
                            animal.getBreed(),
                            animal.getGender(),
                            animal.getColour(),
                            animal.getDateOfBirth(),
                            animal.getCustomer().getId(),
                            vaccineIds
                    );
                }).collect(Collectors.toList());

        return ResultHelper.success(animalResponses);
    }

    //Evaluation Form 13
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> findByName(@RequestParam String name) {
        List<Animal> animals = animalService.findByName(name);

                List<AnimalResponse> animalResponses = animals.stream()
                .map(animal -> new AnimalResponse(
                        animal.getId(),
                        animal.getName(),
                        animal.getSpecies(),
                        animal.getBreed(),
                        animal.getGender(),
                        animal.getColour(),
                        animal.getDateOfBirth(),
                        animal.getCustomer().getId(),
                        animal.getVaccineList().stream().map(
                                Vaccine::getId
                        ).collect(Collectors.toList())
                )).collect(Collectors.toList());

        return ResultHelper.success(animalResponses);
    }

    //Evaluation Form 14
    @GetMapping("/owner/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> findByCustomerId(@PathVariable Long customerId) {
        List<Animal> animals = animalService.findByCustomerId(customerId);
        List<AnimalResponse> animalResponses = animals.stream()
                .map(animal -> new AnimalResponse(
                        animal.getId(),
                        animal.getName(),
                        animal.getSpecies(),
                        animal.getBreed(),
                        animal.getGender(),
                        animal.getColour(),
                        animal.getDateOfBirth(),
                        animal.getCustomer().getId(),
                        animal.getVaccineList().stream().map(
                                Vaccine::getId
                        ).collect(Collectors.toList()))
                ).collect(Collectors.toList());

        return ResultHelper.success(animalResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultData<AnimalResponse>> update(@PathVariable("id") Long id, @Valid @RequestBody AnimalUpdateRequest animalUpdateRequest) {
        try {
            Animal updatedAnimal = animalService.update(id, animalUpdateRequest);

            AnimalResponse animalResponse = new AnimalResponse(
                    updatedAnimal.getId(),
                    updatedAnimal.getName(),
                    updatedAnimal.getSpecies(),
                    updatedAnimal.getBreed(),
                    updatedAnimal.getGender(),
                    updatedAnimal.getColour(),
                    updatedAnimal.getDateOfBirth(),
                    updatedAnimal.getCustomer().getId()
            );

            return ResponseEntity.ok(ResultHelper.success(animalResponse));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResultHelper.notFoundAnimal(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultHelper.internalServerError(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> delete(@PathVariable("id") Long id) {
        Animal deletedAnimal = animalService.getById(id);
        this.animalService.delete(id);

        AnimalResponse animalResponse = new AnimalResponse(
                deletedAnimal.getId(),
                deletedAnimal.getName(),
                deletedAnimal.getSpecies(),
                deletedAnimal.getBreed(),
                deletedAnimal.getGender(),
                deletedAnimal.getColour(),
                deletedAnimal.getDateOfBirth(),
                deletedAnimal.getCustomer().getId(),
                deletedAnimal.getVaccineList().stream().map(
                        Vaccine::getId
                ).collect(Collectors.toList())
        );

        return ResultHelper.deleted(animalResponse);
    }

}
