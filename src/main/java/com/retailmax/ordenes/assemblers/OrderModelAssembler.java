package com.retailmax.ordenes.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.retailmax.ordenes.controller.OrderController;
import com.retailmax.ordenes.model.order.Order;


@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {
    @Override
    public EntityModel<Order> toModel(Order order) {
        EntityModel<Order> orderModel = EntityModel.of(order);

        // Enlaces básicos para todas las operaciones CRUD
        orderModel.add(
                linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).listOrders()).withRel("orders"),
                linkTo(methodOn(OrderController.class).updateOrder(order.getId(), null)).withRel("update"),
                linkTo(methodOn(OrderController.class).deleteOrderById(order.getId())).withRel("delete"));

        // Enlaces para operaciones específicas
        orderModel.add(
                linkTo(methodOn(OrderController.class).cancelOrder(order.getId())).withRel("cancel"),
                linkTo(methodOn(OrderController.class).addReturn(order.getId(), null)).withRel("create-return"),
                linkTo(methodOn(OrderController.class).getOrdersByUserId(order.getUserId())).withRel("user-orders"));

        return orderModel;
    }

    @Override
    public CollectionModel<EntityModel<Order>> toCollectionModel(Iterable<? extends Order> entities) {
        List<EntityModel<Order>> orderList = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orderList,
                linkTo(methodOn(OrderController.class).listOrders()).withSelfRel(),
                linkTo(methodOn(OrderController.class).addOrder(null)).withRel("create-order"));
    }
}
