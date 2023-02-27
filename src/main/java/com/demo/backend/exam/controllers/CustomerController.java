package com.demo.backend.exam.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.backend.exam.dto.CustomerDTO;
import com.demo.backend.exam.services.customer.CustomerServices;

@RestController
public class CustomerController {

    @Autowired
    private CustomerServices customerServices;

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return new ResponseEntity<List<CustomerDTO>>(customerServices.getAllCustomers(), HttpStatus.OK);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return new ResponseEntity<CustomerDTO>(customerServices.getCustomerById(id), HttpStatus.OK);
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerDTO> addCustomer(@RequestBody CustomerDTO customer) {
        return new ResponseEntity<CustomerDTO>(customerServices.addCustomer(customer), HttpStatus.CREATED);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customer) {
        return new ResponseEntity<CustomerDTO>(customerServices.updateCustomer(id, customer), HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerServices.deleteCustomer(id);
        return new ResponseEntity<String>("Customer deleted successfully", HttpStatus.OK);
    }

}
