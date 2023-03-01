package com.demo.backend.exam.services.orderItem.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.backend.exam.dto.OrderItemDTO;
import com.demo.backend.exam.models.OrderItem;
import com.demo.backend.exam.repository.OrderItemRepository;
import com.demo.backend.exam.services.orderItem.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public OrderItemDTO addOrderItem(OrderItemDTO orderItem) {
        OrderItem orderItemToSave = modelMapper.map(orderItem, OrderItem.class);

        return modelMapper.map(orderItemRepository.save(orderItemToSave), OrderItemDTO.class);

    }

}
