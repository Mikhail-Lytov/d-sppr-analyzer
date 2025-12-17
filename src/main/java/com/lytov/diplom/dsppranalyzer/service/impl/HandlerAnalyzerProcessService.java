package com.lytov.diplom.dsppranalyzer.service.impl;

import com.lytov.diplom.dsppranalyzer.configuration.rabbit.AnalyzerRMQConfig;
import com.lytov.diplom.dsppranalyzer.service.api.AnaliseService;
import com.lytov.diplom.dsppranalyzer.service.dto.AnalyzerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandlerAnalyzerProcessService {

    private final List<AnaliseService> services;

    @RabbitListener(queues = AnalyzerRMQConfig.FROM_ANALYZER_PROCESS_QUEUE)
    public void handle(@Payload AnalyzerRequest request) {
        services.forEach(service -> {
            try {
                service.analise(request);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
