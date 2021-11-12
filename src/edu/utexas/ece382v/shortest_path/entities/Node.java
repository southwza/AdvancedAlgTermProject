package edu.utexas.ece382v.shortest_path.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Node implements Comparable<Node> {

    private String identifier;
    private Double weight;
    private List<Edge> incomingEdges = new ArrayList<>();
    private List<Edge> outgoingEdges = new ArrayList<>();


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
        return weight.compareTo(o.getWeight());
    }

    public Collection<Edge> getOutgoingEdges() {
        return outgoingEdges;
    }

}
