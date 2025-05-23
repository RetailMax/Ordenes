package com.retailmax.ordenes.model;

import java.util.Date;
import java.util.List;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    //@Column(name = "order_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "order_date", nullable = false)
    private Date orderDate;
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;
    @Column(name = "shipping_fee")
    private Double shippingFee;
    @Column(name = "status_id", nullable = false)
    private Integer statusId;
    @Column(name = "payment_id")
    private String paymentId;
    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "shipping_street")
    private String shippingStreet;
    @Column(name = "shipping_city")
    private String shippingCity;
    @Column(name = "shipping_region")
    private String shippingRegion;
    @Column(name = "shipping_postal_code")
    private String shippingPostalCode;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderReturn> returns;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderStatusHistory> statusHistory;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<PaymentEvent> paymentEvents;
}