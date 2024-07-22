package com.acikgozKaan.VetRestAPI.api;

import com.acikgozKaan.VetRestAPI.business.abstracts.ICustomerService;
import com.acikgozKaan.VetRestAPI.core.modelMapper.IModelMapperService;
import com.acikgozKaan.VetRestAPI.core.result.ResultData;
import com.acikgozKaan.VetRestAPI.core.utilies.ResultHelper;
import com.acikgozKaan.VetRestAPI.dto.request.customer.CustomerSaveRequest;
import com.acikgozKaan.VetRestAPI.dto.request.customer.CustomerUpdateRequest;
import com.acikgozKaan.VetRestAPI.dto.response.CustomerResponse;
import com.acikgozKaan.VetRestAPI.entity.Customer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {

    private final ICustomerService customerService;
    private final IModelMapperService modelMapper;

    public CustomerController(ICustomerService customerService, IModelMapperService modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<CustomerResponse> save(@Valid @RequestBody CustomerSaveRequest customerSaveRequest) {
        Customer saveCustomer = modelMapper.forRequest().map(customerSaveRequest, Customer.class);
        this.customerService.save(saveCustomer);

        CustomerResponse customerResponse = modelMapper.forResponse().map(saveCustomer, CustomerResponse.class);
        return ResultHelper.created(customerResponse);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<CustomerResponse>> getAll() {
        List<Customer> customers = customerService.getAll();
        List<CustomerResponse> customerResponses = customers.stream().map(
                        customer -> modelMapper.forResponse().map(customer, CustomerResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(customerResponses);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CustomerResponse> update(@Valid @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        Customer updateCustomer = modelMapper.forRequest().map(customerUpdateRequest, Customer.class);
        customerService.update(updateCustomer);

        CustomerResponse customerResponse = modelMapper.forResponse().map(updateCustomer, CustomerResponse.class);
        return ResultHelper.success(customerResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CustomerResponse> delete(@PathVariable("id") Long id) {
        Customer deletedCustomer = customerService.getById(id);
        this.customerService.delete(id);
        CustomerResponse customerResponse = modelMapper.forResponse().map(deletedCustomer, CustomerResponse.class);
        return ResultHelper.deleted(customerResponse);
    }

}
