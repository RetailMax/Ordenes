package com.retailmax.ordenes.model.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class OrderAddress {
    @Column(name = "street")
    private String street;
    @Column(name = "city")
    private String city;
    @Column(name = "region")
    private String region;
    @Column(name = "postal_code")
    private String postalCode;
}