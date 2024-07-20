package com.acikgozKaan.VetRestAPI.business.abstracts;

import com.acikgozKaan.VetRestAPI.entity.AvailableDate;

import java.util.List;

public interface IAvailableDateService {

    void save(AvailableDate availableDate);

    List<AvailableDate> getAll();

    AvailableDate getById(Long id);

    AvailableDate update(AvailableDate availableDate);

    void delete(Long id);

}
