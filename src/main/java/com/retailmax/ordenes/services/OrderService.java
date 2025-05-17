package com.retailmax.ordenes.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retailmax.ordenes.model.Order;
import com.retailmax.ordenes.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Boolean addOrder(Order order) {
        return orderRepository.addNewOrder(order);
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public Boolean updateOrder(Order order) {
        return orderRepository.updateOrder(order);
    }

    public Boolean deleteOrder(String id) {
        return orderRepository.deleteOrderById(id);
    }

}
