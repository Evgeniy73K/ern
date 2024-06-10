package org.example.ern.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ErnRowDto {
    private LocalDate ernDate;
    private String ernSiebelId;
    private String ernState;
    private String ernTaxDirection;
    private Double ernTaxRate;
    private Double ernValue;
}
