package com.talgokhberg.LegalDataConsolidator.repository;

import com.talgokhberg.LegalDataConsolidator.dto.ConsolidatedLegalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsolidatedLegalDataRepository extends JpaRepository<ConsolidatedLegalData, String> {
}

