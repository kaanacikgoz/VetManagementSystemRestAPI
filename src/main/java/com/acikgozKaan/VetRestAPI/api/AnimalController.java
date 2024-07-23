package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAnimalService;
import com.acikgozKaan.VetRestAPI.business.abstracts.ICustomerService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.modelMapper.IModelMapperService;
import com.acikgozKaan.VetRestAPI.core.result.ResultData;
import com.acikgozKaan.VetRestAPI.core.utilies.ResultHelper;
import com.acikgozKaan.VetRestAPI.dto.request.animal.AnimalSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.request.animal.AnimalUpdateRequest;
import com.acikgozKaan.VetRestAPI.dto.response.AnimalResponse;
import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.acikgozKaan.VetRestAPI.entity.Customer;
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
    private final IModelMapperService modelMapper;

    public AnimalController(IAnimalService animalService, ICustomerService customerService, IModelMapperService modelMapper) {
        this.animalService = animalService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ResultData<AnimalResponse>> save(@Valid @RequestBody AnimalSaveRequest animalSaveRequest) {
        try {
            Animal saveAnimal = modelMapper.forRequest().map(animalSaveRequest, Animal.class);

            if (animalSaveRequest.getCustomerId() != null) {
                Customer customer = customerService.getById(animalSaveRequest.getCustomerId());
                saveAnimal.setCustomer(customer);
            } else {
                saveAnimal.setCustomer(null);
            }

            Animal savedAnimal = animalService.save(saveAnimal);

            AnimalResponse animalResponse = modelMapper.forResponse().map(savedAnimal, AnimalResponse.class);
            animalResponse.setCustomerId(savedAnimal.getCustomer() != null ? savedAnimal.getCustomer().getId() : null);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResultHelper.created(animalResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResultHelper.errorData("Not Found Customer"));
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAll() {
        List<Animal> animals = animalService.getAll();
        List<AnimalResponse> animalResponses = animals.stream().map(
                animal -> modelMapper.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(animalResponses);
    }

    @GetMapping("/search")
    public ResponseEntity<ResultData<List<AnimalResponse>>> findByName(@RequestParam String name) {
        List<Animal> animals = animalService.findByName(name);
        List<AnimalResponse> animalResponses = animals.stream()
                .map(animal -> modelMapper.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
        if (animals.isEmpty()) {
            ResultData<List<AnimalResponse>> result = ResultHelper.notFound(animalResponses);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else {
            ResultData<List<AnimalResponse>> result = ResultHelper.success(animalResponses);
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/owner/{customerId}")
    public ResponseEntity<ResultData<List<AnimalResponse>>> findByCustomerId(@PathVariable Long customerId) {
        List<Animal> animals = animalService.findByCustomerId(customerId);
        List<AnimalResponse> animalResponses = animals.stream()
                .map(animal -> modelMapper.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());

        if (animals.isEmpty()) {
            ResultData<List<AnimalResponse>> result = ResultHelper.notFound(animalResponses);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else {
            ResultData<List<AnimalResponse>> result = ResultHelper.success(animalResponses);
            return ResponseEntity.ok(result);
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResultData<AnimalResponse>> update(@PathVariable("id") Long id, @Valid @RequestBody AnimalUpdateRequest animalUpdateRequest) {
        try {
            Animal updatedAnimal = animalService.update(id, animalUpdateRequest);

            AnimalResponse animalResponse = modelMapper.forResponse().map(updatedAnimal, AnimalResponse.class);
            animalResponse.setCustomerId(updatedAnimal.getCustomer() != null ? updatedAnimal.getCustomer().getId() : null);

            return ResponseEntity.ok(ResultHelper.success(animalResponse));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResultHelper.notFoundAnimal(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultHelper.errorData(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> delete(@PathVariable("id") Long id) {
        Animal deletedAnimal = animalService.getById(id);
        this.animalService.delete(id);
        AnimalResponse animalResponse = modelMapper.forResponse().map(deletedAnimal, AnimalResponse.class);
        return ResultHelper.deleted(animalResponse);
    }

}
