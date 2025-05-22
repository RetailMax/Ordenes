package com.retailmax.ordenes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.retailmax.ordenes.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findById(String id);
    Boolean addNewOrder(Order order);
    Boolean updateOrder(Order order);
    Boolean deleteOrderById(String id);
}
