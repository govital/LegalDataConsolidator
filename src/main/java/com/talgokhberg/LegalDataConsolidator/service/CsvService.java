package com.talgokhberg.LegalDataConsolidator.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class CsvService {

    public Map<String, String[]> loadCsv(String filePath) throws IOException, CsvValidationException {
        Map<String, String[]> dataMap = new HashMap<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                // Ensure the record has exactly EXPECTED_LENGTH columns
                if (nextRecord.length < DataMergeService.MERGED_COLUMN_LENGTH) {
                    // Create a new array of the expected length
                    String[] paddedRecord = Arrays.copyOf(nextRecord, DataMergeService.MERGED_COLUMN_LENGTH);
                    // Fill the remaining elements with empty strings
                    for (int i = nextRecord.length; i < DataMergeService.MERGED_COLUMN_LENGTH; i++) {
                        paddedRecord[i] = "";
                    }
                    // Use the padded record
                    dataMap.put(paddedRecord[0], paddedRecord);
                } else {
                    // Assuming ddid is in the first column, storing data keyed by ddid
                    dataMap.put(nextRecord[0], nextRecord);
                }

            }
        }
        return dataMap;
    }
}

