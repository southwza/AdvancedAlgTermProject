package edu.utexas.ece382v.tm_reader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TMDBGraph {
  private List<AgentNode> nodes = new ArrayList<>();
  private List<Connection> edges = new ArrayList<>();

  public TMDBGraph() {}

  public TMDBGraph(List<AgentNode> nodes, List<Connection> edges) {
    this.nodes = nodes;
    this.edges = edges;
  }

  public List<AgentNode> getNodes() {
    return nodes;
  }

  public void setNodes(List<AgentNode> nodes) {
    this.nodes = nodes;
  }

  public List<Connection> getEdges() {
    return edges;
  }

  public void setEdges(List<Connection> edges) {
    this.edges = edges;
  }

  public void addEdge(Connection edge) {
    edges.add(edge);
    edge.getSourceNode().getOutgoingEdges().add(edge);
    edge.getTargetNode().getIncomingEdges().add(edge);
  }

  @Override
  public String toString() {
    String nodesString =
        nodes.stream().map(node -> "  " + node.getIdentifier() + System.lineSeparator())
            .collect(Collectors.joining());

    String edgesString = edges.stream()
        .map(edge -> "  " + edge.getSourceNode().getIdentifier() + ", "
            + edge.getTargetNode().getIdentifier() + ": " + edge.getWeight()
            + System.lineSeparator())
        .collect(Collectors.joining());

    String graphString = "Nodes: " + System.lineSeparator() + nodesString + System.lineSeparator()
        + "Edges: " + System.lineSeparator() + edgesString;
    return graphString;
  }

  public AgentNode findNode(String name) {
    AgentNode node = nodes.stream().filter(n -> n.getName().equals(name)).findFirst().orElse(null);
    System.out.println(node);
    return node;
  }

  public double averageEdgeWeight() {
    return edges.stream().mapToDouble(edge -> edge.getWeight()).average().getAsDouble();
  }

  /**
   * Generates an output file suitable for input to GraphVis graph generator. Ex: save this output
   * to a file and execute the following command line after installing GraphVis: $> dot -O -Tpng
   * {file}
   * 
   * @return
   */
  public String toDotString(TMDBGraph g, AgentNode targetNode) {
    String dotString = "digraph G {" + System.lineSeparator();

    AgentNode sourceNode = targetNode;
    while (sourceNode.getPredecessor() != null) {
      sourceNode = sourceNode.getPredecessor();
    }

    for (AgentNode n : g.getNodes()) {
      String color = "";
      if (n.equals(sourceNode)) {
        color = "[ style = filled, fillcolor = green ]";
      } else if (n.equals(targetNode)) {
        color = "[ style = filled, fillcolor = red ]";
      }
      dotString += "  " + n.getIdentifier() + System.lineSeparator() + color;
    }

    dotString += System.lineSeparator();

    for (Connection e : g.getEdges()) {
      String color = "";
      // Is this a connecting edge along the shortest path?

      if (e.getSourceNode().equals(e.getTargetNode().getPredecessor())) {
        color = "; color = green; penwidth = 10";
      }
      String label = " [label = \"" + e.getWeight() + "\"" + color + "]";
      dotString += "  " + e.getSourceNode().getIdentifier() + " -> "
          + e.getTargetNode().getIdentifier() + label + System.lineSeparator();
    }
    dotString += "}";
    return dotString;
  }
}
