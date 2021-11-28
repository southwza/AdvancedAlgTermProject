package edu.utexas.ece382v.tmdb_shortestpath;

import java.util.ArrayList;
import edu.utexas.ece382v.tm_reader.AgentNode;
import edu.utexas.ece382v.tm_reader.Connection;
import edu.utexas.ece382v.tm_reader.TMDBGraph;
import edu.utexas.ece382v.tm_reader.TMDBReader;

public class ComputeAllBaconPaths {
  // TODO: Copy path to nodes during algorithm execution. This is expensive to do after the fact.
  public static void printPath(AgentNode target) {
    AgentNode pathNode = target;
    ArrayList<Connection> path = new ArrayList<Connection>();
    while (pathNode.getPredecessor() != null) {
      for (Connection edge : pathNode.getIncomingEdges()) {
        if (edge.getSourceNode().getIdentifier()
            .equals(pathNode.getPredecessor().getIdentifier())) {
          path.add(0, edge);
        }
      }
      pathNode = pathNode.getPredecessor();
    }
    for (Connection edge : path) {
      System.out.println(edge);
    }
  }

  // TODO: Copy path to nodes during algorithm execution. This is expensive to do after the fact.
  public static void computePath(AgentNode target) {
    AgentNode pathNode = target;
    ArrayList<Connection> path = new ArrayList<Connection>();
    while (pathNode.getPredecessor() != null) {
      for (Connection edge : pathNode.getIncomingEdges()) {
        if (edge.getSourceNode().getIdentifier()
            .equals(pathNode.getPredecessor().getIdentifier())) {
          path.add(0, edge);
        }
      }
      pathNode = pathNode.getPredecessor();
    }
    if (path.size() == 0) {
      System.out.println("Path length is zero between Kevin Bacon and " + target.getName());
    } else if (path.size() > 5) {
      System.out.println(
          "Path length is " + path.size() + " between Kevin Bacon and " + target.getName());
    }
  }

  public static void main(String[] args) throws Exception {
    System.out.println(
        "Welcome to the Kevin Bacon Path Detector. It is not advised that you run this to completion.");
    System.out.println("This is a discovery tool for long and non-existent paths to Kevin Bacon.");
    System.out.println("Please wait a moment while we load the data");
    TMDBGraph g = TMDBReader.generateGraph();
    System.out.println("The calculator is computing all paths to Kevin Bacon...");

    double start = System.nanoTime();

    AgentNode kevin = g.getNodes().stream().filter(n -> n.getName().equalsIgnoreCase("Kevin Bacon"))
        .findFirst().orElse(null);
    int i = 0;
    for (AgentNode agent : g.getNodes()) {
      if (i % 2500 == 0) {
        System.out.println("Computing Delta Stepping: " + i + "...");
      }
      DeltaSteppingTMDB ds = new DeltaSteppingTMDB();
      Double delta = 5D;
      Double deltaSteppingShortestPathDist = ds.calculateShortestPath(g, kevin, agent, delta);
      // computePath(agent);
      if (deltaSteppingShortestPathDist == null) {
        System.out.println("No path to " + agent.getName() + " found");

      } else if (deltaSteppingShortestPathDist > 12) {
        System.out.println(
            "Path to " + agent.getName() + " is higher than 12 @" + deltaSteppingShortestPathDist);
      }
      i = i + 1;
    }

    double end = System.nanoTime();
    System.out.println("----Done----");
    System.out.println("Completed in " + (end - start));
  }
}
