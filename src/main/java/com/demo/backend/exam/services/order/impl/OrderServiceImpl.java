package com.demo.backend.exam.services.order.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.backend.exam.dto.CartItemDTO;
import com.demo.backend.exam.dto.OrderDTO;
import com.demo.backend.exam.models.Address;
import com.demo.backend.exam.models.Customer;
import com.demo.backend.exam.models.Order;
import com.demo.backend.exam.models.OrderItem;
import com.demo.backend.exam.models.Product;
import com.demo.backend.exam.repository.AddressRepository;
import com.demo.backend.exam.repository.CustomerRepository;
import com.demo.backend.exam.repository.OrderItemRepository;
import com.demo.backend.exam.repository.OrderRepository;
import com.demo.backend.exam.repository.ProductRepository;
import com.demo.backend.exam.services.order.OrderService;

import com.demo.backend.exam.exceptions.NotFoundException;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<OrderDTO> getAllOrders(Integer page, Integer pageSize, String sortBy, String sortDirection) {

        throw new UnsupportedOperationException("Unimplemented method 'getAllOrders'");
    }

    @Override
    public List<OrderDTO> getAllOrdersByCustomerId(Long customerId) {

        List<Order> orders = orderRepository.findAllByCustomerId(customerId);

        List<OrderDTO> ordersDTO = new ArrayList<OrderDTO>();

        for (Order order : orders) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setOrderNumber(order.getOrderNumber());
            orderDTO.setDate(order.getDate());
            orderDTO.setPaymentType(order.getPaymentType());
            orderDTO.setTotal(order.getTotal());
            orderDTO.setCustomerId(order.getCustomer().getId());
            orderDTO.setAddressId(order.getShippingAddress().getId());
            List<CartItemDTO> cartItemsDTO = new ArrayList<CartItemDTO>();
            for (OrderItem orderItem : order.getOrderItems()) {
                System.out.println("ORDER PRODUCT:" + orderItem.getProduct());
                CartItemDTO cartItemDTO = new CartItemDTO();
                cartItemDTO.setId(orderItem.getProduct().getId());
                cartItemDTO.setQuantity(orderItem.getQuantity());
                cartItemsDTO.add(cartItemDTO);
            }
            orderDTO.setProducts(cartItemsDTO);
            ordersDTO.add(orderDTO);
        }

        return ordersDTO;
    }

    @Override
    public OrderDTO getOrderById(Long id) {

        throw new UnsupportedOperationException("Unimplemented method 'getOrderById'");
    }

    @Transactional
    @Override
    public OrderDTO addOrder(OrderDTO orderDTO) {
        // Save a temporal Order
        Order temporalOrder = modelMapper.map(orderDTO, Order.class);
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer", "id", orderDTO.getCustomerId()));

        Address shippingAddress = addressRepository.findById(orderDTO.getAddressId())
                .orElseThrow(() -> new NotFoundException("Address", "id", orderDTO.getAddressId()));
        temporalOrder.setCustomer(customer);
        temporalOrder.setShippingAddress(shippingAddress);
        temporalOrder.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        temporalOrder.setTotal(0.0);

        Order temporalOrderSaved = orderRepository.save(temporalOrder);
        System.out.println("Temporal Order saved: " + temporalOrderSaved);
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        Double orderTotal = 0.0;

        for (CartItemDTO cartItemDTO : orderDTO.getProducts()) {
            Product product = productRepository.findById(cartItemDTO.getId())
                    .orElseThrow(() -> new NotFoundException("Product", "id", cartItemDTO.getId()));

            // OrderItem to save
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(temporalOrderSaved);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItemDTO.getQuantity());
            orderItemRepository.save(orderItem);

            // Add OrderItem to Order. Increase order total
            orderTotal += cartItemDTO.getQuantity() * product.getPrice();
            orderItems.add(orderItem);
        }

        // Update temporal Order with OrderItems and total
        temporalOrderSaved.setTotal(orderTotal);
        temporalOrderSaved.setOrderItems(orderItems);
        Order orderSaved = orderRepository.save(temporalOrderSaved);

        // OrdrDTO to return
        OrderDTO resultOrderDTO = new OrderDTO();
        resultOrderDTO.setId(orderSaved.getId());
        resultOrderDTO.setOrderNumber(orderSaved.getOrderNumber());
        resultOrderDTO.setDate(orderSaved.getDate());
        resultOrderDTO.setPaymentType(orderSaved.getPaymentType());
        resultOrderDTO.setTotal(orderSaved.getTotal());
        resultOrderDTO.setCustomerId(orderSaved.getCustomer().getId());
        resultOrderDTO.setAddressId(orderSaved.getShippingAddress().getId());

        List<CartItemDTO> cartItemsDTO = new ArrayList<CartItemDTO>();

        for (OrderItem orderItem : orderSaved.getOrderItems()) {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setId(orderItem.getProduct().getId());
            cartItemDTO.setQuantity(orderItem.getQuantity());
            cartItemsDTO.add(cartItemDTO);
        }
        resultOrderDTO.setProducts(cartItemsDTO);

        return resultOrderDTO;

    }

    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {

        /*
         * UPDATE ORDER
         * Only update shipping address and order items
         */

        // Get Order to update
        Order orderToUpdate = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", "id", id));

        // Update shipping address if provided
        if (orderDTO.getAddressId() != null) {
            Address shippingAddress = addressRepository.findById(orderDTO.getAddressId())
                    .orElseThrow(() -> new NotFoundException("Address", "id", orderDTO.getCustomerId()));
            orderToUpdate.setShippingAddress(shippingAddress);
        }

        // Update OrderItems and total
        Double orderTotal = orderToUpdate.getTotal();
        List<OrderItem> orderItems = orderToUpdate.getOrderItems();
        System.out.println("CART ITEMS:" + orderDTO.getProducts());
        for (CartItemDTO cartItemDTO : orderDTO.getProducts()) {
            System.out.println("CART ITEM:" + cartItemDTO);
            Product product = productRepository.findById(cartItemDTO.getId())
                    .orElseThrow(() -> new NotFoundException("Product", "id", cartItemDTO.getId()));

            if (cartItemDTO.getQuantity() == 0) {
                // Delete OrderItem
                OrderItem orderItemToDelete = orderItemRepository
                        .findByOrderAndProduct(orderToUpdate, product);

                if (orderItemToDelete == null) {
                    throw new NotFoundException("OrderItem", "id", cartItemDTO.getId());
                }

                orderItemRepository.delete(orderItemToDelete);
                orderTotal -= orderItemToDelete.getQuantity() * product.getPrice();
                orderItems.remove(orderItemToDelete);
            } else {
                // Update OrderItem
                OrderItem orderItemToUpdate = orderItemRepository
                        .findByOrderAndProduct(orderToUpdate, product);

                if (orderItemToUpdate == null) {
                    // Create OrderItem
                    orderItemToUpdate = new OrderItem();
                    orderItemToUpdate.setOrder(orderToUpdate);
                    orderItemToUpdate.setProduct(product);
                    orderItemToUpdate.setQuantity(cartItemDTO.getQuantity());
                    orderItemRepository.save(orderItemToUpdate);

                    orderTotal += cartItemDTO.getQuantity() * product.getPrice();
                    orderItems.add(orderItemToUpdate);
                    continue;

                }

                // Decrese order total with old price to then increase with new price
                orderTotal -= orderItemToUpdate.getQuantity() * product.getPrice();

                orderItemToUpdate.setQuantity(cartItemDTO.getQuantity());
                orderItemRepository.save(orderItemToUpdate);
                orderItems.add(orderItemToUpdate);

                orderTotal += cartItemDTO.getQuantity() * product.getPrice();
                orderItems.add(orderItemToUpdate);
            }
        }

        // Update temporal Order with OrderItems and total
        orderToUpdate.setTotal(orderTotal);
        orderToUpdate.setOrderItems(orderItems);
        Order orderUpdated = orderRepository.saveAndFlush(orderToUpdate);

        // OrdrDTO to return
        OrderDTO resultOrderDTO = new OrderDTO();
        resultOrderDTO.setId(orderUpdated.getId());
        resultOrderDTO.setOrderNumber(orderUpdated.getOrderNumber());
        resultOrderDTO.setDate(orderUpdated.getDate());
        resultOrderDTO.setPaymentType(orderUpdated.getPaymentType());
        resultOrderDTO.setTotal(orderUpdated.getTotal());
        resultOrderDTO.setCustomerId(orderUpdated.getCustomer().getId());
        resultOrderDTO.setAddressId(orderUpdated.getShippingAddress().getId());

        List<CartItemDTO> cartItemsDTO = new ArrayList<CartItemDTO>();
        for (OrderItem orderItem : orderUpdated.getOrderItems()) {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setId(orderItem.getProduct().getId());
            cartItemDTO.setQuantity(orderItem.getQuantity());
            cartItemsDTO.add(cartItemDTO);
        }
        resultOrderDTO.setProducts(cartItemsDTO);

        return resultOrderDTO;
    }

    @Override
    public void deleteOrder(Long id) {
        orderItemRepository.deleteById(id);
    }

}
