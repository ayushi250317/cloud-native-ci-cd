package com.assignment1.inputvalidator.InputValidator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvValidationException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InputController {

    private final InputValidationService inputValidationService;

    @PostMapping("/calculate")
    public ResponseEntity<Object> validate(@RequestBody Request request) throws CsvValidationException, IOException {
        if(request.getFile()==null){
            ErrorResponse errorResponse=ErrorResponse.builder().file(request.getFile()).error("Invalid JSON input.").build();
            return ResponseEntity.ok(errorResponse);
        }
        if (!inputValidationService.doesFileExists(request.getFile())) {
            ErrorResponse errorResponse = ErrorResponse.builder().file(request.getFile()).error("File not found.")
                    .build();
            return ResponseEntity.ok(errorResponse);
        }
        if(!inputValidationService.isValidCSV(request.getFile())){
            ErrorResponse errorResponse=ErrorResponse.builder().file(request.getFile()).error("Input file not in CSV format.").build();
            return ResponseEntity.ok(errorResponse);
        }
        String url = "http://csvreader:6001/sum";
        Integer value=inputValidationService.sendPostRequest(url, request);
        ValidResponse validResponse = ValidResponse.builder().file(request.getFile()).sum(value).build();
        return ResponseEntity.ok(validResponse);
    }

    @PostMapping("/store-file")
    public ResponseEntity<Object> storeFile(@RequestBody DataRequest request) throws CsvValidationException, IOException {
        if(inputValidationService.storeFile(request.getFile(),request.getData())){
            MessageResponse messageResponse= MessageResponse.builder().file(request.getFile()).message("Success.").build();
            return ResponseEntity.ok(messageResponse);
        }
        else {
            ErrorResponse errorResponse = ErrorResponse.builder().file(request.getFile()).error("Error while storing the file to the storage.")
                    .build();
            return ResponseEntity.ok(errorResponse);
        }
    }
}
