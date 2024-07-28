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
public class AvailableDateResponse {

    private Long id;

    private LocalDate availableDate;

    private List<Long> doctorIds;

}
