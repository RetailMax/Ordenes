package com.retailmax.ordenes.controller;


import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retailmax.ordenes.model.order.Order;
import com.retailmax.ordenes.model.payment.PaymentStatus;
import com.retailmax.ordenes.model.returns.OrderReturn;
import com.retailmax.ordenes.model.returns.ReturnStatus;
import com.retailmax.ordenes.model.status.OrderStatus;
import com.retailmax.ordenes.services.OrderReturnService;
import com.retailmax.ordenes.services.OrderService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderReturnService orderReturnService;

  @GetMapping()
  public ResponseEntity<List<Order>> listOrders() {
    List<Order> oredenes = orderService.getOrders();
    if (oredenes.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(oredenes);
  }

  @PostMapping()
  public ResponseEntity<Order> addOrder(@RequestBody Order order) {
    order.setStatus(OrderStatus.CREATED);
    order.setCreatedAt(new Date());
    order.setOrderDate(new Date());
    order.setPaymentStatus(PaymentStatus.PENDING);

    if (order.getItems() == null || order.getItems().isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    Order result = orderService.addOrder(order);

    return ResponseEntity.status(HttpStatus.CREATED).body(result);

  }

  @GetMapping("{id}")
  public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
    try {
      Order order = orderService.getOrderById(id);
      return ResponseEntity.ok(order);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
    try {
      updatedOrder.setId(id);
      
      Order order = orderService.updateOrder(id, updatedOrder);
      return ResponseEntity.ok(order);
  } catch (RuntimeException e) {
      if (e instanceof IllegalStateException) {
          return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
      return ResponseEntity.notFound().build();
  }
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
    try {
      orderService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/client/{userId}")
  public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable String userId) {
    List<Order> orders = orderService.getOrdersByUserId(userId);
    if (orders.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(orders);
  }

  @PostMapping("/{orderId}/returns")
  public ResponseEntity<OrderReturn> addReturn(@PathVariable Long orderId, @RequestBody OrderReturn orderReturn) {
    orderReturn.setStatus(ReturnStatus.REQUESTED);
    OrderReturn createdReturn = orderReturnService.addReturn(orderId, orderReturn);
    if (createdReturn == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createdReturn);
  }

  @PutMapping("/{id}/cancellation")
  public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
    Order canceledOrder = orderService.cancelOrder(id);
    if (canceledOrder != null) {
      return ResponseEntity.ok(canceledOrder);
    }
    return ResponseEntity.notFound().build();
  }

}
