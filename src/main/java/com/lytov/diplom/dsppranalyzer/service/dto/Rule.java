package com.lytov.diplom.dsppranalyzer.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rule {
    public String ruleId;
    public String code;
    public String target; // NODE or EDGE
    public Where where;
    public Requires requires;
    public Emit emit;
    public Evidence evidence;
    public Boolean searchEdge = false;
    public UUID riskId;
}
