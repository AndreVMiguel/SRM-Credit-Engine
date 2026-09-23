package com.srm.creditengine.service;

import com.srm.creditengine.dto.*;
import com.srm.creditengine.entity.*;
import com.srm.creditengine.repository.SettlementRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class SettlementService {
    private final SettlementRepository repository;
    private final PricingService pricingService;

    public SettlementService(SettlementRepository repository, PricingService pricingService) {
        this.repository = repository;
        this.pricingService = pricingService;
    }

    @Transactional
    public SettlementResponse settle(SettlementRequest r) {
        var existing = repository.findByIdempotencyKey(r.idempotencyKey());
        if (existing.isPresent()) return map(existing.get());
        var p = pricingService.calculate(r.pricing());
        var q = r.pricing();
        try {
            return map(repository.saveAndFlush(new Settlement(r.idempotencyKey(), r.assignorId(), q.receivableType(), q.faceValue(), q.assetCurrency().toUpperCase(), q.settlementCurrency().toUpperCase(), q.dueDate(), q.baseRateMonthly(), p.spreadMonthly(), p.exchangeRate(), p.netAmount())));
        } catch (DataIntegrityViolationException ex) {
            return repository.findByIdempotencyKey(r.idempotencyKey()).map(this::map).orElseThrow(() -> ex);
        }
    }

    @Transactional(readOnly = true)
    public Page<SettlementResponse> statement(Instant from, Instant to, String currency, ReceivableType receivableType, Pageable pageable) {
        var allowedSorts = Set.of("createdAt", "netAmount", "dueDate", "faceValue", "settlementCurrency", "receivableType");
        pageable.getSort().forEach(order -> {
            if (!allowedSorts.contains(order.getProperty()))
                throw new IllegalArgumentException("Invalid sort property: " + order.getProperty());
        });
        Specification<Settlement> spec = (root, q, cb) -> {
            List<Predicate> p = new ArrayList<>();
            if (from != null) p.add(cb.greaterThanOrEqualTo(root.get("createdAt"), from));
            if (to != null) p.add(cb.lessThan(root.get("createdAt"), to));
            if (currency != null && !currency.isBlank())
                p.add(cb.equal(root.get("settlementCurrency"), currency.toUpperCase()));
            if (receivableType != null) p.add(cb.equal(root.get("receivableType"), receivableType));
            return cb.and(p.toArray(Predicate[]::new));
        };
        return repository.findAll(spec, pageable).map(this::map);
    }

    private SettlementResponse map(Settlement s) {
        return new SettlementResponse(s.getId(), s.getAssignorId(), s.getReceivableType(), s.getFaceValue(), s.getAssetCurrency(), s.getSettlementCurrency(), s.getDueDate(), s.getNetAmount(), s.getStatus(), s.getCreatedAt());
    }
}
