package com.acikgozKaan.VetRestAPI.business;

import com.acikgozKaan.VetRestAPI.business.concretes.CustomerManager;
import com.acikgozKaan.VetRestAPI.dao.CustomerRepo;
import com.acikgozKaan.VetRestAPI.entity.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CustomerManagerTest {

    @InjectMocks
    private CustomerManager customerManager;

    @Mock
    private CustomerRepo customerRepo;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        try {
            if (autoCloseable!=null) {
                autoCloseable.close();
            }
        } catch (Exception e) {
            System.out.println("Resource cannot be closed!");
        }
    }

    @Test
    void getAll_CustomersExist_ReturnsCustomerList() {
        // given
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Customer1");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Customer2");

        List<Customer> customers = Arrays.asList(customer1, customer2);

        when(customerRepo.findAll()).thenReturn(customers);

        // when
        List<Customer> result = customerManager.getAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Customer::getName).containsExactly("Customer1", "Customer2");

        verify(customerRepo, times(1)).findAll();
    }


}
