package com.Alencar.demo.model.enums;

public enum TransactionType {
    REVENUE("Receita"),
    EXPENSE("Despesa");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
