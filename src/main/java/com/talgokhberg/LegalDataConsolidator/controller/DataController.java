package com.talgokhberg.LegalDataConsolidator.controller;

import com.talgokhberg.LegalDataConsolidator.service.CsvExportService;
import com.talgokhberg.LegalDataConsolidator.service.CsvService;
import com.talgokhberg.LegalDataConsolidator.service.DataMergeService;
import com.talgokhberg.LegalDataConsolidator.service.DataSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DataController {

    private static final String VENDOR_A_FILE_NAME = "/docket_cl.csv";
    private static final String VENDOR_B_FILE_NAME = "/docket_wl.csv";
    private static final String CONSOLIDATED_FILE_NAME = "/consolidated_legal_data.csv";


    @Autowired
    private CsvService csvService;
    @Autowired
    private DataMergeService dataMergeService;
    @Autowired
    private DataSaveService dataSaveService;
    @Autowired
    private CsvExportService csvExportService;

    //example request: http://localhost:8080/process?fileLocation=/Users/vitalik/IdeaProjects/LegalDataConsolidator/src/main/resources
    @GetMapping("/process")
    public ResponseEntity<String> processData(@RequestParam String fileLocation) throws Exception {
        String vendorAFilePath = fileLocation + VENDOR_A_FILE_NAME;
        String vendorBFilePath = fileLocation + VENDOR_B_FILE_NAME;
        System.out.println("Received fileLocation: " + fileLocation);

        Map<String, String[]> vendorAData;
        Map<String, String[]> vendorBData;
        // Load CSVs
        System.out.println("Load CSVs: " + vendorAFilePath + " and: " + vendorBFilePath);
        try{
            vendorAData = csvService.loadCsv(vendorAFilePath);
            System.out.println("FINISHED LOADING FILE: " + vendorAFilePath);
            vendorBData = csvService.loadCsv(vendorBFilePath);
            System.out.println("FINISHED LOADING FILE: " + vendorBFilePath);
        }catch (Exception e){
            System.out.println("Exception LOADING CSV files. Exception: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Processing Failed. Check logs");
        }

        System.out.println("FINISHED LOADING BOTH FILES!");

        // Merge Data
        Map<String, String[]> mergedData;
        try{
            mergedData = dataMergeService.mergeData(vendorAData, vendorBData);
        }catch (Exception e){
            System.out.println("Exception Merging Data: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Processing Failed. Check logs");
        }

        System.out.println("FINISHED Merging Data!");

        // Save to DB
        try{
            dataSaveService.saveData(mergedData);
        }catch (Exception e){
            System.out.println("Exception Saving to DB: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Processing Failed. Check logs");
        }

        System.out.println("FINISHED Saving to DB!");

        // Export to CSV
        try{
            csvExportService.exportToCsv(mergedData, fileLocation + CONSOLIDATED_FILE_NAME);
        }catch (Exception e){
            System.out.println("Exception Exporting to CSV: " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Processing Failed. Check logs");
        }

        System.out.println("FINISHED Exporting to CSV!");
        return ResponseEntity.ok("Processing Completed");
    }
}

