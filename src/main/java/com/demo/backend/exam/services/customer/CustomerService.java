package com.demo.backend.exam.services.customer;

import java.util.List;

import com.demo.backend.exam.dto.CustomerDTO;

public interface CustomerService {

    public List<CustomerDTO> getAllCustomers();

    public CustomerDTO getCustomerById(Long id);

    public CustomerDTO addCustomer(CustomerDTO customer);

    public CustomerDTO updateCustomer(Long id, CustomerDTO customer);

    public void deleteCustomer(Long id);

}
