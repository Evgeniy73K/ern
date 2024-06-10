package org.example.ern.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestDto {
    private String path;
    private Double taxRate;
    private LocalDate fromDate;
    private LocalDate toDate;
}
