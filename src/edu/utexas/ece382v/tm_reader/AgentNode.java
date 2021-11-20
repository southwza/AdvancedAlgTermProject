package edu.utexas.ece382v.tm_reader;

import java.util.ArrayList;
import java.util.List;

public class AgentNode implements Comparable<AgentNode> {

  private Integer identifier;
  private String name;
  private Double weight;
  private List<Connection> incomingEdges = new ArrayList<>();
  private List<Connection> outgoingEdges = new ArrayList<>();

  private AgentNode predecessor = null;


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

  public List<Connection> getIncomingEdges() {
    return this.incomingEdges;
  }

  public void setIncomingEdges(List<Connection> incomingEdges) {
    this.incomingEdges = incomingEdges;
  }

  public void setOutgoingEdges(List<Connection> outgoingEdges) {
    this.outgoingEdges = outgoingEdges;
  }

  @Override
  public int compareTo(AgentNode o) {
    return this.getIdentifier().compareTo(o.getIdentifier());
  }

  public List<Connection> getOutgoingEdges() {
    return this.outgoingEdges;
  }

  @Override
  public String toString() {
    return this.identifier + " " + this.name;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null)
      return false;
    if (!(o instanceof AgentNode))
      return false;
    if (o == this)
      return true;
    return this.getIdentifier().equals(((AgentNode) o).getIdentifier());
  }

  @Override
  public int hashCode() {
    return this.getIdentifier();
  }

  public AgentNode getPredecessor() {
    return predecessor;
  }

  public void setPredecessor(AgentNode predecessor) {
    this.predecessor = predecessor;
  }

}
