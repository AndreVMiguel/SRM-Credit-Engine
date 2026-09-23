package com.srm.creditengine.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ExchangeRateResponse(UUID id, String baseCurrency, String quoteCurrency, BigDecimal rate,
                                   Instant effectiveAt) {
}
