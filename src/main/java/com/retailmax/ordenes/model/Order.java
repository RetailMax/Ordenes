package com.retailmax.ordenes.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String id;
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
    private String shippinPostalCode;
    private Date createdAt;
    private Date updatedAt;

}
