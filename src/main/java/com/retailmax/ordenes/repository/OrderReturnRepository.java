package com.retailmax.ordenes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retailmax.ordenes.model.returns.OrderReturn;

public interface OrderReturnRepository extends JpaRepository<OrderReturn, Long>  {

}
