package com.acikgozKaan.VetRestAPI.business.concretes;

import com.acikgozKaan.VetRestAPI.business.abstracts.IAvailableDateService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.AvailableDateRepo;
import com.acikgozKaan.VetRestAPI.entity.AvailableDate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailableDateManager implements IAvailableDateService {

    private final AvailableDateRepo availableDateRepo;

    public AvailableDateManager(AvailableDateRepo availableDateRepo) {
        this.availableDateRepo = availableDateRepo;
    }

    @Override
    public void save(AvailableDate availableDate) {
        availableDateRepo.save(availableDate);
    }

    @Override
    public List<AvailableDate> getAll() {
        return availableDateRepo.findAll();
    }

    @Override
    public AvailableDate getById(Long id) {
        return availableDateRepo.findById(id).orElseThrow(
                ()->new NotFoundException(Msg.NOT_FOUND)
        );
    }

    @Override
    public AvailableDate update(AvailableDate availableDate) {
        this.getById(availableDate.getId());
        return this.availableDateRepo.save(availableDate);
    }

    @Override
    public void delete(Long id) {
        AvailableDate availableDate = this.getById(id);
        availableDateRepo.delete(availableDate);
    }

}
