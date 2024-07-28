package com.acikgozKaan.VetRestAPI.dto.request.vaccine;

import com.acikgozKaan.VetRestAPI.entity.Animal;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineSaveRequest {

    @NotBlank(message = "Customer name cannot be null or empty")
    private String name;

    @NotBlank(message = "Customer name cannot be null or empty")
    private String code;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate protectionStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate protectionFinishDate;

    private List<Long> animalIds;

}
