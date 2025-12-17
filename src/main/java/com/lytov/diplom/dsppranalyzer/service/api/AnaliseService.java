package com.lytov.diplom.dsppranalyzer.service.api;

import com.lytov.diplom.dsppranalyzer.domain.enums.AnaliseType;
import com.lytov.diplom.dsppranalyzer.service.dto.AnalyzerRequest;

public interface AnaliseService {

    void analise(AnalyzerRequest request);

    AnaliseType getType();
}
