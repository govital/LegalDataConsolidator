package com.talgokhberg.LegalDataConsolidator.service;

import com.talgokhberg.LegalDataConsolidator.dto.ConsolidatedLegalData;
import com.talgokhberg.LegalDataConsolidator.repository.ConsolidatedLegalDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Map;

@Service
public class DataSaveService {

    @Autowired
    private ConsolidatedLegalDataRepository repository;

    public void saveData(Map<String, String[]> mergedData) {
        mergedData.forEach((ddid, row) -> {
            ConsolidatedLegalData data = new ConsolidatedLegalData();
            data.setDdid(ddid);
            try {
                data.setDate_filed(DataMergeService.DATE_FORMAT.parse(row[DataMergeService.DATE_FILED_COLUMN_NUM]));
                data.setDate_terminated(DataMergeService.DATE_FORMAT.parse(row[DataMergeService.DATE_TERMINATED_COLUMN_NUM]));
            } catch (ParseException e) {
                System.out.println("saveData dates ParseException data: " + e);
            }
            data.setJurisdiction(row[DataMergeService.JURISTRICTION_COLUMN_NUM]);
            data.setJurisdiction(row[DataMergeService.DISTRICT_COLUMN_NUM]);
            data.setJurisdiction(row[DataMergeService.CASE_NAME_COLUMN_NUM]);
            data.setJurisdiction(row[DataMergeService.CASE_LENGHT_DAYS_COLUMN_NUM]);
            data.setJurisdiction(row[DataMergeService.COURT_ID_COLUMN_NUM]);
            data.setJurisdiction(row[DataMergeService.ALLEGATION_COLUMN_NUM]);
            data.setJurisdiction(row[DataMergeService.DAMAGES_COLUMN_NUM]);
            data.setJurisdiction(row[DataMergeService.CASE_TYPE_COLUMN_NUM]);
            data.setJurisdiction(row[DataMergeService.LINK_COLUMN_NUM]);
            data.setJurisdiction(row[DataMergeService.STATUS_COLUMN_NUM]);
            repository.save(data);
        });
    }
}

