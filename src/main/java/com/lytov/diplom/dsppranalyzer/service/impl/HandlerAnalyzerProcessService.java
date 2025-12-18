package com.lytov.diplom.dsppranalyzer.service.impl;

import com.lytov.diplom.dsppranalyzer.configuration.properties.RuleConfiguration;
import com.lytov.diplom.dsppranalyzer.configuration.rabbit.AnalyzerRMQConfig;
import com.lytov.diplom.dsppranalyzer.configuration.rabbit.CoreRMQConfig;
import com.lytov.diplom.dsppranalyzer.service.dto.AnalyzerRequest;
import com.lytov.diplom.dsppranalyzer.service.dto.Finding;
import com.lytov.diplom.dsppranalyzer.service.dto.ResultAnalyzeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandlerAnalyzerProcessService {

    private final RuleEngine ruleEngine;
    private final RuleConfiguration ruleConfiguration;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = AnalyzerRMQConfig.FROM_ANALYZER_PROCESS_QUEUE)
    public void handle(@Payload AnalyzerRequest request) {
        List<Finding> findings = ruleEngine.analyze(request.getGraph(), ruleConfiguration.getRules());

        ResultAnalyzeMessage message = new ResultAnalyzeMessage(
                request.getProcessId(),
                findings
        );

        rabbitTemplate.convertAndSend(
                CoreRMQConfig.FROM_ANALYZER_RESULT_EXCHANGE,
                "",
                message
        );
        log.info(String.format(
                "send message: %s", message
        ));
        /*services.forEach(service -> {
            try {
                service.analise(request);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });*/
    }
}
