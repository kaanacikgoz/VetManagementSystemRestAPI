package com.acikgozKaan.VetRestAPI.business.concretes;

import com.acikgozKaan.VetRestAPI.business.abstracts.ICustomerService;
import com.acikgozKaan.VetRestAPI.core.exception.DuplicateEntryException;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.CustomerRepo;
import com.acikgozKaan.VetRestAPI.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerManager implements ICustomerService {

    private final CustomerRepo customerRepo;

    public CustomerManager(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public Customer save(Customer customer) {
        boolean emailExists = customerRepo.findByMail(customer.getMail()).isPresent();
        boolean phoneExists = customerRepo.findByPhone(customer.getPhone()).isPresent();

        if (emailExists || phoneExists) {
            throw new DuplicateEntryException("Duplicate Error: Email or Phone number already exists.");
        } else {
            return customerRepo.save(customer);
        }
    }

    @Override
    public List<Customer> getAll() {
        return customerRepo.findAll();
    }

    @Override
    public Customer getById(Long id) {
        return customerRepo.findById(id).orElseThrow(
                ()->new NotFoundException(Msg.NOT_FOUND)
        );
    }

    @Override
    public Customer update(Customer customer) {
        Customer existingCustomer = this.getById(customer.getId());

        Optional<Customer> existingCustomerByEmail = customerRepo.findByMail(customer.getMail());
        Optional<Customer> existingCustomerByPhone = customerRepo.findByPhone(customer.getPhone());

        boolean emailExists = existingCustomerByEmail.isPresent() && !existingCustomerByEmail.get().getId().equals(customer.getId());
        boolean phoneExists = existingCustomerByPhone.isPresent() && !existingCustomerByPhone.get().getId().equals(customer.getId());

        if (emailExists || phoneExists) {
            throw new DuplicateEntryException("Duplicate Error: Email or Phone number already exists.");
        }

        existingCustomer.setName(customer.getName());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setMail(customer.getMail());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setCity(customer.getCity());

        return customerRepo.save(existingCustomer);
    }

    @Override
    public void delete(Long id) {
        Customer customer = this.getById(id);
        customerRepo.delete(customer);
    }

    @Override
    public List<Customer> findByName(String name) {
        return customerRepo.findByName(name);
    }

}
