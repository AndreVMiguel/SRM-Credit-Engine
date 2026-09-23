package com.srm.creditengine.repository;

import com.srm.creditengine.entity.ReceivableProduct;
import com.srm.creditengine.entity.ReceivableType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivableProductRepository extends JpaRepository<ReceivableProduct, ReceivableType> {}
