package com.retailmax.ordenes.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retailmax.ordenes.model.Order;
import com.retailmax.ordenes.services.OrderService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService OrderService;

  @GetMapping()
  public ResponseEntity<List<Order>> listOrders() {
    List<Order> oredenes = OrderService.getOrders();
    if(oredenes.isEmpty()){
        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(oredenes);
  }

  @PostMapping()
  public ResponseEntity<Boolean> addOrder(@RequestBody Order order) {
      boolean result = OrderService.addOrder(order);

      if(result){
        return ResponseEntity.ok(true);
      } else{
        return ResponseEntity.badRequest().body(false);
      }
  }

  @GetMapping("{id}")
  public ResponseEntity<Order> getOrderById(@PathVariable String id) {
    Order order = OrderService.getOrderById(id);
    if (order.equals(null)){
        return ResponseEntity.noContent().build();

    }
    return ResponseEntity.ok(order);  
  }
  
  @PutMapping()
  public ResponseEntity<Boolean> updateOrder(@RequestBody Order order) { 
        boolean result = OrderService.updateOrder(order);     
        if(result){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.badRequest().body(false);
        }
  }
  
  @DeleteMapping("{id}")
  public  ResponseEntity<Boolean> deleteOrderById(@PathVariable String id) {
    Boolean order = OrderService.deleteOrder(id);
    if(order){
        return ResponseEntity.ok(true);
    }else{
        return ResponseEntity.badRequest().body(false);
    }
}
}  

