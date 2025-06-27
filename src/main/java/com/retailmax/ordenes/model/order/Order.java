package com.retailmax.ordenes.model.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.retailmax.ordenes.model.payment.PaymentEvent;
import com.retailmax.ordenes.model.payment.PaymentStatus;
import com.retailmax.ordenes.model.returns.OrderReturn;
import com.retailmax.ordenes.model.status.OrderStatus;
import com.retailmax.ordenes.model.status.OrderStatusHistory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una orden de compra", 
        name = "Order",
        title = "Orden de Compra")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "order_id")
    @Schema(description = "Identificador único de la orden", 
    example = "1", 
    required = true)
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "ID del usuario que realizó la orden", 
    example = "USER123", 
    required = true)
    private String userId;

    @Column(name = "order_date", nullable = false)
    @Schema(description = "Fecha en que se realizó la orden", 
    example = "2023-06-25T10:30:00Z", 
    required = true)
    private Date orderDate;

    @Column(name = "total_amount")
    @Schema(description = "Monto total de la orden", 
    example = "159.99", 
    required = true)
    private Double totalAmount;

    @Column(name = "shipping_fee")
    @Schema(description = "Costo de envío de la orden", 
    example = "5.99", 
    required = false)
    private Double shippingFee;

    @Column(name = "payment_id")
    @Schema(description = "ID del pago asociado a la orden", 
    example = "PAY12345", 
    required = false)
    private String paymentId;

    @Embedded
    @Schema(description = "Dirección de envío de la orden", 
    required = true)
    private OrderAddress shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    @Schema(description = "Estado actual de la orden de pago", 
    example = "PENDING", 
    required = true,
    allowableValues = {"PENDING", "AUTHORIZED", "CAPTURED", "FAILED", "REFUNDED"})
    private PaymentStatus paymentStatus;

    @Column(name = "created_at", nullable = false)
    @Schema(description = "Fecha de creación de la orden", 
    example = "2023-06-25T10:30:00Z", 
    required = true)
    private Date createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Fecha de última actualización de la orden", 
    example = "2023-06-25T12:00:00Z", 
    required = false)
    private Date updatedAt;

    @JsonManagedReference("order-items")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Schema(description = "Lista de items incluidos en la orden",
    required = false,
    title = "Items de la orden")
    private List<OrderItem> items;

    @JsonManagedReference("order-returns")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Schema(description = "Lista de devolucionesde la orden",
    required = false,
    title = "Devoluciones de la orden")
    private List<OrderReturn> returns;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Schema(description = "Estado actual de la orden", 
    example = "CREATED", 
    required = true,
    allowableValues = {"CREATED", "PROCESSING", "SHIPPED", "CANCELLED", "COMPLETED"})
    private OrderStatus status;

    @JsonManagedReference("order-status-history")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Schema(description = "Lista del historial de estados de la orden",
    required = false,
    title = "Historial de estados de la orden")
    private List<OrderStatusHistory> statusHistory;

    @JsonManagedReference("order-payment-events")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Schema(description = "Lista de los eventos de pagos de la orden",
    required = false,
    title = "Eventos de pagos de la orden")
    private List<PaymentEvent> paymentEvents;

    public void updateStatus(OrderStatus newStatus, String changedBy) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(this);
        history.setPreviousStatus(this.status);
        history.setNewStatus(newStatus);
        history.setChangedAt(new Date());
        history.setChangedBy(changedBy);
        
        this.status = newStatus;
        this.statusHistory.add(history);
    }

    public void addItem(OrderItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        item.setOrder(this);
    }

    public void addPaymentEvent(PaymentEvent event) {
        if (paymentEvents == null) {
            paymentEvents = new ArrayList<>();
        }
        paymentEvents.add(event);
        event.setOrder(this);
    }

}