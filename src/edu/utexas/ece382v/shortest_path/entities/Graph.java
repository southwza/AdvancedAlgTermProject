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

    public void addEdge(Node sourceNode, Node targetNode, double weight) {
        Edge e = new Edge(sourceNode, targetNode, weight);
        edges.add(e);
        sourceNode.getOutgoingEdges().add(e);
        targetNode.getIncomingEdges().add(e);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
        edge.getSourceNode().getOutgoingEdges().add(edge);
        edge.getTargetNode().getIncomingEdges().add(edge);
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

    public double averageEdgeWeight() {
        return edges.stream().mapToDouble(edge -> edge.getWeight()).average().getAsDouble();
    }

    /**
     * Generates an output file suitable for input to GraphVis graph generator.
     * Ex: save this output to a file and execute the following command line after installing GraphVis:
     * $> dot -O -Tpng {file}
     * @return
     */
    public String toDotString(Graph g, Node targetNode) {
        String dotString = "digraph G {" + System.lineSeparator();

        Node sourceNode = targetNode;
        while (sourceNode.getPredecessor() != null) {
            sourceNode = sourceNode.getPredecessor();
        }

        for (Node n : g.getNodes()) {
            String color = "";
            if (n.equals(sourceNode)) {
                color = "[ style = filled, fillcolor = green ]";
            } else if (n.equals(targetNode)) {
                color = "[ style = filled, fillcolor = red ]";
            }
            dotString += "  " + n.getIdentifier() + System.lineSeparator() + color;
        }

        dotString += System.lineSeparator();

        for (Edge e : g.getEdges()) {
            String color = "";
            //Is this a connecting edge along the shortest path?
            
            if (e.getSourceNode().equals(e.getTargetNode().getPredecessor())) {
                color = "; color = green; penwidth = 10";
            }
            String label = " [label = \"" + e.getWeight() + "\"" + color + "]";
            dotString += "  " + e.getSourceNode().getIdentifier() + " -> " + e.getTargetNode().getIdentifier() + label + System.lineSeparator();
        }
        dotString += "}";
        return dotString;
    }
}
