package com.demo.backend.exam.dto;

import java.util.List;

import com.demo.backend.exam.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@JsonInclude(Include.NON_NULL)
public class OrderDTO {

    private Long id;

    private Long orderNumber;

    private String date;

    private PaymentMethod paymentType;

    private Double total;

    private Long customerId;

    private Long addressId;

    private List<CartItemDTO> productIds;

    private List<OrderItemDTO> orderItems;

}
