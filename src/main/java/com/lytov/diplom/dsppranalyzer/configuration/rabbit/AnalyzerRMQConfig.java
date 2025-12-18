package com.lytov.diplom.dsppranalyzer.configuration.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalyzerRMQConfig {

    public static final String FROM_ANALYZER_PROCESS_EXCHANGE = "d-sppr-analyzer.analyzer-process.exchange";
    public static final String FROM_ANALYZER_PROCESS_QUEUE = "d-sppr-analyzer.analyzer-process.queue";

    @Bean
    public FanoutExchange fromDSpprAnalyzerProcessExchange() {
        return new FanoutExchange(FROM_ANALYZER_PROCESS_EXCHANGE);
    }

    @Bean
    public Queue fromDSpprAnalyzerProcessQueue() {
        return new Queue(FROM_ANALYZER_PROCESS_QUEUE);
    }

    @Bean
    public Binding fromDSpprAnalyzerProcessBinding(
            FanoutExchange fromDSpprAnalyzerProcessExchange,
            Queue fromDSpprAnalyzerProcessQueue
    ) {
        return BindingBuilder
                .bind(fromDSpprAnalyzerProcessQueue)
                .to(fromDSpprAnalyzerProcessExchange);
    }
}
