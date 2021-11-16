package edu.utexas.ece382v.tm_reader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import edu.utexas.ece382v.shortest_path.entities.Edge;

public class AgentNode implements Comparable<AgentNode> {

  private Integer identifier;
  private String name;
  private Double weight;
  private List<Edge> incomingEdges = new ArrayList<>();
  private List<Edge> outgoingEdges = new ArrayList<>();


  public AgentNode(Integer identifier, String name) {
    this.identifier = identifier;
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getIdentifier() {
    return this.identifier;
  }

  public void setIdentifier(Integer identifier) {
    this.identifier = identifier;
  }

  public Double getWeight() {
    return this.weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public List<Edge> getIncomingEdges() {
    return this.incomingEdges;
  }

  public void setIncomingEdges(List<Edge> incomingEdges) {
    this.incomingEdges = incomingEdges;
  }

  public void setOutgoingEdges(List<Edge> outgoingEdges) {
    this.outgoingEdges = outgoingEdges;
  }

  @Override
  public int compareTo(AgentNode o) {
    // Not null safe
    return this.identifier.compareTo(o.getIdentifier());
  }

  public Collection<Edge> getOutgoingEdges() {
    return this.outgoingEdges;
  }

  @Override
  public String toString() {
    return this.identifier + " " + this.name;
  }

  public boolean equals(AgentNode o) {
    return (o instanceof AgentNode) && (o.getIdentifier()).equals(this.getIdentifier());
  }

  @Override
  public int hashCode() {
    return this.identifier.hashCode();
  }

}
