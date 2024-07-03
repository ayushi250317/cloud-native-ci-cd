package com.assignment1.csvreader.CsvReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CsvReaderService {

    public Integer calculateSum(String fileName, String product) throws CsvValidationException, IOException {
        String rootPath = "/Ayushi_PV_dir/";
        String filePath = rootPath + fileName;
        List<String[]> records = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            log.error("File does not exist: {}", filePath);
            throw new IOException("File not found: " + filePath);
        }

        log.info("Attempting to read file: {}", filePath);

        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                records.add(record);
            }
        } catch (IOException e) {
            log.error("Error reading CSV file: {}", e.getMessage());
            throw e;
        }

        log.info("Successfully read {} records from file", records.size());

        int sum = 0;
        for (int i = 1; i < records.size(); i++) {
            String[] record = records.get(i);
            if (record[0].trim().equals(product)) {
                sum += Integer.parseInt(record[1].trim());
            }
        }

        log.info("Calculated sum for product {}: {}", product, sum);

        return sum;
    }
}