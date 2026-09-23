package com.srm.creditengine.service;

import com.srm.creditengine.dto.*;
import com.srm.creditengine.entity.ExchangeRate;
import com.srm.creditengine.exception.BusinessException;
import com.srm.creditengine.repository.CurrencyRepository;
import com.srm.creditengine.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.*;
import java.time.Instant;

@Service
public class ExchangeRateService {
    private final ExchangeRateRepository repository;
    private final CurrencyRepository currencyRepository;

    public ExchangeRateService(ExchangeRateRepository repository, CurrencyRepository currencyRepository) {
        this.repository = repository;
        this.currencyRepository = currencyRepository;
    }

    @Transactional
    public ExchangeRateResponse create(ExchangeRateRequest r) {
        String base = r.baseCurrency().toUpperCase(), quote = r.quoteCurrency().toUpperCase();
        if (base.equals(quote)) throw new BusinessException("Currencies must be different");
        var baseCurrency = currencyRepository.findById(base).orElseThrow(() -> new BusinessException("Unsupported currency: " + base));
        var quoteCurrency = currencyRepository.findById(quote).orElseThrow(() -> new BusinessException("Unsupported currency: " + quote));
        return map(repository.save(new ExchangeRate(baseCurrency, quoteCurrency, r.rate(), r.effectiveAt())));
    }

    @Transactional(readOnly = true)
    public BigDecimal findRate(String base, String quote, Instant at) {
        String normalizedBase = base.toUpperCase(), normalizedQuote = quote.toUpperCase();
        if (normalizedBase.equals(normalizedQuote)) return BigDecimal.ONE;
        var direct = repository.findFirstByBaseCurrencyCodeAndQuoteCurrencyCodeAndEffectiveAtLessThanEqualOrderByEffectiveAtDesc(normalizedBase, normalizedQuote, at);
        if (direct.isPresent()) return direct.get().getRate();
        var inverse = repository.findFirstByBaseCurrencyCodeAndQuoteCurrencyCodeAndEffectiveAtLessThanEqualOrderByEffectiveAtDesc(normalizedQuote, normalizedBase, at).orElseThrow(() -> new BusinessException("Exchange rate not found for " + normalizedBase + "/" + normalizedQuote));
        return BigDecimal.ONE.divide(inverse.getRate(), 8, RoundingMode.HALF_EVEN);
    }

    private ExchangeRateResponse map(ExchangeRate e) {
        return new ExchangeRateResponse(e.getId(), e.getBaseCurrency(), e.getQuoteCurrency(), e.getRate(), e.getEffectiveAt());
    }
}
