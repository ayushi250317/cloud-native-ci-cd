package com.assignment1.csvreader.CsvReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvReaderService {
    private final ResourceLoader resourceLoader;
     public Integer calculateSum(String fileName, String product) throws CsvValidationException, IOException {
        String rootPath = "file:/app/data/";
        String filePath = rootPath + fileName;
        List<String[]> records = new ArrayList<>();
        Resource resource = resourceLoader.getResource(filePath);
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                CSVReader csvReader = new CSVReader(reader)) {
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                records.add(record);
            }
        } catch (IOException e) {
        }
        Integer sum=0;
        for (int i = 1; i < records.size(); i++) {
            String[] record = records.get(i);
            if(record[0].equals(product)){
                sum+=Integer.parseInt(record[1]);
            }
        }
        return sum;
    }
}
