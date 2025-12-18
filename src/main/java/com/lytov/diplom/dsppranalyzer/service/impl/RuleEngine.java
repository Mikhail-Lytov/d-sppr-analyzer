package com.lytov.diplom.dsppranalyzer.service.impl;

import com.lytov.diplom.dsppranalyzer.domain.enums.BpmnType;
import com.lytov.diplom.dsppranalyzer.domain.enums.EdgeType;
import com.lytov.diplom.dsppranalyzer.domain.enums.NodeType;
import com.lytov.diplom.dsppranalyzer.service.dto.*;
import com.lytov.diplom.dsppranalyzer.util.Matchers;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RuleEngine {
    public List<Finding> analyze(BpmnGraph g, List<Rule> rules) {
        List<Finding> out = new ArrayList<>();

        for (Rule r : rules) {
            if ("NODE".equalsIgnoreCase(r.target)) {
                out.addAll(applyNodeRule(g, r));
            } else if ("EDGE".equalsIgnoreCase(r.target)) {
                out.addAll(applyEdgeRule(g, r));
            }
        }

        return dedupe(out);
    }

    private List<Finding> applyNodeRule(BpmnGraph g, Rule r) {
        Set<NodeType> allowedTypes = Matchers.setOfNodeType(r.where != null ? r.where.nodeTypes : null);
        List<Finding> findings = new ArrayList<>();

        for (BpmnGraph.BpmnNode n : g.nodes.values()) {
            if (!allowedTypes.isEmpty() && !allowedTypes.contains(n.type())) continue;
            if (!checkNodeRequires(g, n, r.requires)) continue;

            List<BpmnGraph.BpmnEdge> edges = new  ArrayList<>();
            if (r.searchEdge) {
                edges = searchEdge(g, n, r);
            }
            if (!edges.isEmpty()) {
                for (BpmnGraph.BpmnEdge e : edges) {
                    String evidence = renderEvidence(r, n, e , null);
                    findings.add(new Finding(
                            r.ruleId, r.code, BpmnType.NODE, n.id(),
                            r.emit != null ? r.emit.severity : "MED",
                            r.emit != null ? r.emit.confidence : "LOW",
                            evidence, n.id(), r.riskId
                    ));
                }
            } else {
                String evidence = renderEvidence(r, n, null, null);
                findings.add(new Finding(
                        r.ruleId, r.code, BpmnType.NODE, n.id(),
                        r.emit != null ? r.emit.severity : "MED",
                        r.emit != null ? r.emit.confidence : "LOW",
                        evidence, n.id(), r.riskId
                ));
            }
        }
        return findings;
    }

    private boolean checkNodeRequires(BpmnGraph g, BpmnGraph.BpmnNode n, Requires req) {
        if (req == null) return true;

        // inEdges requirements
        if (req.inEdges != null && !req.inEdges.isEmpty()) {
            List<BpmnGraph.BpmnEdge> inEdges = g.in.getOrDefault(n.id(), List.of());
            for (ReqEdge re : req.inEdges) {
                if (!existsMatchingEdge(g, inEdges, re, true)) return false;
            }
        }

        // outEdges requirements
        if (req.outEdges != null && !req.outEdges.isEmpty()) {
            List<BpmnGraph.BpmnEdge> outEdges = g.out.getOrDefault(n.id(), List.of());
            for (ReqEdge re : req.outEdges) {
                if (!existsMatchingEdge(g, outEdges, re, false)) return false;
            }
        }

        return true;
    }

    private boolean existsMatchingEdge(BpmnGraph g, List<BpmnGraph.BpmnEdge> edges, ReqEdge re, boolean isIncomingList) {
        Set<NodeType> srcTypes = Matchers.setOfNodeType(re.sourceNodeTypes);
        Set<NodeType> tgtTypes = Matchers.setOfNodeType(re.targetNodeTypes);

        for (BpmnGraph.BpmnEdge e : edges) {
            if (re.type != null && !re.type.name().equals(e.type().name())) continue;

            BpmnGraph.BpmnNode src = g.nodes.get(e.sourceId());
            BpmnGraph.BpmnNode tgt = g.nodes.get(e.targetId());

            // Для incomingList: source -> THIS_NODE; для outgoingList: THIS_NODE -> target
            if (!srcTypes.isEmpty() && (src == null || !srcTypes.contains(src.type()))) continue;
            if (!tgtTypes.isEmpty() && (tgt == null || !tgtTypes.contains(tgt.type()))) continue;

            return true;
        }
        return false;
    }

    private List<Finding> applyEdgeRule(BpmnGraph g, Rule r) {
        Set<EdgeType> allowedEdgeTypes = Matchers.setOf(r.where != null ? r.where.edgeTypes : null);
        Set<NodeType> srcTypes = Matchers.setOfNodeType(r.requires != null ? r.requires.sourceNodeTypes : null);
        Set<NodeType> tgtTypes = Matchers.setOfNodeType(r.requires != null ? r.requires.targetNodeTypes : null);

        List<Finding> findings = new ArrayList<>();

        for (BpmnGraph.BpmnEdge e : g.edges) {
            if (!allowedEdgeTypes.isEmpty() && !allowedEdgeTypes.stream().map(EdgeType::name).collect(Collectors.toSet()).contains(e.type().name()))
                continue;

            BpmnGraph.BpmnNode src = g.nodes.get(e.sourceId());
            BpmnGraph.BpmnNode tgt = g.nodes.get(e.targetId());

            if (!srcTypes.isEmpty() && (src == null || !srcTypes.contains(src.type()))) continue;
            if (!tgtTypes.isEmpty() && (tgt == null || !tgtTypes.contains(tgt.type()))) continue;

            String evidence = renderEvidence(r, null, e, g);
            findings.add(new Finding(
                    r.ruleId, r.code, BpmnType.EDGE, e.id(),
                    r.emit != null ? r.emit.severity : "MED",
                    r.emit != null ? r.emit.confidence : "LOW",
                    evidence, e.sourceId(), r.riskId
            ));
        }
        return findings;
    }

    private String renderEvidence(Rule r, BpmnGraph.BpmnNode node, BpmnGraph.BpmnEdge edge, BpmnGraph g) {
        String t = r.evidence != null ? r.evidence.template : null;
        if (t == null) return "";

        // супер-простой templating
        String res = t;
        if (node != null) {
            res = res.replace("{node.id}", node.id())
                    .replace("{node.type}", String.valueOf(node.type()))
                    .replace("{node.name}", String.valueOf(node.name()));
        }
        if (edge != null) {
            res = res.replace("{edge.id}", edge.id())
                    .replace("{edge.type}", edge.type().name())
                    .replace("{edge.sourceId}", edge.sourceId())
                    .replace("{edge.targetId}", edge.targetId());
        }
        return res;
    }

    private List<Finding> dedupe(List<Finding> findings) {
        // ключ: ruleId + bpmnType + refId
        Map<String, Finding> uniq = new LinkedHashMap<>();
        for (Finding f : findings) {
            String key = f.ruleId() + "|" + f.bpmnType() + "|" + f.refId();
            uniq.putIfAbsent(key, f);
        }
        return new ArrayList<>(uniq.values());
    }

    private List<BpmnGraph.BpmnEdge> searchEdge(BpmnGraph g, BpmnGraph.BpmnNode node, Rule r) {
        List<BpmnGraph.BpmnEdge> edges = g.edges
                .stream()
                .filter(
                        edg -> {
                            return edg.targetId().equals(node.id());
                        }
                )
                .toList();

        List<NodeType> sourcesNodeTypes = new ArrayList();
        for (ReqEdge req : r.requires.inEdges) {
            sourcesNodeTypes.addAll(req.sourceNodeTypes);
        }

        List<BpmnGraph.BpmnNode> sources = g.nodes.values()
                .stream()
                .filter(s ->  sourcesNodeTypes.contains(s.type()))
                .toList();

        return edges.stream().filter(e -> {
                    return sources.stream().map(BpmnGraph.BpmnNode::id).toList().contains(e.sourceId());
                })
                .toList();
    }
}