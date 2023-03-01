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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.backend.exam.dto.AddressDTO;
import com.demo.backend.exam.services.address.AddressService;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressService addressServices;

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        return new ResponseEntity<List<AddressDTO>>(addressServices.getAllAddresses(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id) {
        return new ResponseEntity<AddressDTO>(addressServices.getAddressById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO address) {
        return new ResponseEntity<AddressDTO>(addressServices.addAddress(address), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id, @RequestBody AddressDTO address) {
        return new ResponseEntity<AddressDTO>(addressServices.updateAddress(id, address), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        addressServices.deleteAddress(id);
        return new ResponseEntity<String>("Address deleted successfully", HttpStatus.OK);
    }

}
