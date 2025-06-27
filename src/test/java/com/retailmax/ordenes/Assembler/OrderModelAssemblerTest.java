package com.retailmax.ordenes.Assembler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import com.retailmax.ordenes.assemblers.OrderModelAssembler;
import com.retailmax.ordenes.controller.OrderController;
import com.retailmax.ordenes.model.order.Order;
import com.retailmax.ordenes.model.order.OrderAddress;
import com.retailmax.ordenes.model.order.OrderItem;
import com.retailmax.ordenes.model.payment.PaymentStatus;
import com.retailmax.ordenes.model.status.OrderStatus;

class OrderModelAssemblerTest {

    private OrderModelAssembler assembler;
    private Order order;

    @BeforeEach
    void setUp() {
        assembler = new OrderModelAssembler();
        
        order = new Order();
        order.setId(1L);
        order.setUserId("user1");
        order.setOrderDate(new Date());
        order.setTotalAmount(100.0);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setStatus(OrderStatus.CREATED);
        
        OrderAddress address = new OrderAddress();
        address.setStreet("Test Street");
        address.setCity("Test City");
        order.setShippingAddress(address);

        OrderItem item = new OrderItem();
        item.setId(1L);
        item.setProductId("prod1");
        order.setItems(List.of(item));
    }

    @Test
    void toModel_ShouldAddExpectedLinks() {
        EntityModel<Order> model = assembler.toModel(order);

        assertThat(model.getContent()).isEqualTo(order);
        assertThat(model.getLinks())
            .hasSize(7)
            .containsExactlyInAnyOrder(
                linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).listOrders()).withRel("orders"),
                linkTo(methodOn(OrderController.class).updateOrder(order.getId(), null)).withRel("update"),
                linkTo(methodOn(OrderController.class).deleteOrderById(order.getId())).withRel("delete"),
                linkTo(methodOn(OrderController.class).cancelOrder(order.getId())).withRel("cancel"),
                linkTo(methodOn(OrderController.class).addReturn(order.getId(), null)).withRel("create-return"),
                linkTo(methodOn(OrderController.class).getOrdersByUserId(order.getUserId())).withRel("user-orders")
            );
    }

    @Test
    void toCollectionModel_ShouldAddExpectedLinks() {
        List<Order> orders = List.of(order);
        CollectionModel<EntityModel<Order>> model = assembler.toCollectionModel(orders);

        assertThat(model.getLinks())
            .hasSize(2)
            .containsExactlyInAnyOrder(
                linkTo(methodOn(OrderController.class).listOrders()).withSelfRel(),
                linkTo(methodOn(OrderController.class).addOrder(null)).withRel("create-order")
            );

        assertThat(model.getContent())
            .hasSize(1)
            .first()
            .satisfies(entityModel -> {
                assertThat(entityModel.getContent()).isEqualTo(order);
                assertThat(entityModel.getLinks()).hasSize(7);
            });
    }
}
