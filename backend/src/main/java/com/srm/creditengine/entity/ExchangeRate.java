package com.srm.creditengine.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="exchange_rates")
public class ExchangeRate {
    @Id private UUID id;
    @Column(nullable=false,length=3) private String baseCurrency;
    @Column(nullable=false,length=3) private String quoteCurrency;
    @Column(nullable=false,precision=19,scale=8) private BigDecimal rate;
    @Column(nullable=false) private Instant effectiveAt;
    protected ExchangeRate() {}
    public ExchangeRate(String baseCurrency,String quoteCurrency,BigDecimal rate,Instant effectiveAt){this.id=UUID.randomUUID();this.baseCurrency=baseCurrency;this.quoteCurrency=quoteCurrency;this.rate=rate;this.effectiveAt=effectiveAt;}
    public UUID getId(){return id;} public String getBaseCurrency(){return baseCurrency;} public String getQuoteCurrency(){return quoteCurrency;} public BigDecimal getRate(){return rate;} public Instant getEffectiveAt(){return effectiveAt;}
}
