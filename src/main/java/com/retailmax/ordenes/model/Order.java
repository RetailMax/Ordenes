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
    @Column(name = "order_id")
    private Long id;
    @Column(name = "user_id")
    private String userId;
    private Date orderDate;
    private Double totalAmount;
    private Double shippingFee;
    private Integer statusId;
    private String paymentId;
    private String paymentStatus;

    private String shippingStreet;
    private String shippingCity;
    private String shippingRegion;
    private String shippingPostalCode;

    private Date createdAt;
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