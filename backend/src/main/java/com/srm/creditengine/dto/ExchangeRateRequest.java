package com.srm.creditengine.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
public record ExchangeRateRequest(@NotBlank @Pattern(regexp="(?i)^(BRL|USD)$",message="must be BRL or USD") String baseCurrency,@NotBlank @Pattern(regexp="(?i)^(BRL|USD)$",message="must be BRL or USD") String quoteCurrency,@NotNull @DecimalMin(value="0.00000001") BigDecimal rate,@NotNull Instant effectiveAt) {}
