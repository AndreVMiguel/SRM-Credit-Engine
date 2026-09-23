package com.srm.creditengine.repository;
import com.srm.creditengine.entity.Settlement; import org.springframework.data.jpa.repository.*; import org.springframework.data.jpa.domain.Specification; import java.util.*;
public interface SettlementRepository extends JpaRepository<Settlement,UUID>, JpaSpecificationExecutor<Settlement> { Optional<Settlement> findByIdempotencyKey(String key); }
