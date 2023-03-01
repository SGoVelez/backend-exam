package com.demo.backend.exam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.backend.exam.models.Order;
import com.demo.backend.exam.models.OrderItem;
import com.demo.backend.exam.models.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    public OrderItem findByOrderAndProduct(Order order, Product product);

    public List<OrderItem> findByProduct(Product product);

    public List<OrderItem> findByOrder(Order order);
}
