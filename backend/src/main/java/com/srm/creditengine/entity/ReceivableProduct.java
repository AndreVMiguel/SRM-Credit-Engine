package com.srm.creditengine.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "receivable_products")
public class ReceivableProduct {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private ReceivableType type;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 12, scale = 8)
    private BigDecimal monthlySpread;

    @Column(nullable = false)
    private boolean active;

    protected ReceivableProduct() {
    }

    public ReceivableType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMonthlySpread() {
        return monthlySpread;
    }

    public boolean isActive() {
        return active;
    }
}
