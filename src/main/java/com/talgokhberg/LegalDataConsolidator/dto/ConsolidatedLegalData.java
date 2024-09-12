package com.talgokhberg.LegalDataConsolidator.dto;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "consolidated_legal_data")
public class ConsolidatedLegalData {

    @Id
    @Column(length = 32)
    private String ddid;

    @Column
    private Date date_filed;

    @Column
    private Date date_terminated;

    @Column(length = 256)
    private String jurisdiction;

    @Column(length = 512)
    private String district;

    @Column(length = 512)
    private String case_name;

    @Column
    private Integer case_length_days;

    @Column(length = 32)
    private String court_id;

    @Column(length = 16384)
    private String allegation;

    @Column(length = 1024)
    private String damages;

    @Column(length = 1024)
    private String case_type;

    @Column(length = 2048)
    private String link;

    @Column(length = 2048)
    private String status;

    // Getters and setters for each field

    public String getDdid() {
        return ddid;
    }

    public void setDdid(String ddid) {
        this.ddid = ddid;
    }

    public Date getDate_filed() {
        return date_filed;
    }

    public void setDate_filed(Date date_filed) {
        this.date_filed = date_filed;
    }

    public Date getDate_terminated() {
        return date_terminated;
    }

    public void setDate_terminated(Date date_terminated) {
        this.date_terminated = date_terminated;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCase_name() {
        return case_name;
    }

    public void setCase_name(String case_name) {
        this.case_name = case_name;
    }

    public Integer getCase_length_days() {
        return case_length_days;
    }

    public void setCase_length_days(Integer case_length_days) {
        this.case_length_days = case_length_days;
    }

    public String getCourt_id() {
        return court_id;
    }

    public void setCourt_id(String court_id) {
        this.court_id = court_id;
    }

    public String getAllegation() {
        return allegation;
    }

    public void setAllegation(String allegation) {
        this.allegation = allegation;
    }

    public String getDamages() {
        return damages;
    }

    public void setDamages(String damages) {
        this.damages = damages;
    }

    public String getCase_type() {
        return case_type;
    }

    public void setCase_type(String case_type) {
        this.case_type = case_type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

