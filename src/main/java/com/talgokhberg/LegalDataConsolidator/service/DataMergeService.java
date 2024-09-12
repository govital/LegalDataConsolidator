package com.talgokhberg.LegalDataConsolidator.service;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class DataMergeService {
    
    //SOURCES:
    private static final String WESTLAW = "westlaw";
    private static final String COURT_LISTENERS = "courtlisteners";

    // Column numbers:
    public static final int MERGED_COLUMN_LENGTH = 13;

    public static final int DDID_COLUMN_NUM = 0;
    public static final int DATE_FILED_COLUMN_NUM = 1;
    public static final int DATE_TERMINATED_COLUMN_NUM = 2;
    public static final int JURISTRICTION_COLUMN_NUM = 3;
    public static final int DISTRICT_COLUMN_NUM = 4;
    public static final int CASE_NAME_COLUMN_NUM = 5;
    public static final int CASE_LENGHT_DAYS_COLUMN_NUM = 6;
    public static final int COURT_ID_COLUMN_NUM = 7;
    public static final int ALLEGATION_COLUMN_NUM = 8;
    public static final int DAMAGES_COLUMN_NUM = 9;
    public static final int CASE_TYPE_COLUMN_NUM = 10;
    public static final int LINK_COLUMN_NUM = 11;
    public static final int STATUS_COLUMN_NUM = 12;
    public static final String SIMPLE_DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(SIMPLE_DATE_FORMAT_PATTERN);





    public Map<String, String[]> mergeData(Map<String, String[]> vendorA, Map<String, String[]> vendorB) {
        Map<String, String[]> mergedData = new HashMap<>();

        for (String ddid : vendorA.keySet()) {
            String[] rowA = vendorA.get(ddid);
            String[] rowB = vendorB.get(ddid);
            String[] mergedRow;

            if (rowB != null) {
                mergedRow = applyRules(rowA, rowB);
            } else {
                mergedRow = rowA;  // Default to vendorA if vendorB doesn't have the record
            }
            mergedData.put(ddid, mergedRow);
        }
        return mergedData;
    }

    private String[] applyRules(String[] rowA, String[] rowB) {
        String[] mergedRow = new String[MERGED_COLUMN_LENGTH];

        mergedRow[DDID_COLUMN_NUM] = rowA[DDID_COLUMN_NUM];
        mergedRow[DATE_FILED_COLUMN_NUM] = getEarliestDate(rowA[DATE_FILED_COLUMN_NUM], rowB[DATE_FILED_COLUMN_NUM]);
        mergedRow[DATE_TERMINATED_COLUMN_NUM] = getLatestDate(rowA[DATE_TERMINATED_COLUMN_NUM], rowB[DATE_TERMINATED_COLUMN_NUM]);
        mergedRow[JURISTRICTION_COLUMN_NUM] = getJurisdictionByLatestTermination(rowA, rowB);
        mergedRow[DISTRICT_COLUMN_NUM] = getBySourceRank(rowA[DISTRICT_COLUMN_NUM], rowB[DISTRICT_COLUMN_NUM], rowA[DDID_COLUMN_NUM], rowB[DDID_COLUMN_NUM], COURT_LISTENERS, WESTLAW);
        mergedRow[CASE_NAME_COLUMN_NUM] = getBySourceRank(rowA[CASE_NAME_COLUMN_NUM], rowB[CASE_NAME_COLUMN_NUM], rowA[DDID_COLUMN_NUM], rowB[DDID_COLUMN_NUM], WESTLAW, COURT_LISTENERS);
        mergedRow[CASE_LENGHT_DAYS_COLUMN_NUM] = getBySourceRank(rowA[CASE_LENGHT_DAYS_COLUMN_NUM], rowB[CASE_LENGHT_DAYS_COLUMN_NUM], rowA[DDID_COLUMN_NUM], rowB[DDID_COLUMN_NUM], WESTLAW, COURT_LISTENERS);
        mergedRow[COURT_ID_COLUMN_NUM] = getBySourceRank(rowA[COURT_ID_COLUMN_NUM], rowB[COURT_ID_COLUMN_NUM], rowA[DDID_COLUMN_NUM], rowB[DDID_COLUMN_NUM], COURT_LISTENERS, WESTLAW);
        mergedRow[ALLEGATION_COLUMN_NUM] = getBySourceRank(rowA[ALLEGATION_COLUMN_NUM], rowB[ALLEGATION_COLUMN_NUM], rowA[DDID_COLUMN_NUM], rowB[DDID_COLUMN_NUM], WESTLAW, null);
        mergedRow[DAMAGES_COLUMN_NUM] = getBySourceRank(rowA[DAMAGES_COLUMN_NUM], rowB[DAMAGES_COLUMN_NUM], rowA[DDID_COLUMN_NUM], rowB[DDID_COLUMN_NUM], WESTLAW, null);
        mergedRow[CASE_TYPE_COLUMN_NUM] = getByCaseTypeRules(rowA[CASE_TYPE_COLUMN_NUM], rowB[CASE_TYPE_COLUMN_NUM]);
        mergedRow[LINK_COLUMN_NUM] = getBySourceRank(rowA[LINK_COLUMN_NUM], rowB[LINK_COLUMN_NUM], rowA[DDID_COLUMN_NUM], rowB[DDID_COLUMN_NUM], WESTLAW, COURT_LISTENERS);
        mergedRow[STATUS_COLUMN_NUM] = getBySourceRank(rowA[STATUS_COLUMN_NUM], rowB[STATUS_COLUMN_NUM], rowA[DDID_COLUMN_NUM], rowB[DDID_COLUMN_NUM], WESTLAW, COURT_LISTENERS);
        return mergedRow;
    }

    // Helper method to get the earliest date
    private String getEarliestDate(String dateA, String dateB) {
        try {
            Date parsedDateA = DATE_FORMAT.parse(dateA);
            Date parsedDateB = DATE_FORMAT.parse(dateB);
            return (parsedDateA.before(parsedDateB)) ? dateA : dateB;
        } catch (ParseException e) {
            return defaultRule(dateA, dateB);
        }
    }

    // Helper method to get the latest date
    private String getLatestDate(String dateA, String dateB) {
        try {
            Date parsedDateA = DATE_FORMAT.parse(dateA);
            Date parsedDateB = DATE_FORMAT.parse(dateB);
            return (parsedDateA.after(parsedDateB)) ? dateA : dateB;
        } catch (ParseException e) {
            return defaultRule(dateA, dateB);
        }
    }

    // Rule for jurisdiction by latest terminated date
    private String getJurisdictionByLatestTermination(String[] rowA, String[] rowB) {
        String terminationDateA = rowA[2]; // Assuming date_terminated is at index 2
        String terminationDateB = rowB[2];
        return getLatestDate(terminationDateA, terminationDateB).equals(terminationDateA) ? rowA[3] : rowB[3];
    }

    // Helper method for handling source rank rules
    private String getBySourceRank(String valueA, String valueB, String sourceA, String sourceB, String preferredSource1, String preferredSource2) {
        if (sourceA.equals(preferredSource1)) {
            return valueA;
        }
        if (sourceB.equals(preferredSource1)) {
            return valueB;
        }
        if (preferredSource2 != null) {
            if (sourceA.equals(preferredSource2)) {
                return valueA;
            }
            if (sourceB.equals(preferredSource2)) {
                return valueB;
            }
        }
        return defaultRule(valueA, valueB);
    }

    // Rule for case_type with specific conditions
    private String getByCaseTypeRules(String caseTypeA, String caseTypeB) {
        String condition1 = "Other statutory damages";
        if (caseTypeA.equals(condition1))
            return caseTypeA;
        if (caseTypeB.equals(condition1))
            return caseTypeB;

        String condition2 = "nos";
        String condition3 = "contract";

        if (!caseTypeA.toLowerCase().contains(condition2) && !caseTypeA.toLowerCase().contains(condition3))
            return caseTypeA;
        if (!caseTypeB.toLowerCase().contains(condition2) && !caseTypeB.toLowerCase().contains(condition3))
            return caseTypeB;

        return defaultRule(caseTypeA, caseTypeB);
    }


    // Default rule: take most common, then latest, then random
    private String defaultRule(String valueA, String valueB) {
        // 1. Most common value (if both are equal, return either)
        if (valueA.equals(valueB))
            return valueA;

        // 2. Latest value: assuming that lexicographically larger is considered "latest" here
        if (valueA.compareTo(valueB) > 0)
            return valueA;
        else if (valueA.compareTo(valueB) < 0)
            return valueB;

        // 3. Random selection
        return new Random().nextBoolean() ? valueA : valueB;
    }
}
