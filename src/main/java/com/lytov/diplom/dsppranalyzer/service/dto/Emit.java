package com.lytov.diplom.dsppranalyzer.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emit {
    public String mark;       // THIS_NODE / THIS_EDGE
    public String severity;   // LOW/MED/HIGH
    public String confidence; // LOW/MED/HIGH
}
