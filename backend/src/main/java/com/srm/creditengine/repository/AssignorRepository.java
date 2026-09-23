package com.srm.creditengine.repository;

import com.srm.creditengine.entity.Assignor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AssignorRepository extends JpaRepository<Assignor, UUID> {}
