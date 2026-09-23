package com.srm.creditengine.dto;

import java.math.BigDecimal;

public record PricingResponse(BigDecimal faceValue, BigDecimal presentValue, BigDecimal netAmount,
                              BigDecimal baseRateMonthly, BigDecimal spreadMonthly, BigDecimal exchangeRate,
                              long termDays) {
}
