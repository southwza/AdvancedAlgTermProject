package edu.utexas.ece382v.tm_reader;

public class Connection {

  private AgentNode sourceNode;
  private AgentNode targetNode;
  private Double weight;
  private Integer filmId;
  private String filmName;

  public Connection(AgentNode sourceNode, AgentNode targetNode, Double weight, Integer filmId,
      String filmName) {
    super();
    this.sourceNode = sourceNode;
    this.targetNode = targetNode;
    this.weight = weight;
    this.filmId = filmId;
    this.filmName = filmName;
  }

  public AgentNode getSourceNode() {
    return sourceNode;
  }

  public void setSourceNode(AgentNode sourceNode) {
    this.sourceNode = sourceNode;
  }

  public AgentNode getTargetNode() {
    return targetNode;
  }

  public void setTargetNode(AgentNode targetNode) {
    this.targetNode = targetNode;
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  @Override
  public String toString() {
    return this.sourceNode.getName() + " " + this.filmName + " " + this.weight + " "
        + this.targetNode.getName();
  }


}
