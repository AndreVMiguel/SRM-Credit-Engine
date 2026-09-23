package com.srm.creditengine.controller;
import com.srm.creditengine.dto.*; import com.srm.creditengine.service.ExchangeRateService; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/exchange-rates")
public class ExchangeRateController { private final ExchangeRateService service; public ExchangeRateController(ExchangeRateService service){this.service=service;} @PostMapping public ResponseEntity<ExchangeRateResponse> create(@Valid @RequestBody ExchangeRateRequest request){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));} }
