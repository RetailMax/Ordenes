package com.retailmax.ordenes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retailmax.ordenes.model.payment.PaymentEvent;

public interface PaymentEventRepository extends JpaRepository<PaymentEvent, Long> {

}
