package com.lytov.diplom.dsppranalyzer.service.dto;

import com.lytov.diplom.dsppranalyzer.domain.enums.NodeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Requires {
    public List<ReqEdge> inEdges;
    public List<ReqEdge> outEdges;

    // EDGE rules:
    public List<NodeType> sourceNodeTypes;
    public List<NodeType> targetNodeTypes;
}
