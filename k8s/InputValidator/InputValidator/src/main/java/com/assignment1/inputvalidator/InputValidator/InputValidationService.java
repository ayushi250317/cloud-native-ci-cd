package com.assignment1.inputvalidator.InputValidator;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InputValidationService {

    @Autowired
    private RestTemplate restTemplate;
    private final ResourceLoader resourceLoader;

    public boolean doesFileExists(String fileName) {
        // Load the file as a resource
        String rootPath = "file:/app/data/";
        String filePath = rootPath + fileName;
        Resource resource = resourceLoader.getResource(filePath);
        if (!resource.exists()) {
            return false;
        }
        return true;
    }

    public boolean isValidCSV(String fileName) throws CsvValidationException, IOException {
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
            return false;
        }
        if (records.size() == 0) {
            return false;
        }
        String[] columns = records.get(0);
        if (columns.length != 2) {
            return false;
        }
        if (!columns[0].equals("product") || !columns[1].equals("amount")) {
            return false;
        }
        for (int i = 1; i < records.size(); i++) {
            String[] record = records.get(i);
            if (record.length != 2) {
                return false;
            }
            try {
                Integer.parseInt(record[1]);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public Integer sendPostRequest(String url, Object requestBody)
            throws JsonMappingException, JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                String.class);
        String responseBody = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String value = jsonNode.get("sum").asText();
        final Logger logger = LoggerFactory.getLogger(InputValidationService.class);
        logger.info(responseBody);
        logger.info("Success");
        return Integer.parseInt(value);
    }
//hi2
    public Boolean storeFile(String fileName, String contents) throws IOException {
        
       String rootPath = resourceLoader.getResource("file:/app/data/ayushi_dir/").getFile().getAbsolutePath();
        
        Path directoryPath = Paths.get(rootPath);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        Path filePath = directoryPath.resolve(fileName);
        Files.write(filePath, contents.getBytes());

        return true;
    }

}
