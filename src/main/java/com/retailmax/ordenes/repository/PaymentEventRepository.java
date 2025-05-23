package com.retailmax.ordenes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retailmax.ordenes.model.PaymentEvent;

public interface PaymentEventRepository extends JpaRepository<PaymentEvent, Long> {

}
