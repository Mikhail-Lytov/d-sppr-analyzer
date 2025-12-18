package com.lytov.diplom.dsppranalyzer.service.dto;

import com.lytov.diplom.dsppranalyzer.domain.enums.EdgeType;
import com.lytov.diplom.dsppranalyzer.domain.enums.NodeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Where {
    public List<NodeType> nodeTypes;  // for NODE
    public List<EdgeType> edgeTypes;  // for EDGE
}
