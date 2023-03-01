package com.demo.backend.exam.services.customer.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.backend.exam.dto.AddressDTO;
import com.demo.backend.exam.dto.CustomerDTO;
import com.demo.backend.exam.dto.OrderDTO;
import com.demo.backend.exam.exceptions.NotFoundException;
import com.demo.backend.exam.models.Address;
import com.demo.backend.exam.models.Customer;
import com.demo.backend.exam.repository.CustomerRepository;
import com.demo.backend.exam.repository.OrderRepository;
import com.demo.backend.exam.services.customer.CustomerService;

import javax.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customerList = customerRepository.findAll();

        List<CustomerDTO> customerDTOList = new ArrayList<>();

        for (Customer customer : customerList) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(customer.getId());
            customerDTO.setName(customer.getName());
            customerDTO.setPhoneNumber(customer.getPhoneNumber());
            customerDTO.setEmail(customer.getEmail());

            List<AddressDTO> addressDTOList = customer.getAddresses().stream().map(address -> {
                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setId(address.getId());
                addressDTO.setStreet(address.getStreet());
                addressDTO.setNumber(address.getNumber());
                addressDTO.setCity(address.getCity());
                addressDTO.setState(address.getState());
                addressDTO.setCountry(address.getCountry());
                addressDTO.setZipCode(address.getZipCode());
                return addressDTO;
            }).collect(Collectors.toList());

            List<OrderDTO> orderDTOList = customer.getOrders().stream().map(order -> {
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setId(order.getId());
                orderDTO.setOrderNumber(order.getOrderNumber());
                orderDTO.setDate(order.getDate());
                orderDTO.setPaymentType(order.getPaymentType());
                orderDTO.setTotal(order.getTotal());
                return orderDTO;
            }).collect(Collectors.toList());

            customerDTO.setAddresses(addressDTOList);
            customerDTO.setOrders(orderDTOList);

            customerDTOList.add(customerDTO);
        }

        return customerDTOList;
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer", "id", id));

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setEmail(customer.getEmail());

        List<AddressDTO> addressDTOList = customer.getAddresses().stream().map(address -> {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setId(address.getId());
            addressDTO.setStreet(address.getStreet());
            addressDTO.setNumber(address.getNumber());
            addressDTO.setCity(address.getCity());
            addressDTO.setState(address.getState());
            addressDTO.setCountry(address.getCountry());
            addressDTO.setZipCode(address.getZipCode());
            return addressDTO;
        }).collect(Collectors.toList());

        List<OrderDTO> orderDTOList = customer.getOrders().stream().map(order -> {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setOrderNumber(order.getOrderNumber());
            orderDTO.setDate(order.getDate());
            orderDTO.setPaymentType(order.getPaymentType());
            orderDTO.setTotal(order.getTotal());
            return orderDTO;
        }).collect(Collectors.toList());

        customerDTO.setAddresses(addressDTOList);
        customerDTO.setOrders(orderDTOList);

        return customerDTO;
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
        customerToUpdate
                .setAddresses(customer.getAddresses().stream().map(address -> modelMapper.map(address, Address.class))
                        .collect(Collectors.toList()));

        Customer updatedCustomer = customerRepository.save(customerToUpdate);

        CustomerDTO responseCustomerDTO = new CustomerDTO();
        responseCustomerDTO.setId(updatedCustomer.getId());
        responseCustomerDTO.setName(updatedCustomer.getName());
        responseCustomerDTO.setPhoneNumber(updatedCustomer.getPhoneNumber());
        responseCustomerDTO.setEmail(updatedCustomer.getEmail());

        List<AddressDTO> addressDTOList = updatedCustomer.getAddresses().stream()
                .map(address -> modelMapper.map(address, AddressDTO.class)).collect(Collectors.toList());

        responseCustomerDTO.setAddresses(addressDTOList);

        return responseCustomerDTO;
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
