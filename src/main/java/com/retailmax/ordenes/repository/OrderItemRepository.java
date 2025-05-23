package com.retailmax.ordenes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retailmax.ordenes.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>  {

}
