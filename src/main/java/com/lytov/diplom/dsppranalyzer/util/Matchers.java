package com.lytov.diplom.dsppranalyzer.util;

import com.lytov.diplom.dsppranalyzer.domain.enums.EdgeType;
import com.lytov.diplom.dsppranalyzer.domain.enums.NodeType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Matchers {
    public static boolean inSet(String value, Set<String> set) {
        return value != null && set.contains(value);
    }

    public static Set<NodeType> setOfNodeType(List<NodeType> list) {
        return list == null ? Set.of() : new HashSet<>(list);
    }

    public static Set<EdgeType> setOf(List<EdgeType> list) {
        return list == null ? Set.of() : new HashSet<>(list);
    }
}
