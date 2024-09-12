package com.talgokhberg.LegalDataConsolidator.service;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Service
public class CsvExportService {

    public void exportToCsv(Map<String, String[]> data, String filePath) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String[]> entry : data.entrySet()) {
                writer.writeNext(entry.getValue());
            }
        }
    }
}

