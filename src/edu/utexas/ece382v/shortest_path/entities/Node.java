package edu.utexas.ece382v.shortest_path.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import edu.utexas.ece382v.tm_reader.AgentNode;

public class Node implements Comparable<Node> {

    private String identifier;
    private Double weight;
    private List<Edge> incomingEdges = new ArrayList<>();
    private List<Edge> outgoingEdges = new ArrayList<>();

    private Node predecessor = null;

    public Node(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public List<Edge> getIncomingEdges() {
        return incomingEdges;
    }

    public void setIncomingEdges(List<Edge> incomingEdges) {
        this.incomingEdges = incomingEdges;
    }

    public void setOutgoingEdges(List<Edge> outgoingEdges) {
        this.outgoingEdges = outgoingEdges;
    }

    @Override
    public int compareTo(Node o) {
        //Not null safe
        return Comparator.comparing(Node::getWeight).thenComparing(Node::getIdentifier).compare(this, o);
    }

    public Collection<Edge> getOutgoingEdges() {
        return outgoingEdges;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

}
