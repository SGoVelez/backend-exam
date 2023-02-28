package com.demo.backend.exam.services.address.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.backend.exam.repository.AddressRepository;
import com.demo.backend.exam.services.address.AddressService;
import com.demo.backend.exam.dto.AddressDTO;
import com.demo.backend.exam.exceptions.NotFoundException;
import com.demo.backend.exam.models.Address;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<AddressDTO> getAllAddresses() {

        List<Address> addressList = addressRepository.findAll();
        List<AddressDTO> addressDTOList = addressList.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .collect(Collectors.toList());

        return addressDTOList;
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new NotFoundException("Address", "id", id));

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public AddressDTO addAddress(AddressDTO address) {
        Address addressToSave = modelMapper.map(address, Address.class);

        return modelMapper.map(addressRepository.save(addressToSave), AddressDTO.class);
    }

    @Override
    public AddressDTO updateAddress(Long id, AddressDTO address) {
        Address addressToUpdate = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address", "id", id));

        addressToUpdate.setStreet(address.getStreet());
        addressToUpdate.setNumber(address.getNumber());
        addressToUpdate.setCity(address.getCity());
        addressToUpdate.setState(address.getState());
        addressToUpdate.setCountry(address.getCountry());
        addressToUpdate.setZipCode(address.getZipCode());

        return modelMapper.map(addressRepository.save(addressToUpdate), AddressDTO.class);
    }

    @Override
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

}
