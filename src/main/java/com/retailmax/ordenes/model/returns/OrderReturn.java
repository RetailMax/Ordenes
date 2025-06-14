package com.retailmax.ordenes.model.returns;

import java.math.BigDecimal;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.retailmax.ordenes.model.order.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_returns")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "return_id")
    private Long id;

    @JsonBackReference("order-returns")
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "return_type",nullable = false)
    private String returnType;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReturnStatus status;

    @Column(name = "request_at", nullable = false)
    private Date requestedAt;

    @Column(name = "processed_by")
    private String processedby;
    
    @Column(name = "processed_at")
    private Date processedAt;

    @Column(name = "refund_amount")
    private Double refundAmount;


}
