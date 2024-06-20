package org.example.ern.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.example.ern.dto.RequestDto;
import org.example.ern.service.Finder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;

@RestController
@Tag(name = "Finder Controller", description = "Контроллер для поиска расхождений")
public class Controller {
    private final Finder finder = new Finder();

    @PostMapping("/findDiscrepancy")
    @Operation(summary = "Найти расхождения", description = "Поиск расхождений на основе переданных параметров")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Расхождения найдены",
                    content = @Content(schema = @Schema(implementation = HashMap.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
    })
    @SneakyThrows
    public HashMap<String, Double> findDiscrepancy(@RequestBody RequestDto requestDto) {
        String path = requestDto.getPath();
        LocalDate fromDate = requestDto.getFromDate();
        LocalDate toDate = requestDto.getToDate();
        Double taxRate = requestDto.getTaxRate();
        return finder.findDiscrepancy(path, fromDate, toDate, taxRate);
    }
}

