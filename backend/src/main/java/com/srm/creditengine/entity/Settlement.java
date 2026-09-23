package com.srm.creditengine.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

@Entity @Table(name="settlements")
public class Settlement {
    @Id private UUID id;
    @Column(nullable=false,unique=true,length=80) private String idempotencyKey;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="assignor_id",nullable=false) private Assignor assignor;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="receivable_type",nullable=false) private ReceivableProduct receivableProduct;
    @Column(nullable=false,precision=19,scale=4) private BigDecimal faceValue;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="asset_currency",nullable=false) private Currency assetCurrency;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="settlement_currency",nullable=false) private Currency settlementCurrency;
    @Column(nullable=false) private LocalDate dueDate;
    @Column(nullable=false,precision=12,scale=8) private BigDecimal baseRateMonthly;
    @Column(nullable=false,precision=12,scale=8) private BigDecimal spreadMonthly;
    @Column(precision=19,scale=8) private BigDecimal exchangeRate;
    @Column(nullable=false,precision=19,scale=4) private BigDecimal netAmount;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private SettlementStatus status;
    @Column(nullable=false) private Instant createdAt;
    @Version private long version;
    protected Settlement() {}
    public Settlement(String key,Assignor assignor,ReceivableProduct product,BigDecimal face,Currency assetCurrency,Currency settlementCurrency,LocalDate dueDate,BigDecimal baseRate,BigDecimal spread,BigDecimal exchangeRate,BigDecimal netAmount){id=UUID.randomUUID();idempotencyKey=key;this.assignor=assignor;receivableProduct=product;faceValue=face;this.assetCurrency=assetCurrency;this.settlementCurrency=settlementCurrency;this.dueDate=dueDate;baseRateMonthly=baseRate;spreadMonthly=spread;this.exchangeRate=exchangeRate;this.netAmount=netAmount;status=SettlementStatus.SETTLED;createdAt=Instant.now();}
    public UUID getId(){return id;} public String getIdempotencyKey(){return idempotencyKey;} public UUID getAssignorId(){return assignor.getId();} public ReceivableType getReceivableType(){return receivableProduct.getType();} public BigDecimal getFaceValue(){return faceValue;} public String getAssetCurrency(){return assetCurrency.getCode();} public String getSettlementCurrency(){return settlementCurrency.getCode();} public LocalDate getDueDate(){return dueDate;} public BigDecimal getBaseRateMonthly(){return baseRateMonthly;} public BigDecimal getSpreadMonthly(){return spreadMonthly;} public BigDecimal getExchangeRate(){return exchangeRate;} public BigDecimal getNetAmount(){return netAmount;} public SettlementStatus getStatus(){return status;} public Instant getCreatedAt(){return createdAt;}
}
