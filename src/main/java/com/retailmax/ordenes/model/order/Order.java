package com.retailmax.ordenes.model.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.retailmax.ordenes.model.payment.PaymentEvent;
import com.retailmax.ordenes.model.payment.PaymentStatus;
import com.retailmax.ordenes.model.returns.OrderReturn;
import com.retailmax.ordenes.model.status.OrderStatus;
import com.retailmax.ordenes.model.status.OrderStatusHistory;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "order_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "order_date", nullable = false)
    private Date orderDate;
    @Column(name = "total_amount")
    private Double totalAmount;
    @Column(name = "shipping_fee")
    private Double shippingFee;

    @Column(name = "payment_id")
    private String paymentId;

    @Embedded
    private OrderAddress shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    @JsonManagedReference("order-items")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @JsonManagedReference("order-returns")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderReturn> returns;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @JsonManagedReference("order-status-history")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderStatusHistory> statusHistory;

    @JsonManagedReference("order-payment-events")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<PaymentEvent> paymentEvents;

    public void updateStatus(OrderStatus newStatus, String changedBy) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(this);
        history.setPreviousStatus(this.status);
        history.setNewStatus(newStatus);
        history.setChangedAt(new Date());
        history.setChangedBy(changedBy);
        
        this.status = newStatus;
        this.statusHistory.add(history);
    }

    public void addItem(OrderItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        item.setOrder(this);
    }

    public void addPaymentEvent(PaymentEvent event) {
        if (paymentEvents == null) {
            paymentEvents = new ArrayList<>();
        }
        paymentEvents.add(event);
        event.setOrder(this);
    }

}