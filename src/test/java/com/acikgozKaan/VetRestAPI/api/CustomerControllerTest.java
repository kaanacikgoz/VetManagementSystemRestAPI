package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.ICustomerService;
import com.acikgozKaan.VetRestAPI.core.modelMapper.IModelMapperService;
import com.acikgozKaan.VetRestAPI.core.utilies.Msg;
import com.acikgozKaan.VetRestAPI.dto.request.customer.CustomerSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.response.CustomerResponse;
import com.acikgozKaan.VetRestAPI.entity.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}
