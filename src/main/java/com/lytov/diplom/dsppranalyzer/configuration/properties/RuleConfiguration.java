package com.lytov.diplom.dsppranalyzer.configuration.properties;

import com.lytov.diplom.dsppranalyzer.service.dto.Rule;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties("rule-configuration")
public class RuleConfiguration {

    private final List<Rule> rules = new ArrayList<>();
}
