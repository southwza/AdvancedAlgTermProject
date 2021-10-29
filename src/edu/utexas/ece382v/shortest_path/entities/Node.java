package edu.utexas.ece382v.shortest_path.entities;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {

    private String identifier;
    private Double weight;
    private List<Edge> edges = new ArrayList<>();


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

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    @Override
    public int compareTo(Node o) {
        //Not null safe
        return weight.compareTo(o.getWeight());
    }

}
