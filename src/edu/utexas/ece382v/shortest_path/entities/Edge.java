package edu.utexas.ece382v.shortest_path.entities;

public class Edge {

    private Node node1;
    private Node node2;
    private Double weight;

    public Edge(Node node1, Node node2, Double weight) {
        this.node1 = node1;
        this.node2 = node2;
        this.weight = weight;
    }

    public Node getNode1() {
        return node1;
    }

    public void setNode1(Node node1) {
        this.node1 = node1;
    }

    public Node getNode2() {
        return node2;
    }

    public void setNode2(Node node2) {
        this.node2 = node2;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * 
     * @param sourceNode
     * @return the other node of this edge. SourceNode must not be null and
     *         must be one of the nodes in this edge
     */
    public Node getTarget(Node sourceNode) {
        if (sourceNode.getIdentifier().equals(node1.getIdentifier())) {
            return node2;
        }
        return node1;
    }
}
