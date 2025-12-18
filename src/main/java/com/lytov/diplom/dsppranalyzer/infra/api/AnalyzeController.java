package com.lytov.diplom.dsppranalyzer.infra.api;

import com.lytov.diplom.dsppranalyzer.service.dto.AnalyzerRequest;
import com.lytov.diplom.dsppranalyzer.service.impl.HandlerAnalyzerProcessService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Анализ процессов")
@RequestMapping("/api/v0/analyze")
@RestController
@RequiredArgsConstructor
public class AnalyzeController {

    private final HandlerAnalyzerProcessService service;

    @PostMapping("")
    public void analyze(@RequestBody AnalyzerRequest request) {
        service.handle(request);
    }
}
