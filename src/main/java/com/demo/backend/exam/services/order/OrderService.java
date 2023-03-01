package com.demo.backend.exam.services.order;

import java.util.List;

import com.demo.backend.exam.dto.OrderDTO;

public interface OrderService {

    public List<OrderDTO> getAllOrders(Integer page, Integer pageSize, String sortBy, String sortDirection);

    public List<OrderDTO> getAllOrdersByCustomerId(Long customerId);

    public OrderDTO getOrderById(Long id);

    public OrderDTO addOrder(OrderDTO orderDTO);

    public OrderDTO updateOrder(Long id, OrderDTO orderDTO);

    public void deleteOrder(Long id);

}
