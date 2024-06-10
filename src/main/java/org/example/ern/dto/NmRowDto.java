package org.example.ern.dto;

import lombok.Data;

import java.time.LocalDate;


@Data
public class NmRowDto {
    private LocalDate nmDate;
    private String nmSiebelId;
    private String nmState;
    private String nmTaxDirection;
    private Double nmTaxRate;
    private Double nmValue;
}
