package com.assignment1.csvreader.CsvReader;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.exceptions.CsvValidationException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CsvController {

    private final CsvReaderService csvReaderService;
    @PostMapping("/sum")
    public ResponseEntity<Response> validate(@RequestBody Request request) throws CsvValidationException, IOException{
        Integer sum=csvReaderService.calculateSum(request.getFile(),request.getProduct());
        Response response=Response.builder().sum(sum.toString()).build();
        return ResponseEntity.ok(response);
    }

}
