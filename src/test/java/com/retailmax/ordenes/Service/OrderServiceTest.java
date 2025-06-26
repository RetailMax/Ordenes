package com.retailmax.ordenes.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.retailmax.ordenes.model.order.Order;
import com.retailmax.ordenes.model.order.OrderAddress;
import com.retailmax.ordenes.model.order.OrderItem;
import com.retailmax.ordenes.model.status.OrderStatus;
import com.retailmax.ordenes.repository.OrderRepository;
import com.retailmax.ordenes.services.OrderService;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

 
   @Test
    void testGetOrders(){

        Order order = new Order(1L,"luisa01",new Date(),15000.0,500.0,"pay123",null,null,new Date(),new Date(),List.of(),List.of(),OrderStatus.PROCESSING,List.of(),List.of());

        when(orderRepository.findAll()).thenReturn(List.of (order));

        List<Order> orders = orderService.getOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getId());

    }

    @Test
    void testAddOrder(){
        Order order = new Order(1L,"luisa01",new Date(),500.0,500.0,"pay123",null,null,new Date(),new Date(),List.of(),List.of(),OrderStatus.PROCESSING,List.of(),List.of());

        when(orderRepository.save(order)).thenReturn(order);

        Order addOrder = orderService.addOrder(order);

        assertNotNull(addOrder);
        assertEquals(500.0,addOrder.getTotalAmount());

        assertNotNull(addOrder.getStatusHistory());
        assertTrue(addOrder.getStatusHistory().isEmpty());

        assertNotNull(addOrder.getPaymentEvents());
        assertTrue(addOrder.getPaymentEvents().isEmpty());


        verify(orderRepository).save(order);
    }

    @Test 
    void testGetOrderById(){
       
        Order order = new Order(1L,"luisa01",new Date(),500.0,500.0,"pay123",null,null,new Date(),new Date(),List.of(),List.of(),OrderStatus.PROCESSING,List.of(),List.of());


        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        Order found = orderService.getOrderById(order.getId());

        assertNotNull(found);
        assertEquals(1L,found.getId());

    }

    @Test
    void testDelete(){
        Long id = 1L;

        doNothing().when(orderRepository).deleteById(id);

        orderService.delete(id);

        verify(orderRepository, times(1)).deleteById(id);

    }

     @Test 
    void testGetOrderByUserId(){
        ArrayList<Order> orders = new ArrayList<>();
        Order order1 = new Order(1L,"luisa01",new Date(),15000.0,500.0,"pay123",null,null,new Date(),new Date(),List.of(),List.of(),OrderStatus.PROCESSING,List.of(),List.of());
        Order order2 = new Order(2L,"luisa02",new Date(),15000.0,500.0,"pay123",null,null,new Date(),new Date(),List.of(),List.of(),OrderStatus.PROCESSING,List.of(),List.of());
        orders.add(order1);
        orders.add(order2);

        when(orderRepository.findByUserId("luisa01")).thenReturn(List.of(order1));

        List<Order> result = orderService.getOrdersByUserId("luisa01");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("luisa01", result.get(0).getUserId());
    
    }

    @Test
    void testUpdateOrder(){
        Long id = 1L;
        Order order = new Order(id,"luisa01",new Date(),15000.0,500.0,"pay123",null,null,new Date(),new Date(),new ArrayList<>(),new ArrayList<>(),OrderStatus.PROCESSING,new ArrayList<>(),new ArrayList<>());
       
        OrderAddress newAddress = new OrderAddress("calle","santiago", "rm", "00000");
        Order newOrder = new Order(1L,"luisa01",new Date(),15000.0,500.0,"pay123",newAddress,null,new Date(),new Date(),new ArrayList<>(),new ArrayList<>(),OrderStatus.SHIPPED,new ArrayList<>(),new ArrayList<>());   
        
        OrderItem newItem = new OrderItem();
        newItem.setId(1l);
        newItem.setOrder(newOrder);
        newItem.setQuantity(2);
        newItem.setUnitPrice(100.0);

        List<OrderItem> items = new ArrayList<>();
        items.add(newItem);
        newOrder.setItems(items);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);
        
        Order updateOrder = orderService.updateOrder(id, newOrder);

        assertNotNull(updateOrder);
        assertEquals(OrderStatus.SHIPPED,updateOrder.getStatus());
        assertEquals("calle", updateOrder.getShippingAddress().getStreet());
        
        

   
        
        
    }

    @Test
    void testCancelOrder(){
        Order order = new Order(1L,"luisa01",new Date(),15000.0,500.0,"pay123",null,null,new Date(),new Date(),new ArrayList<>(),new ArrayList<>(),OrderStatus.PROCESSING,new ArrayList<>(),new ArrayList<>());
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order canceledOrder = orderService.cancelOrder(1L);

        assertNotNull(canceledOrder);
        assertEquals(OrderStatus.CANCELLED, canceledOrder.getStatus());

    }



    @Test
    void calculateTotalTest(){
        // Creamos una instancia de orden porque el metodo la requiere como parametro
        Order order = new Order();
        // Generamos items de orden para ser agregados a una lista de este tipo
        OrderItem orderItem = new OrderItem(1L,order,"1", "product", "sku",1,1.0);
        OrderItem orderItem2 = new OrderItem(1L,order,"1", "product", "sku",2,2.0);
        // Declaramos la lista vacia
        List<OrderItem> listItems = new ArrayList<>();
        // Le asignamos los items creados a la lista
        listItems.add(orderItem);
        listItems.add(orderItem2);
        // Seteamos el atributo items, que es una lista de OrderItem, con la lista de ordenes que creamos y cargamos anteriormente
        order.setItems(listItems);
        // De la misma forma, seteamos el shippingFee
        order.setShippingFee(1.0);
        // Teniendo todos estos seteos, ya podemos probar todo lo que comprende el core de la función


        // Llamamos a nuestro metodo a probar
        orderService.calculateTotal(order);

        // Segun el funcionamiento del metodo, este va a asignar el valor del total al atributo de TotalAmount de la orden
        // Por lo cual, para poder hacer el assert, debemos llamar ese atibuto de la instancia de Orden
        assertEquals(6, order.getTotalAmount());

    }

}
