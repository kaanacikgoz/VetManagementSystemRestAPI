package com.acikgozKaan.VetRestAPI.business.abstracts;

import com.acikgozKaan.VetRestAPI.entity.Customer;

import java.util.List;

public interface ICustomerService {

    Customer save(Customer customer);

    List<Customer> getAll();

    Customer getById(Long id);

    Customer update(Customer customer);

    void delete(Long id);

    List<Customer> findByName(String name);

}
