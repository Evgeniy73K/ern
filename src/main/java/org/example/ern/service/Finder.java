package org.example.ern.service;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.ern.dto.ErnRowDto;
import org.example.ern.dto.NmRowDto;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class Finder {

    private List<ErnRowDto> ernList = new ArrayList<>();
    private List<NmRowDto> nmList = new ArrayList<>();
    private final Map<String, Double> ernMap = new HashMap<>();
    private final Map<String, Double> nmMap = new HashMap<>();
    private final Map<String, Double> resultMap = new HashMap<>();

    public HashMap<String, Double> findDiscrepancy(String path, LocalDate fromDate, LocalDate toDate, Double taxRate) {
        clearAllData();

        readFileAndParse(path);

        filterListsByDateAndTaxRate(fromDate, toDate, taxRate);

        createValueMaps();

        calculateResultMap();

        return (HashMap<String, Double>) resultMap;
    }

    private void clearAllData() {
        ernList.clear();
        nmList.clear();
        ernMap.clear();
        nmMap.clear();
        resultMap.clear();
    }

    @SneakyThrows
    private void readFileAndParse(String path) {
        FileInputStream inputStream = new FileInputStream(new File(path));
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        Iterator<Row> rowIterator = workbook.getSheetAt(1).iterator();

        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            parseRow(row);
        }
    }

    private void parseRow(Row row) {
        ErnRowDto ernDto = new ErnRowDto();
        NmRowDto nmDto = new NmRowDto();

        if (row.getCell(0) != null) {
            ernDto.setErnDate(row.getCell(0).getLocalDateTimeCellValue().toLocalDate());
            ernDto.setErnSiebelId(String.valueOf(row.getCell(1)));
            ernDto.setErnState(String.valueOf(row.getCell(2)));
            ernDto.setErnTaxDirection(String.valueOf(row.getCell(3)));
            ernDto.setErnTaxRate(Double.valueOf(String.valueOf(row.getCell(4))));
            ernDto.setErnValue(Double.valueOf(String.valueOf(row.getCell(5))));
            ernList.add(ernDto);
        }

        if (row.getCell(6) != null) {
            nmDto.setNmDate(row.getCell(6).getLocalDateTimeCellValue().toLocalDate());
            nmDto.setNmSiebelId(String.valueOf(row.getCell(7)));
            nmDto.setNmState(String.valueOf(row.getCell(8)));
            nmDto.setNmTaxDirection(String.valueOf(row.getCell(9)));
            nmDto.setNmTaxRate(Double.valueOf(String.valueOf(row.getCell(10))));
            nmDto.setNmValue(Double.valueOf(String.valueOf(row.getCell(11))));
            nmList.add(nmDto);
        }
    }

    private void filterListsByDateAndTaxRate(LocalDate fromDate, LocalDate toDate, Double taxRate) {
        nmList = nmList.stream()
                .filter(obj -> Objects.equals(obj.getNmTaxRate(), taxRate))
                .filter(obj -> obj.getNmDate().isAfter(fromDate.minusDays(1)))
                .filter(obj -> obj.getNmDate().isBefore(toDate.plusDays(1)))
                .collect(Collectors.toList());

        ernList = ernList.stream()
                .filter(obj -> Objects.equals(obj.getErnTaxRate(), taxRate))
                .filter(obj -> obj.getErnDate().isAfter(fromDate.minusDays(1)))
                .filter(obj -> obj.getErnDate().isBefore(toDate.plusDays(1)))
                .collect(Collectors.toList());
    }

    private void createValueMaps() {
        nmList.forEach(nmDto -> nmMap.merge(nmDto.getNmSiebelId(), nmDto.getNmValue(), Double::sum));
        ernList.forEach(ernRowDto -> ernMap.merge(ernRowDto.getErnSiebelId(), ernRowDto.getErnValue(), Double::sum));
    }

    private void calculateResultMap() {
        nmMap.forEach((k, v) -> {
            if (!ernMap.containsKey(k) || ernMap.get(k) - v != 0) {
                resultMap.put(k, nmMap.get(k));
            }
        });

        ernMap.forEach((k, v) -> {
            if(!nmMap.containsKey(k)) {
                resultMap.put(k, ernMap.get(k));
            }
        });
    }
}