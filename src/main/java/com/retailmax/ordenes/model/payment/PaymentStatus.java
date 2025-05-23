package com.retailmax.ordenes.model.payment;

public enum PaymentStatus {
    PENDING("Pendiente"),
    AUTHORIZED("Autorizado"),
    CAPTURED("Capturado"),
    FAILED("Fallido"),
    REFUNDED("Reembolsado");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}