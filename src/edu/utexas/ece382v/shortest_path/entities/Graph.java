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
                .filter(edge -> edge.getSourceNode().getIdentifier().equals(sourceNode.getIdentifier()))
                .collect(Collectors.toList());
    }

    public Edge getSmallestOutgoingEdge(Node sourceNode) {
        Edge smallestOutgoingEdge = edges.stream() //
                .filter(edge -> edge.getSourceNode().getIdentifier().equals(sourceNode.getIdentifier()))
                .min(Comparator.comparing(Edge::getWeight)).orElse(null);
        return smallestOutgoingEdge;
    }

    public void addEdge(Node sourceNode, Node targetNode, double weight) {
        Edge e = new Edge(sourceNode, targetNode, weight);
        edges.add(e);
    }

    @Override
    public String toString() {
        String nodesString = nodes.stream()
                .map(node -> "  " + node.getIdentifier() + System.lineSeparator())
                .collect(Collectors.joining());

        String edgesString = edges.stream()
                .map(edge -> "  " + edge.getSourceNode().getIdentifier() + ", " + edge.getTargetNode().getIdentifier() + ": " + edge.getWeight() + System.lineSeparator())
                .collect(Collectors.joining());

        String graphString = "Nodes: " + System.lineSeparator() + nodesString + System.lineSeparator() + "Edges: " + System.lineSeparator() + edgesString;
        return graphString;
    }

    public Node findNode(String identifier) {
        Node node = nodes.stream()
                .filter(n -> n.getIdentifier().equals(identifier))
                .findFirst().orElse(null);

        return node;
    }

}
