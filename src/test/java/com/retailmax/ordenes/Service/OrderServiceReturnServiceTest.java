package com.retailmax.ordenes.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.retailmax.ordenes.model.order.Order;
import com.retailmax.ordenes.model.returns.OrderReturn;
import com.retailmax.ordenes.model.returns.ReturnStatus;
import com.retailmax.ordenes.repository.OrderRepository;
import com.retailmax.ordenes.repository.OrderReturnRepository;
import com.retailmax.ordenes.services.OrderReturnService;

@ExtendWith(MockitoExtension.class)
class OrderReturnServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderReturnRepository orderReturnRepository;

    @InjectMocks
    private OrderReturnService orderReturnService;

    @Test
    void testAddReturn_Success() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        OrderReturn orderReturn = new OrderReturn();
        orderReturn.setId(1L);
        orderReturn.setReason("Producto defectuoso");
        orderReturn.setRefundAmount(100.0);
        orderReturn.setStatus(ReturnStatus.REQUESTED);
        orderReturn.setRequestedAt(new java.sql.Date(new Date().getTime()));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderReturnRepository.save(any(OrderReturn.class))).thenReturn(orderReturn);

        OrderReturn result = orderReturnService.addReturn(orderId, orderReturn);

        assertNotNull(result);
        assertEquals("Producto defectuoso", result.getReason());
        assertEquals(ReturnStatus.REQUESTED, result.getStatus());
        assertEquals(order, result.getOrder());
        verify(orderReturnRepository).save(orderReturn);
    }

    @Test
    void testAddReturn_OrderNotFound() {

        Long orderId = 1L;
        OrderReturn orderReturn = new OrderReturn();
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        OrderReturn result = orderReturnService.addReturn(orderId, orderReturn);

        assertNull(result);
        verify(orderReturnRepository, never()).save(any(OrderReturn.class));
    }

    @Test
    void testAddReturn_WithNullOrderReturn() {

        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));


        assertThrows(NullPointerException.class, () -> {
            orderReturnService.addReturn(orderId, null);
        });
        
        verify(orderReturnRepository, never()).save(any(OrderReturn.class));
    }
}