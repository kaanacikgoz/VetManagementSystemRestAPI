package com.acikgozKaan.VetRestAPI.dao;

import com.acikgozKaan.VetRestAPI.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CustomerRepoTest {

    @Autowired
    private CustomerRepo customerRepo;

    @Test
    @DisplayName("Customer found with his/her mail!")
    void testFindByMail() {
        // given
        Customer customer = new Customer();
        customer.setName("Customer1");
        customer.setPhone("123123111");
        customer.setMail("customer1@mail.com");
        customer.setAddress("Address1");
        customer.setCity("City");

        customerRepo.save(customer);

        // when
        Optional<Customer> foundCustomers = customerRepo.findByMail(customer.getMail());

        // then
        assertThat(foundCustomers).isPresent();

        foundCustomers.ifPresent(customer1 -> assertThat(customer1.getMail()).isEqualTo(customer.getMail()));
    }

    @Test
    @DisplayName("Customer found with his/her phone!")
    void testFindByPhone() {
        // given
        Customer customer = new Customer();
        customer.setName("Customer2");
        customer.setPhone("123123222");
        customer.setMail("customer2@mail.com");
        customer.setAddress("Address2");
        customer.setCity("City2");

        customerRepo.save(customer);

        // when
        Optional<Customer> foundCustomers = customerRepo.findByPhone(customer.getPhone());

        // then
        assertThat(foundCustomers).isPresent();

        foundCustomers.ifPresent(customer2 -> assertThat(customer2.getPhone()).isEqualTo(customer.getPhone()));
    }

    @Test
    @DisplayName("Customer found with his/her name!")
    void testFindByName() {
        // given
        Customer customer = new Customer();
        customer.setName("Customer3");
        customer.setPhone("123123333");
        customer.setMail("customer3@mail.com");
        customer.setAddress("Address3");
        customer.setCity("City3");

        customerRepo.save(customer);

        // when
        List<Customer> foundCustomers = customerRepo.findByName(customer.getName());

        // then
        assertThat(foundCustomers.get(0).getName()).isEqualTo(customer.getName());
    }

}
