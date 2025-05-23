package com.retailmax.ordenes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retailmax.ordenes.model.Order;
import com.retailmax.ordenes.model.OrderReturn;
import com.retailmax.ordenes.repository.OrderRepository;
import com.retailmax.ordenes.repository.OrderReturnRepository;

@Service
public class OrderReturnService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderReturnRepository orderReturnRepository;

    public OrderReturn addReturn(Long orderId, OrderReturn orderReturn){
        Order order = orderRepository.findById(orderId).orElse(null);

        if(order == null){
            System.out.println("No se encontró la orden con ID: " + orderId);
            return null;
        }

        orderReturn.setOrder(order);

        return orderReturnRepository.save(orderReturn);

    }

}
