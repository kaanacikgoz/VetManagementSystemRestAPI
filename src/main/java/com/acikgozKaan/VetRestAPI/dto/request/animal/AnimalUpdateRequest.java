package com.acikgozKaan.VetRestAPI.dto.request.animal;

import com.acikgozKaan.VetRestAPI.entity.Animal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalUpdateRequest {

    @NotBlank(message = "Animal name can not be null or empty")
    private String name;

    @NotBlank(message = "Animal species can not be null or empty")
    private String species;

    @NotBlank(message = "Animal breed can not be null or empty")
    private String breed;

    @NotNull(message = "Animal gender can not be null or empty")
    private Animal.Gender gender;

    @NotBlank(message = "Animal colour can not be null or empty")
    private String colour;

    @NotNull(message = "Animal dateOfBirth can not be null or empty")
    private LocalDate dateOfBirth;

    @NotNull(message = "Animal customerId can not be null or empty")
    private Long customerId;

}
