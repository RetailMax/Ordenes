package com.retailmax.ordenes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.retailmax.ordenes.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    
    public void getOrders(){}
    public void addOrder(){}
    public void getOrderById(String id){}
    public void updateOrder(String id){}
    public void deleteOrder(String id){}


}
