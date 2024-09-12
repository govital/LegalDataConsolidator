package com.talgokhberg.LegalDataConsolidator.dto;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_table")
public class AuditTable {

    @Id
    private String ddid;
    private String columnName;
    private String source;

    // getters and setters
}

