package com.retailmax.ordenes.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.retailmax.ordenes.model.order.Order;
import com.retailmax.ordenes.model.order.OrderItem;
import com.retailmax.ordenes.repository.OrderRepository;
import com.retailmax.ordenes.services.OrderService;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

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
