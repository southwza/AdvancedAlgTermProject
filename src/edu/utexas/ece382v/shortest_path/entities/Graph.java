package edu.utexas.ece382v.shortest_path.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Graph {
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    public Graph() {}

    public Graph(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public List<Node> getNodes() {
        return nodes;
    }
    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
    public List<Edge> getEdges() {
        return edges;
    }
    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public List<Edge> getOutgoingEdges(Node sourceNode) {
        return edges.stream() //
                .filter(edge -> edge.getNode1().getIdentifier().equals(sourceNode.getIdentifier())
                        || edge.getNode2().getIdentifier().equals(sourceNode.getIdentifier())) //
                .collect(Collectors.toList());
    }

    public Edge getSmallestOutgoingEdge(Node sourceNode) {
        Edge smallestOutgoingEdge = edges.stream() //
                .filter(edge -> edge.getNode1().getIdentifier().equals(sourceNode.getIdentifier())
                        || edge.getNode2().getIdentifier().equals(sourceNode.getIdentifier())) //
                .min(Comparator.comparing(Edge::getWeight)).orElse(null);
        return smallestOutgoingEdge;
    }

    public void addEdge(Node node_1, Node node_2, double weight) {
        Edge e = new Edge(node_1, node_2, weight);
        edges.add(e);
    }

}
