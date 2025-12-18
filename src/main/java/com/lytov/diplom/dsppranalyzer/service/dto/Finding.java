package com.lytov.diplom.dsppranalyzer.service.dto;

import com.lytov.diplom.dsppranalyzer.domain.enums.BpmnType;

import java.util.UUID;

public record Finding(
        String ruleId,
        String code,
        BpmnType bpmnType, // NODE/EDGE
        String refId,    // id элемента BPMN или ребра
        String severity,
        String confidence,
        String evidence,
        String idTask,
        UUID riskId
) {
}
