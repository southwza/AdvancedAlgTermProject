package edu.utexas.ece382v.shortest_path.entities;

public class Edge {

    private Node sourceNode;
    private Node targetNode;
    private Double weight;

    public Edge(Node sourceNode, Node targetNode, Double weight) {
        super();
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.weight = weight;
    }

    public Node getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(Node sourceNode) {
        this.sourceNode = sourceNode;
    }

    public Node getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(Node targetNode) {
        this.targetNode = targetNode;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    
}
