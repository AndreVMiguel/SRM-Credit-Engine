package com.srm.creditengine.service;

import com.srm.creditengine.dto.*;
import com.srm.creditengine.entity.ReceivableType;
import com.srm.creditengine.exception.BusinessException;
import com.srm.creditengine.strategy.PricingStrategy;
import org.springframework.stereotype.Service;

import java.math.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PricingService {
    private static final MathContext MC = new MathContext(18, RoundingMode.HALF_EVEN);
    private final Map<ReceivableType, PricingStrategy> strategies;
    private final ExchangeRateService exchangeRates;

    public PricingService(List<PricingStrategy> strategies, ExchangeRateService exchangeRates) {
        this.strategies = strategies.stream().collect(Collectors.toUnmodifiableMap(PricingStrategy::supports, Function.identity()));
        this.exchangeRates = exchangeRates;
    }

    public PricingResponse calculate(PricingRequest r) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(ZoneOffset.UTC), r.dueDate());
        if (days < 0) throw new BusinessException("Due date cannot be earlier than today");
        var strategy = Optional.ofNullable(strategies.get(r.receivableType())).orElseThrow(() -> new BusinessException("Unsupported receivable type"));
        var spread = strategy.monthlySpread();
        double months = days / 30.0;
        double factor = Math.pow(BigDecimal.ONE.add(r.baseRateMonthly()).add(spread).doubleValue(), months);
        var present = r.faceValue().divide(BigDecimal.valueOf(factor), MC).setScale(4, RoundingMode.HALF_EVEN);
        var fx = exchangeRates.findRate(r.assetCurrency(), r.settlementCurrency(), Instant.now());
        var net = present.multiply(fx, MC).setScale(4, RoundingMode.HALF_EVEN);
        return new PricingResponse(r.faceValue(), present, net, r.baseRateMonthly(), spread, fx, days);
    }
}
