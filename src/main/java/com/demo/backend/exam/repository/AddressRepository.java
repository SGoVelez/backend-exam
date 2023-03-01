package com.demo.backend.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.backend.exam.models.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
