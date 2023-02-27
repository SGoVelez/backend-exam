package com.demo.backend.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.backend.exam.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
