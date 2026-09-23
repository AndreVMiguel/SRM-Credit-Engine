package com.srm.creditengine.controller;

import com.srm.creditengine.dto.*;
import com.srm.creditengine.service.PricingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pricing")
public class PricingController {
    private final PricingService service;

    public PricingController(PricingService service) {
        this.service = service;
    }

    @PostMapping("/simulations")
    public PricingResponse simulate(@Valid @RequestBody PricingRequest request) {
        return service.calculate(request);
    }
}
