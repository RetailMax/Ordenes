package com.retailmax.ordenes.model;

import java.util.Date;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//@Entity
//@Table(name = "order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    //@Column(nullable = false)
    private String userId;

    //@Column(nullable = false)
    private Date orderDate;

   
    private Double totalAmount;
    private Double shippingFee;
    private Integer statusId;
    private String paymentId;
    private String paymentStatus;
    private String shippingStreet;
    private String shippingCity;
    private String shippingRegion;
    private String shippinPostalCode;
    private Date createdAt;
    private Date updatedAt;

}
