package com.acikgozKaan.VetRestAPI.dto.response;

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
public class VaccineResponse {

    private Long id;

    private String name;

    private String code;

    private LocalDate protectionStartDate;

    private LocalDate protectionFinishDate;

    private List<Long> animalIds;

}
