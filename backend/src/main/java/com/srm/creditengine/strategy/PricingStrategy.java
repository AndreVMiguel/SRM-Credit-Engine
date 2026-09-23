package com.srm.creditengine.strategy;
import com.srm.creditengine.entity.ReceivableType; import java.math.BigDecimal;
public interface PricingStrategy { ReceivableType supports(); BigDecimal monthlySpread(); }
