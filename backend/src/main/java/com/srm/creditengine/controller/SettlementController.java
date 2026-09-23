package com.srm.creditengine.controller;
import com.srm.creditengine.dto.*; import com.srm.creditengine.entity.ReceivableType; import com.srm.creditengine.service.SettlementService; import jakarta.validation.Valid; import org.springdoc.core.annotations.ParameterObject; import org.springframework.data.domain.*; import org.springframework.data.web.PageableDefault; import org.springframework.format.annotation.DateTimeFormat; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.time.Instant;
@RestController @RequestMapping("/api/v1/settlements")
public class SettlementController {
    private final SettlementService service; public SettlementController(SettlementService service){this.service=service;}
    @PostMapping public ResponseEntity<SettlementResponse> settle(@Valid @RequestBody SettlementRequest request){return ResponseEntity.status(HttpStatus.CREATED).body(service.settle(request));}
    @GetMapping public Page<SettlementResponse> statement(@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Instant from,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) Instant to,@RequestParam(required=false) String currency,@RequestParam(required=false) ReceivableType receivableType,@ParameterObject @PageableDefault(size=20,sort="createdAt",direction=Sort.Direction.DESC) Pageable pageable){return service.statement(from,to,currency,receivableType,pageable);}
}
