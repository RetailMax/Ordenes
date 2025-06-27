package com.retailmax.ordenes.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retailmax.ordenes.controller.OrderController;
import com.retailmax.ordenes.model.order.Order;
import com.retailmax.ordenes.model.order.OrderAddress;
import com.retailmax.ordenes.model.order.OrderItem;
import com.retailmax.ordenes.model.payment.PaymentStatus;
import com.retailmax.ordenes.model.returns.OrderReturn;
import com.retailmax.ordenes.model.returns.ReturnStatus;
import com.retailmax.ordenes.model.status.OrderStatus;
import com.retailmax.ordenes.services.OrderReturnService;
import com.retailmax.ordenes.services.OrderService;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderReturnService orderReturnService;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setUserId("user1");
        order.setOrderDate(new Date());
        order.setTotalAmount(15000.0);
        order.setShippingFee(500.0);
        order.setPaymentId("pay-123");
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());

        OrderAddress address = new OrderAddress();
        address.setStreet("Av. Principal 123");
        address.setCity("Santiago");
        address.setRegion("RM");
        address.setPostalCode("8320000");
        order.setShippingAddress(address);

        OrderItem item = new OrderItem();
        item.setId(1L);
        item.setProductId("1");
        item.setProductName("product");
        item.setSku("sku");
        item.setQuantity(2);
        item.setUnitPrice(1.0);
        order.setItems(List.of(item));
    }

    @Test
    void testListOrders() throws Exception {

        when(orderService.getOrders()).thenReturn(List.of(order));

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value("user1"));

    }

    @Test
    void testAddOrder() throws Exception {

        when(orderService.addOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value("user1"));
    }

    @Test
    void testGetOrderById() throws Exception {

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value("user1"));
    }

    @Test
    void testUpdateOrder() throws Exception {

        when(orderService.updateOrder(eq(1L), any(Order.class))).thenReturn(order);
        mockMvc.perform(put("/api/v1/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value("user1"));
    }

    @Test
    void testDeleteOrderById() throws Exception {

        doNothing().when(orderService).delete(1L);

        mockMvc.perform(delete("/api/v1/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).delete(1L);

    }

    @Test
    void testGetOrdersByUserId() throws Exception {

        when(orderService.getOrdersByUserId("user1")).thenReturn(List.of(order));

        mockMvc.perform(get("/api/v1/orders/client/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value("user1"));
    }

    @Test
    void testAddReturn() throws Exception{
     OrderReturn orderReturn = new OrderReturn();
    orderReturn.setId(1L);
    orderReturn.setOrder(new Order()); 
    orderReturn.getOrder().setId(1L);
    orderReturn.setReturnType("DEVOLUCION");
    orderReturn.setReason("Producto defectuoso");
    orderReturn.setProcessedby(null);
    orderReturn.setProcessedAt(null);
    orderReturn.setRefundAmount(1000.0);

        when(orderReturnService.addReturn(eq(1L), any(OrderReturn.class))).thenReturn(orderReturn);

        mockMvc.perform(post("/api/v1/orders/1/returns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderReturn)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").value(1))
                        .andExpect(jsonPath("$.reason").value("Producto defectuoso"));
                        
                        
    }

    @Test
    void testCancelOrder() throws Exception{

        when(orderService.cancelOrder(1L)).thenReturn(order);

        mockMvc.perform(put("/api/v1/orders/1/cancellation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CREATED"));

    }

}
