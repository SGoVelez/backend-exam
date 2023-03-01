package com.demo.backend.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.backend.exam.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
