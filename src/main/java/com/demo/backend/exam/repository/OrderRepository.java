package com.demo.backend.exam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.backend.exam.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    public List<Order> findAllByCustomerId(Long customerId);

    public Boolean existsByCustomerId(Long customerId);
}
