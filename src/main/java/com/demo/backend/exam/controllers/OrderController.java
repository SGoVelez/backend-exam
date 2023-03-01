package com.demo.backend.exam.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.backend.exam.dto.OrderDTO;
import com.demo.backend.exam.services.order.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderDTO orderDTO) {

        return new ResponseEntity<OrderDTO>(orderService.addOrder(orderDTO), HttpStatus.CREATED);

    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> addOrder(@PathVariable Long customerId) {

        return new ResponseEntity<List<OrderDTO>>(orderService.getAllOrdersByCustomerId(customerId),
                HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {
        OrderDTO orderUpdatedDTO = orderService.updateOrder(orderId, orderDTO);
        return new ResponseEntity<OrderDTO>(orderUpdatedDTO, HttpStatus.OK);
    }

}
