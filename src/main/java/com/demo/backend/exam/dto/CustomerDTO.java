package com.demo.backend.exam.dto;

import java.util.List;

import com.demo.backend.exam.models.Address;
import com.demo.backend.exam.models.Order;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {

    private Long id;

    private String name;

    private String phoneNumber;

    private String email;

    private List<Address> addresses;

    private List<Order> orders;

}
