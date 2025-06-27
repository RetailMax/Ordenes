package com.retailmax.ordenes.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retailmax.ordenes.assemblers.OrderModelAssembler;
import com.retailmax.ordenes.model.order.Order;
import com.retailmax.ordenes.model.payment.PaymentStatus;
import com.retailmax.ordenes.model.returns.OrderReturn;
import com.retailmax.ordenes.model.returns.ReturnStatus;
import com.retailmax.ordenes.model.status.OrderStatus;
import com.retailmax.ordenes.services.OrderReturnService;
import com.retailmax.ordenes.services.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Ordenes", description = "Operaciones relacionadas con Ordenes")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderReturnService orderReturnService;

  @Autowired
  private OrderModelAssembler assembler;

  @GetMapping()
  @Operation(summary = "Obtener todas las Ordenes", description = "Obtiene una lista de todas las ordenes registradas")
  @ApiResponse(responseCode = "200", description = "Operación exitosa")
  public ResponseEntity<CollectionModel<EntityModel<Order>>> listOrders() {
    List<Order> oredenes = orderService.getOrders();
    if (oredenes.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(assembler.toCollectionModel(oredenes));
  }

  @PostMapping()
  @Operation(summary = "Añade una nueva Orden", description = "Agrega una nueva orden a la base de datos")
  @ApiResponse(responseCode = "200", description = "Operación exitosa")
  public ResponseEntity<EntityModel<Order>> addOrder(
      @Parameter(description = "Objeto Order a crear", required = true, schema = @Schema(implementation = Order.class)) @RequestBody Order order) {
    order.setStatus(OrderStatus.CREATED);
    order.setCreatedAt(new Date());
    order.setOrderDate(new Date());
    order.setPaymentStatus(PaymentStatus.PENDING);

    if (order.getItems() == null || order.getItems().isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    Order result = orderService.addOrder(order);

    return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(result));

  }

  @GetMapping("{id}")
  @Operation(summary = "Obtener una Orden por id", description = "Obtiene una orden por su id")
  @ApiResponse(responseCode = "200", description = "Operación exitosa")
  public ResponseEntity<EntityModel<Order>> getOrderById(
      @Parameter(description = "Id de la orden a obtener", required = true) @PathVariable Long id) {
    try {
      Order order = orderService.getOrderById(id);
      return ResponseEntity.ok(assembler.toModel(order));
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  @Operation(summary = "Actualiza una orden", description = "Actualiza una orden por su id")
  @ApiResponse(responseCode = "200", description = "Operación exitosa")
  public ResponseEntity<EntityModel<Order>> updateOrder(
      @Parameter(description = "Id de la orden a actualizar", required = true) @PathVariable Long id,
      @Parameter(description = "Objeto Order a actualizar", required = true, schema = @Schema(implementation = Order.class)) @RequestBody Order updatedOrder) {
    try {
      Order order = orderService.updateOrder(id, updatedOrder);
      return ResponseEntity.ok(assembler.toModel(order));
    } catch (RuntimeException e) {
      if (e instanceof IllegalStateException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("{id}")
  @Operation(summary = "Elimina una orden", description = "Elimina una orden por su id")
  @ApiResponse(responseCode = "200", description = "Operación exitosa")
  public ResponseEntity<Void> deleteOrderById(
      @Parameter(description = "Id de la orden a eliminar", required = true) @PathVariable Long id) {
    try {
      orderService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/client/{userId}")
  @Operation(summary = "Obtener Ordenes por cliente", description = "Obtiene lista de ordenes que pertenecen a un cliente")
  @ApiResponse(responseCode = "200", description = "Operación exitosa")
  public ResponseEntity<CollectionModel<EntityModel<Order>>> getOrdersByUserId(
      @Parameter(description = "Id del usuario del cual se desea obtener la lista de sus ordenes", required = true) @PathVariable String userId) {
    List<Order> orders = orderService.getOrdersByUserId(userId);
    if (orders.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(assembler.toCollectionModel(orders));
  }

  @PostMapping("/{orderId}/returns")
  @Operation(summary = "Agregar devolución", description = "Agrega una devolución a una orden ya existente")
  @ApiResponse(responseCode = "200", description = "Operación exitosa")
  public ResponseEntity<OrderReturn> addReturn(
      @Parameter(description = "Id de la orden a la cual se le desea agregar una devolución", required = true) @PathVariable Long orderId,
      @Parameter(description = "Objeto OrderReturn a crear", required = true, schema = @Schema(implementation = OrderReturn.class)) @RequestBody OrderReturn orderReturn) {
    orderReturn.setStatus(ReturnStatus.REQUESTED);
    OrderReturn createdReturn = orderReturnService.addReturn(orderId, orderReturn);
    if (createdReturn == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createdReturn);
  }

  @PutMapping("/{id}/cancellation")
  @Operation(summary = "Cancelar una orden", description = "Cancelar una orden ya creada")
  @ApiResponse(responseCode = "200", description = "Operación exitosa")
  public ResponseEntity<EntityModel<Order>> cancelOrder(
      @Parameter(description = "Id de la orden a cancelar", required = true) @PathVariable Long id) {
    Order canceledOrder = orderService.cancelOrder(id);
    if (canceledOrder != null) {
      return ResponseEntity.ok(assembler.toModel(canceledOrder));
    }
    return ResponseEntity.notFound().build();
  }

}
