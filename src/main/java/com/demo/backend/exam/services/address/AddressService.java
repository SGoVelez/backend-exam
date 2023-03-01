package com.demo.backend.exam.services.address;

import java.util.List;

import com.demo.backend.exam.dto.AddressDTO;

public interface AddressService {

    public List<AddressDTO> getAllAddresses();

    public AddressDTO getAddressById(Long id);

    public AddressDTO addAddress(AddressDTO address);

    public AddressDTO updateAddress(Long id, AddressDTO address);

    public void deleteAddress(Long id);

}
