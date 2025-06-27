package com.retailmax.ordenes.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.retailmax.ordenes.model.order.Order;
import com.retailmax.ordenes.model.order.OrderItem;
import com.retailmax.ordenes.model.payment.PaymentEvent;
import com.retailmax.ordenes.model.status.OrderStatus;
import com.retailmax.ordenes.model.status.OrderStatusHistory;

public class OrderTest {



    @Test
    void addItemTest(){
        Order order1 = new Order();
        //Order order2 = new Order();
        OrderItem orderItem = new OrderItem(1L,order1,"1", "product", "sku",1,1.0);

        order1.addItem(orderItem);

        assertEquals(1,order1.getItems().size());
        assertEquals(orderItem, order1.getItems().get(0));
        
    }

    @Test
    void addPaymentEventTest(){
        Order order1 = new Order();
        PaymentEvent paymentEvent = new PaymentEvent(1L, order1, "1", "1", new Date());

        order1.addPaymentEvent(paymentEvent);

        assertEquals(1, order1.getPaymentEvents().size());
        assertEquals(paymentEvent, order1.getPaymentEvents().get(0));
    }

}
