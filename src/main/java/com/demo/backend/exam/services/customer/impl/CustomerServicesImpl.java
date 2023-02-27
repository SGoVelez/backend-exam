package com.demo.backend.exam.services.customer.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.backend.exam.dto.CustomerDTO;
import com.demo.backend.exam.exceptions.NotFoundException;
import com.demo.backend.exam.models.Customer;
import com.demo.backend.exam.repository.CustomerRepository;
import com.demo.backend.exam.repository.OrderRepository;
import com.demo.backend.exam.services.customer.CustomerServices;

import jakarta.transaction.Transactional;

@Service
public class CustomerServicesImpl implements CustomerServices {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customerList = customerRepository.findAll();

        return customerList.stream().map(customer -> modelMapper.map(customer, CustomerDTO.class)).toList();
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer", "id", id));

        return modelMapper.map(customer, CustomerDTO.class);
    }

    @Override
    @Transactional
    public CustomerDTO addCustomer(CustomerDTO customer) {
        Customer customerToSave = modelMapper.map(customer, Customer.class);
        Customer savedCustomer = customerRepository.save(customerToSave);

        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO customer) {
        Customer customerToUpdate = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer", "id", id));

        customerToUpdate.setName(customer.getName());
        customerToUpdate.setPhoneNumber(customer.getPhoneNumber());
        customerToUpdate.setEmail(customer.getEmail());
        customerToUpdate.setAddresses(customer.getAddresses());

        Customer updatedCustomer = customerRepository.save(customerToUpdate);

        return modelMapper.map(updatedCustomer, CustomerDTO.class);
    }

    @Override
    public void deleteCustomer(Long id) {

        Customer customerToDelete = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer", "id", id));

        Boolean customerHasOrders = orderRepository.existsByCustomerId(id);

        if (customerHasOrders) {
            throw new RuntimeException(String.format("Can not delete customer with id %s because it has orders", id));
        }

        customerRepository.deleteById(id);
    }

}
