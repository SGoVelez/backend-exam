package com.demo.backend.exam.dto;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.demo.backend.exam.models.Order;
import com.demo.backend.exam.models.Product;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItemDTO {

    private Long id;

    private Integer quantity;

    private Product product;

    private Order order;
}
