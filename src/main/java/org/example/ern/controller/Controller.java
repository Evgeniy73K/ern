package org.example.ern.controller;

import lombok.SneakyThrows;
import org.example.ern.dto.RequestDto;
import org.example.ern.service.Finder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;

@RestController
public class Controller {
    private final Finder finder = new Finder();

    @PostMapping("/findDiscrepancy")
    @SneakyThrows
    public HashMap<String, Double> findDiscrepancy(@RequestBody RequestDto requestDto) {
        String path = requestDto.getPath();
        LocalDate fromDate = requestDto.getFromDate();
        LocalDate toDate = requestDto.getToDate();
        Double taxRate = requestDto.getTaxRate();
        return finder.findDiscrepancy(path, fromDate, toDate, taxRate);
    }
}

