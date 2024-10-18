package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.ICustomerService;
import com.acikgozKaan.VetRestAPI.core.exception.NotFoundException;
import com.acikgozKaan.VetRestAPI.core.modelMapper.IModelMapperService;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dto.request.customer.CustomerSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.request.customer.CustomerUpdateRequest;
import com.acikgozKaan.VetRestAPI.dto.response.CustomerResponse;
import com.acikgozKaan.VetRestAPI.entity.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICustomerService customerService;

    @MockBean
    private IModelMapperService modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void save_Customer_Success() throws Exception {
        // given
        CustomerSaveRequest request = new CustomerSaveRequest();
        request.setName("Customer");
        request.setMail("customer@mail.com");
        request.setPhone("123123123");
        request.setAddress("Address");
        request.setCity("City");

        Customer savedCustomer = Customer.builder()
                .id(1L)
                .name("Customer")
                .mail("customer@mail.com")
                .phone("123123123")
                .address("Address")
                .city("City")
                .build();

        CustomerResponse customerResponse = new CustomerResponse(
                savedCustomer.getId(),
                savedCustomer.getName(),
                savedCustomer.getPhone(),
                savedCustomer.getMail(),
                savedCustomer.getAddress(),
                savedCustomer.getCity()
        );

        // when

        // Create a mock ModelMapper instance
        ModelMapper modelMapperMock = Mockito.mock(ModelMapper.class);

        // Set up the mock ModelMapper service to return the mock instance
        when(modelMapper.forRequest()).thenReturn(modelMapperMock);
        when(modelMapper.forResponse()).thenReturn(modelMapperMock);

        // Stubbing map method calls with correct usage of argument matchers
        when(modelMapperMock.map(any(CustomerSaveRequest.class), eq(Customer.class))).thenReturn(savedCustomer);
        when(customerService.save(any(Customer.class))).thenReturn(savedCustomer);
        when(modelMapperMock.map(any(Customer.class), eq(CustomerResponse.class))).thenReturn(customerResponse);

        // then
        mockMvc.perform(post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated(),
                jsonPath("$.status").value(true),
                jsonPath("$.message").value(Msg.CREATED),
                jsonPath("$.data.id").value(1L),
                jsonPath("$.data.name").value("Customer"),
                jsonPath("$.data.mail").value("customer@mail.com"),
                jsonPath("$.data.phone").value("123123123"),
                jsonPath("$.data.address").value("Address"),
                jsonPath("$.data.city").value("City")
        );
    }

    @Test
    void getAll_Customers_Success() throws Exception {
        // given
        Customer customer = Customer.builder()
                .id(1L)
                .name("Customer")
                .build();

        Customer customer2 = Customer.builder()
                .id(2L)
                .name("Customer2")
                .build();

        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setId(customer.getId());
        customerResponse.setName(customer.getName());

        CustomerResponse customerResponse2 = new CustomerResponse();
        customerResponse2.setId(customer2.getId());
        customerResponse2.setName(customer2.getName());

        // when
        ModelMapper modelMapperMock = mock(ModelMapper.class);

        when(modelMapper.forRequest()).thenReturn(modelMapperMock);
        when(modelMapper.forResponse()).thenReturn(modelMapperMock);

        when(modelMapperMock.map(eq(customer), eq(CustomerResponse.class))).thenReturn(customerResponse);
        when(modelMapperMock.map(eq(customer2), eq(CustomerResponse.class))).thenReturn(customerResponse2);

        when(customerService.getAll()).thenReturn(List.of(customer, customer2));

        // then
        mockMvc.perform(get("/v1/customers"))
                .andExpectAll(
                        jsonPath("$.status").value(true),
                        jsonPath("$.message").value(Msg.OK),
                        jsonPath("$.code").value(200),
                        jsonPath("$.data[0].id").value(1L),
                        jsonPath("$.data[0].name").value("Customer"),
                        jsonPath("$.data[1].id").value(2L),
                        jsonPath("$.data[1].name").value("Customer2")
                );
    }

    @Nested
    final class findByNameTest {

        @Test
        void findByName_Customer_Success() throws Exception {
            // given
            Customer customer = Customer.builder()
                    .id(1L)
                    .name("Customer")
                    .build();

            CustomerResponse customerResponse = new CustomerResponse();
            customerResponse.setId(customer.getId());
            customerResponse.setName(customer.getName());

            // when
            ModelMapper modelMapperMock = mock(ModelMapper.class);

            when(modelMapper.forResponse()).thenReturn(modelMapperMock);

            when(customerService.findByName("Customer")).thenReturn(List.of(customer));
            when(modelMapperMock.map(customer, CustomerResponse.class)).thenReturn(customerResponse);

            // then
            mockMvc.perform(get("/v1/customers/search")
                            .param("name", "Customer"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data[0].id").value(1L),
                            jsonPath("$.data[0].name").value("Customer")
                    );
        }

        @Test
        void findByName_Customers_Success() throws Exception {
            // given
            Customer customer = Customer.builder()
                    .id(1L)
                    .name("Customer")
                    .build();

            Customer customer2 = Customer.builder()
                    .id(2L)
                    .name("Customer")
                    .build();

            CustomerResponse customerResponse = new CustomerResponse();
            customerResponse.setId(customer.getId());
            customerResponse.setName(customer.getName());

            CustomerResponse customerResponse2 = new CustomerResponse();
            customerResponse2.setId(customer2.getId());
            customerResponse2.setName(customer2.getName());

            // when
            ModelMapper modelMapperMock = mock(ModelMapper.class);

            when(modelMapper.forResponse()).thenReturn(modelMapperMock);

            when(customerService.findByName("Customer")).thenReturn(List.of(customer, customer2));

            when(modelMapperMock.map(customer, CustomerResponse.class)).thenReturn(customerResponse);
            when(modelMapperMock.map(customer2, CustomerResponse.class)).thenReturn(customerResponse2);

            // then
            mockMvc.perform(get("/v1/customers/search")
                            .param("name", "Customer"))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data[0].id").value(1L),
                            jsonPath("$.data[0].name").value("Customer"),
                            jsonPath("$.data[1].id").value(2L),
                            jsonPath("$.data[1].name").value("Customer")
                    );
        }

        @Test
        void findByName_NonExistingCustomer_ThrowsNotFoundException() throws Exception {
            // given
            String nonExistingCustomerName = "NonExist";

            // when
            when(customerService.findByName(nonExistingCustomerName)).thenReturn(List.of());

            // then
            mockMvc.perform(get("/v1/customers/search")
                    .param("name", nonExistingCustomerName))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(false),
                            jsonPath("$.message").value(Msg.NOT_FOUND),
                            jsonPath("$.code").value(404)
                    );
        }

    }

    @Nested
    final class updateTest {

        @Test
        void update_Customer_Success() throws Exception {
            // given
            CustomerUpdateRequest updateRequest = new CustomerUpdateRequest();
            updateRequest.setName("UpdatedCustomer");

            Customer customer = Customer.builder()
                    .id(1L)
                    .name("Customer")
                    .build();

            Customer updatedCustomer = Customer.builder()
                    .id(1L)
                    .name("UpdatedCustomer")
                    .build();

            CustomerResponse customerResponse = new CustomerResponse();
            customerResponse.setId(updatedCustomer.getId());
            customerResponse.setName(updatedCustomer.getName());

            // when
            ModelMapper modelMapperMock = mock(ModelMapper.class);

            when(modelMapper.forRequest()).thenReturn(modelMapperMock);
            when(modelMapper.forResponse()).thenReturn(modelMapperMock);

            when(modelMapper.forRequest().map(any(CustomerUpdateRequest.class), eq(Customer.class))).thenReturn(customer);
            when(customerService.update(customer)).thenReturn(updatedCustomer);
            when(modelMapper.forResponse().map(updatedCustomer, CustomerResponse.class)).thenReturn(customerResponse);

            // then
            mockMvc.perform(put("/v1/customers/{id}", customer.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.OK),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data.id").value(1L),
                            jsonPath("$.data.name").value("UpdatedCustomer")
                    );
        }

        @Test
        void update_NonExistingCustomer_ThrowsNotFoundException() throws Exception {
            // given
            CustomerUpdateRequest updateRequest = new CustomerUpdateRequest();
            updateRequest.setName("UpdatedCustomer");

            Customer customer = Customer.builder()
                    .id(10L)
                    .name("UpdatedCustomer")
                    .build();

            // when
            ModelMapper modelMapperMock = mock(ModelMapper.class);

            when(modelMapper.forRequest()).thenReturn(modelMapperMock);
            when(modelMapper.forResponse()).thenReturn(modelMapperMock);

            when(modelMapperMock.map(any(CustomerUpdateRequest.class), eq(Customer.class))).thenReturn(customer);
            when(customerService.update(customer)).thenThrow(new NotFoundException(Msg.NOT_FOUND));

            // then
            mockMvc.perform(put("/v1/customers/{id}", 10L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(false),
                            jsonPath("$.message").value(Msg.NOT_FOUND),
                            jsonPath("$.code").value(404)
                    );
        }
    }

    @Nested
    final class deleteTest {

        @Test
        void delete_Customer_Success() throws Exception {
            // given
            Customer willDeleteCustomer = Customer.builder()
                    .id(1L)
                    .name("DeletedCustomer")
                    .build();

            CustomerResponse customerResponse = new CustomerResponse();
            customerResponse.setId(willDeleteCustomer.getId());
            customerResponse.setName(willDeleteCustomer.getName());

            // when
            ModelMapper modelMapperMock = mock(ModelMapper.class);

            when(modelMapper.forResponse()).thenReturn(modelMapperMock);

            when(customerService.getById(willDeleteCustomer.getId())).thenReturn(willDeleteCustomer);
            doNothing().when(customerService).delete(willDeleteCustomer.getId());
            when(modelMapperMock.map(willDeleteCustomer, CustomerResponse.class)).thenReturn(customerResponse);

            // then
            mockMvc.perform(delete("/v1/customers/{id}", willDeleteCustomer.getId()))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value(true),
                            jsonPath("$.message").value(Msg.DELETED),
                            jsonPath("$.code").value(200),
                            jsonPath("$.data.id").value(1L),
                            jsonPath("$.data.name").value("DeletedCustomer")
                    );
        }

        @Test
        void delete_NonExistingCustomer_ThrowNotFoundException() throws Exception {
            // given
            Long nonExistingCustomerId = 10L;

            // when
            ModelMapper modelMapperMock = mock(ModelMapper.class);

            when(modelMapper.forResponse()).thenReturn(modelMapperMock);

            when(customerService.getById(nonExistingCustomerId)).thenThrow(new NotFoundException(Msg.NOT_FOUND));

            // then
            mockMvc.perform(delete("/v1/customers/{id}", nonExistingCustomerId))
                    .andDo(print())
                    .andExpectAll(
                            status().isNotFound(),
                            jsonPath("$.status").value(false),
                            jsonPath("$.message").value(Msg.NOT_FOUND),
                            jsonPath("$.code").value(404)
                    );
        }

    }

}
