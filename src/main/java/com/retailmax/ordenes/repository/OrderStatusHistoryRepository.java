package com.retailmax.ordenes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retailmax.ordenes.model.status.OrderStatusHistory;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long>  {

}
