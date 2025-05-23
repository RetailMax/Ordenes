package com.retailmax.ordenes.model.returns;

public enum ReturnStatus {
    REQUESTED("Solicitado"),
    IN_REVIEW("En Revisión"),
    APPROVED("Aprobado"),
    REJECTED("Rechazado"),
    COMPLETED("Completado");

    private final String description;

    ReturnStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}