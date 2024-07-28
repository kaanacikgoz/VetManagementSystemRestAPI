package com.acikgozKaan.VetRestAPI.dto.request.availableDate;


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
public class AvailableDateUpdateRequest {

    private LocalDate availableDate;

    private List<Long> doctorIds;

}
