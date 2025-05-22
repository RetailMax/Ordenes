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

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).get();
    }

    //public Boolean updateOrder(Order order) {
      //  return orderRepository.updateOrder(order);
    //}

    public void delete(Long id){
        orderRepository.deleteById(id);
    }

}
