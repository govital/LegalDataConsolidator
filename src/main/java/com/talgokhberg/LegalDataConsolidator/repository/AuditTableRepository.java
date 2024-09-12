package com.talgokhberg.LegalDataConsolidator.repository;

import com.talgokhberg.LegalDataConsolidator.dto.AuditTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditTableRepository extends JpaRepository<AuditTable, String> {
}

