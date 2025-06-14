package com.retailmax.ordenes.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retailmax.ordenes.model.order.Order;
import com.retailmax.ordenes.model.order.OrderItem;
import com.retailmax.ordenes.model.status.OrderStatus;

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
        order.setStatusHistory(new ArrayList<>());
        order.setPaymentEvents(new ArrayList<>());
        calculateTotal(order);
        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).get();
    }

    @Transactional
    public Order updateOrder(Long id, Order updatedOrder) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada: " + id));

        if (existingOrder.getStatus() == OrderStatus.COMPLETED ||
                existingOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("No se puede actualizar una orden completada o cancelada");
        }

        if (updatedOrder.getStatus() != existingOrder.getStatus()) {
            existingOrder.updateStatus(updatedOrder.getStatus(), "USER");
        }

        if (updatedOrder.getShippingAddress() != null) {
            existingOrder.setShippingAddress(updatedOrder.getShippingAddress());
        }

        if (updatedOrder.getItems() != null && !updatedOrder.getItems().isEmpty()) {
            existingOrder.getItems().clear();
            for (OrderItem item : updatedOrder.getItems()) {
                item.setOrder(existingOrder);
                existingOrder.getItems().add(item);
            }

            calculateTotal(existingOrder);
        }

        existingOrder.setUpdatedAt(new Date());

        return orderRepository.save(existingOrder);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getOrdersByUserId(String id) {
        return orderRepository.findByUserId(id);
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        if (order.getStatus() == OrderStatus.CANCELLED ||
                order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("No se puede cancelar una orden que ya está cancelada o completada");
        }


        order.updateStatus(OrderStatus.CANCELLED, "SYSTEM");
        order.setUpdatedAt(new Date());

        return orderRepository.save(order);
    }

    public void calculateTotal(Order order) {
        double total = 0;
        for (OrderItem item : order.getItems()) {
            total += item.getQuantity() * item.getUnitPrice();
        }

        if (order.getShippingFee() != null) {
            total += order.getShippingFee();
        }

        order.setTotalAmount(total);
    }

}
