package com.srm.creditengine.strategy;
import com.srm.creditengine.entity.ReceivableType; import org.springframework.stereotype.Component; import java.math.BigDecimal;
@Component public class MercantileDuplicatePricingStrategy implements PricingStrategy { public ReceivableType supports(){return ReceivableType.MERCANTILE_DUPLICATE;} public BigDecimal monthlySpread(){return new BigDecimal("0.015");} }
