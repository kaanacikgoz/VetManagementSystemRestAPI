package com.acikgozKaan.VetRestAPI.business.abstracts;

import com.acikgozKaan.VetRestAPI.entity.Customer;

import java.util.List;

public interface ICustomerService {

    void save(Customer customer);

    List<Customer> getAll();

    Customer getById(Long id);

    Customer update(Customer customer);

    void delete(Long id);

}
