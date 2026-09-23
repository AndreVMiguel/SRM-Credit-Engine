package com.srm.creditengine.dto;
import jakarta.validation.Valid; import jakarta.validation.constraints.*; import java.util.UUID;
public record SettlementRequest(@NotBlank @Size(max=80) String idempotencyKey,@NotNull UUID assignorId,@NotNull @Valid PricingRequest pricing) {}
