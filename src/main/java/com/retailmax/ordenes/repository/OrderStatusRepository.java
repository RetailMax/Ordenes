package com.retailmax.ordenes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retailmax.ordenes.model.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

}
