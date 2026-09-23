package com.srm.creditengine.dto;
import com.srm.creditengine.entity.ReceivableType; import jakarta.validation.constraints.*; import java.math.BigDecimal; import java.time.LocalDate;
public record PricingRequest(@NotNull @DecimalMin("0.01") BigDecimal faceValue,@NotNull LocalDate dueDate,@NotNull ReceivableType receivableType,@NotBlank @Pattern(regexp="(?i)^(BRL|USD)$",message="must be BRL or USD") String assetCurrency,@NotBlank @Pattern(regexp="(?i)^(BRL|USD)$",message="must be BRL or USD") String settlementCurrency,@NotNull @DecimalMin("0.0") BigDecimal baseRateMonthly) {}
