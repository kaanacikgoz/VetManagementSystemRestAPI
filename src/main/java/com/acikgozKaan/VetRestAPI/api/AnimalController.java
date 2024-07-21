package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAnimalService;
import com.acikgozKaan.VetRestAPI.core.modelMapper.IModelMapperService;
import com.acikgozKaan.VetRestAPI.core.result.ResultData;
import com.acikgozKaan.VetRestAPI.core.utilies.ResultHelper;
import com.acikgozKaan.VetRestAPI.dto.request.animal.AnimalSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.request.animal.AnimalUpdateRequest;
import com.acikgozKaan.VetRestAPI.dto.response.AnimalResponse;
import com.acikgozKaan.VetRestAPI.entity.Animal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/animals")
public class AnimalController {

    private final IAnimalService animalService;
    private final IModelMapperService modelMapper;

    public AnimalController(IAnimalService animalService, IModelMapperService modelMapper) {
        this.animalService = animalService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AnimalResponse> saveCategory(@Valid @RequestBody AnimalSaveRequest animalSaveRequest) {
        Animal saveAnimal = modelMapper.forRequest().map(animalSaveRequest, Animal.class);
        this.animalService.save(saveAnimal);

        AnimalResponse animalResponse = modelMapper.forResponse().map(saveAnimal, AnimalResponse.class);
        return ResultHelper.created(animalResponse);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Animal> getAll() {
        return animalService.getAll();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> updateCategory(@Valid @RequestBody AnimalUpdateRequest animalUpdateRequest) {
        Animal updateAnimal = modelMapper.forRequest().map(animalUpdateRequest, Animal.class);
        animalService.update(updateAnimal);

        AnimalResponse animalResponse = modelMapper.forResponse().map(updateAnimal, AnimalResponse.class);
        return ResultHelper.success(animalResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<Animal> deleteCategory(@PathVariable("id") Long id) {
        Animal deletedAnimal = animalService.getById(id);
        this.animalService.delete(id);
        return ResultHelper.deleted(deletedAnimal);
    }

}
