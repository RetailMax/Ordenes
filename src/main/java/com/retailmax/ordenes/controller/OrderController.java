package com.retailmax.ordenes.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retailmax.ordenes.model.Order;
import com.retailmax.ordenes.model.OrderReturn;
import com.retailmax.ordenes.services.OrderReturnService;
import com.retailmax.ordenes.services.OrderService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

  @Autowired
  private OrderService OrderService;

  @Autowired
  private OrderReturnService orderReturnService;



  @GetMapping()
  public ResponseEntity<List<Order>> listOrders() {
    List<Order> oredenes = OrderService.getOrders();
    if (oredenes.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(oredenes);
  }

  @PostMapping()
  public ResponseEntity<Order> addOrder(@RequestBody Order order) {
    Order result = OrderService.addOrder(order);

    if (result != null) {
      return ResponseEntity.ok(result);
    } else {
      return ResponseEntity.noContent().build();
    }
  }

  @GetMapping("{id}")
  public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
    Order order = OrderService.getOrderById(id);
    if (order.equals(null)) {
      return ResponseEntity.noContent().build();

    }
    return ResponseEntity.ok(order);
  }

  
  /*
   * @PutMapping()
   * public ResponseEntity<Boolean> updateOrder(@RequestBody Order order) {
   * boolean result = OrderService.updateOrder(order);
   * if(result){
   * return ResponseEntity.ok(true);
   * }else{
   * return ResponseEntity.badRequest().body(false);
   * }
   * }
   */

  @DeleteMapping("{id}")
  public void deleteOrderById(@PathVariable Long id) {
    OrderService.delete(id);
  }

    @GetMapping("/client/{userId}")
  public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable String userId) {
    List<Order> orders = OrderService.getOrdersByUserId(userId);
    if (orders.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(orders);
  }

  @PostMapping("/{orderId}/return")
  public  ResponseEntity<OrderReturn> addReturn(@PathVariable Long orderId,@RequestBody OrderReturn orderReturn) {
      OrderReturn createdReturn = orderReturnService.addReturn(orderId, orderReturn);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdReturn);
  }
  

}
