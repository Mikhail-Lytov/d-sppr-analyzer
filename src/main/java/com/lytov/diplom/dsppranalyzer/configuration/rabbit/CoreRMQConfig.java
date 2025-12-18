package com.lytov.diplom.dsppranalyzer.configuration.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreRMQConfig {

    public static final String FROM_ANALYZER_RESULT_EXCHANGE = "d-sppr-analyzer.analyzer-process-result.exchange";
    public static final String FROM_ANALYZER_RESULT_QUEUE = "d-sppr-analyzer.analyzer-process-result.queue";

    @Bean
    public FanoutExchange fromDSpprAnalyzerResultExchange() {
        return new FanoutExchange(FROM_ANALYZER_RESULT_EXCHANGE);
    }

    @Bean
    public Queue fromDSpprAnalyzerResultQueue() {
        return new Queue(FROM_ANALYZER_RESULT_QUEUE);
    }

    @Bean
    public Binding fromDSpprAnalyzerResultBinding(
            FanoutExchange fromDSpprAnalyzerResultExchange,
            Queue fromDSpprAnalyzerResultQueue
    ) {
        return BindingBuilder
                .bind(fromDSpprAnalyzerResultQueue)
                .to(fromDSpprAnalyzerResultExchange);
    }
}
