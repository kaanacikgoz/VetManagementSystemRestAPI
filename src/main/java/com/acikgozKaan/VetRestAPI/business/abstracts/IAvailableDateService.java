package com.acikgozKaan.VetRestAPI.business.abstracts;

import com.acikgozKaan.VetRestAPI.dto.request.availableDate.AvailableDateSaveRequest;
import com.acikgozKaan.VetRestAPI.entity.AvailableDate;

import java.util.List;

public interface IAvailableDateService {

    AvailableDate save(AvailableDate availableDate);

    List<AvailableDate> getAll();

    AvailableDate getById(Long id);

    AvailableDate update(AvailableDate availableDate);

    void delete(Long id);

    List<AvailableDate> findByIds(List<Long> ids);

}
