package com.acikgozKaan.VetRestAPI.business;

import com.acikgozKaan.VetRestAPI.business.concretes.CustomerManager;
import com.acikgozKaan.VetRestAPI.core.exception.DuplicateEntryException;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dao.CustomerRepo;
import com.acikgozKaan.VetRestAPI.entity.Customer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // With this annotation no need for setUp and tearDown!
public class CustomerManagerTest {

    @InjectMocks
    private CustomerManager customerManager;

    @Mock
    private CustomerRepo customerRepo;

    @Nested
    class saveTest {

        @Test
        void save_CustomerIfNotDuplicate_Success() {
            // given
            Customer customer = Customer.builder()
                    .id(1L)
                    .name("Customer")
                    .build();

            when(customerRepo.save(customer)).thenReturn(customer);

            // when
            Customer savedCustomer = customerManager.save(customer);

            // then
            assertThat(savedCustomer).isNotNull();
        }

        @Test
        void save_CustomerIfDuplicateEmail_ThrowsDuplicateEntryException() {
            // given
            Customer customer = Customer.builder()
                    .id(1L)
                    .name("Customer")
                    .mail("customer@mail.com")
                    .build();

            Customer customer2 = Customer.builder()
                    .id(2L)
                    .name("Customer2")
                    .mail("customer@mail.com")
                    .build();

            when(customerRepo.findByMail(customer2.getMail())).thenReturn(Optional.of(customer));
            when(customerRepo.findByPhone(customer2.getPhone())).thenReturn(Optional.empty());

            // when
            Exception thrown = assertThrows(DuplicateEntryException.class, ()->customerManager.save(customer2));

            // then
            verify(customerRepo, never()).save(customer2);
            assertThat(thrown).extracting(Exception::getClass)
                    .isEqualTo(DuplicateEntryException.class);
            assertThat(thrown).extracting(Exception::getMessage)
                    .isEqualTo("Duplicate Error: Email or Phone number already exists.");
        }

        @Test
        void save_CustomerIfDuplicatePhone_ThrowsDuplicateEntryException() {
            // given
            Customer customer = Customer.builder()
                    .id(1L)
                    .name("Customer")
                    .phone("123123123")
                    .build();

            Customer customer2 = Customer.builder()
                    .id(2L)
                    .name("Customer2")
                    .phone("123123123")
                    .build();

            when(customerRepo.findByMail(customer2.getMail())).thenReturn(Optional.empty());
            when(customerRepo.findByPhone(customer2.getPhone())).thenReturn(Optional.of(customer));

            // when
            Exception thrown = assertThrows(DuplicateEntryException.class, ()->customerManager.save(customer2));

            // then
            verify(customerRepo,never()).save(customer2);
            assertThat(thrown).extracting(Exception::getClass)
                    .isEqualTo(DuplicateEntryException.class);
            assertThat(thrown).extracting(Exception::getMessage)
                    .isEqualTo("Duplicate Error: Email or Phone number already exists.");
        }
    }


    @Test
    void getAll_Customer_Success() {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .name("Customer")
                .build();

        Customer customer2 = Customer.builder()
                .id(2L)
                .name("Customer2")
                .build();

        when(customerRepo.findAll()).thenReturn(List.of(customer, customer2));

        // when
        List<Customer> foundCustomers = customerManager.getAll();

        // then
        assertThat(foundCustomers).isNotEmpty();
        assertThat(foundCustomers).hasSize(2);
        assertThat(foundCustomers).extracting(Customer::getName)
                .containsExactlyInAnyOrder("Customer","Customer2");
    }

    @Nested
    public class getByIdTest {

        @Test
        void getById_Customer_Success() {
            // given
            Customer customer = Customer.builder()
                    .id(1L)
                    .name("Customer")
                    .build();

            when(customerRepo.findById(customer.getId())).thenReturn(Optional.of(customer));

            // when
            Customer foundCustomer = customerManager.getById(customer.getId());

            // then
            assertThat(foundCustomer).isNotNull();
            assertThat(foundCustomer).extracting(Customer::getName)
                    .isEqualTo("Customer");
        }

        @Test
        void getById_Customer_ThrowsNotFoundException() {
            // given
            Customer customer = Customer.builder()
                    .id(1L)
                    .name("Customer")
                    .build();

            when(customerRepo.findById(customer.getId())).thenReturn(Optional.empty());

            // when
            Exception thrown = assertThrows(NotFoundException.class, ()->customerManager.getById(customer.getId()));

            // then
            verify(customerRepo, never()).save(customer);
            assertThat(thrown).isNotNull();
            assertThat(thrown).extracting(Exception::getMessage)
                    .isEqualTo(Msg.NOT_FOUND);
        }
    }



}
