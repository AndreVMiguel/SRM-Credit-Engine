package com.srm.creditengine.dto;

import com.srm.creditengine.entity.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

public record SettlementResponse(UUID id, UUID assignorId, ReceivableType receivableType, BigDecimal faceValue,
                                 String assetCurrency, String settlementCurrency, LocalDate dueDate,
                                 BigDecimal netAmount, SettlementStatus status, Instant createdAt) {
}
