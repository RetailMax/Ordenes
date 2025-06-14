package com.retailmax.ordenes.model.status;


public enum OrderStatus {
    CREATED("Creada"),
    PROCESSING("En Proceso"),
    SHIPPED("Enviada"),
    CANCELLED("Cancelada"),
    COMPLETED("Completada");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}