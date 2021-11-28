package edu.utexas.ece382v.tmdb_shortestpath;

import java.util.ArrayList;
import java.util.Scanner;
import edu.utexas.ece382v.tm_reader.AgentNode;
import edu.utexas.ece382v.tm_reader.Connection;
import edu.utexas.ece382v.tm_reader.TMDBGraph;
import edu.utexas.ece382v.tm_reader.TMDBReader;

public class DemoSixDegrees {
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

  public static void main(String[] args) throws Exception {
    System.out.println("Welcome to the 'Weighted' Six Degrees of Separation calculator!");
    System.out.println("Please wait a moment while we load the data");
    long start = System.nanoTime();
    TMDBGraph g = TMDBReader.generateGraph();
    long end = System.nanoTime();
    System.out.println("Time taken to import and generate graph from tmdb dataset: "
        + (end - start) / 1000000 + "ms");
    System.out.println("Imported graph contains " + g.getNodes().size() + " Nodes and "
        + g.getEdges().size() + " Edges" + System.lineSeparator());

    System.out.println("Given two actors, the 'Weighted' Six Degrees of Separation calculator "
        + System.lineSeparator()
        + "will calculate the total distance between the two actors, where 'distance' is "
        + System.lineSeparator()
        + "how far apart the actors appear in the credits of shared movies!"
        + System.lineSeparator());

    Scanner in = new Scanner(System.in);
    while ("a".equals("a")) {
      AgentNode sourceNode = readActor(g, in);
      AgentNode targetNode = readActor(g, in);

      System.out.println();
      System.out.println("-----------------------");
      System.out.println("Evaluating the path from " + sourceNode + " to " + targetNode);
      System.out.println(sourceNode + " has " + sourceNode.getOutgoingEdges().size()
          + " outgoing edges and " + sourceNode.getIncomingEdges().size() + " incoming edges");

      System.out.println(targetNode + " has " + targetNode.getOutgoingEdges().size()
          + " outgoing edges and " + targetNode.getIncomingEdges().size() + " incoming edges");

      System.out.println("-----------------------");
      System.out.println();

      DeltaSteppingTMDB ds = new DeltaSteppingTMDB();

      Double delta = 1D;
      start = System.nanoTime();
      Double deltaSteppingShortestPathDist =
          ds.calculateShortestPath(g, sourceNode, targetNode, delta);
      end = System.nanoTime();
      System.out.println(System.lineSeparator() + "----------------");
      System.out.println("The shortest distance between " + sourceNode.getName() + " and "
          + targetNode.getName() + " is " + deltaSteppingShortestPathDist + ": ");
      printPath(targetNode);
      System.out.println("----------------");
      System.out.println(System.lineSeparator() + "Try again!");

    }
  }

  private static AgentNode readActor(TMDBGraph g, Scanner in) {
    AgentNode node = null;
    while (node == null) {
      System.out.println("Enter the name of an actor or type \"quit\" to exit: ");
      String name = in.nextLine();
      // Handle a quit signal
      if (name.equalsIgnoreCase("quit")) {
        System.out.println("---- EXIT ----");
        System.exit(1);
      }
      // Let's make sure the name exists in the database:
      node = g.getNodes().stream().filter(n -> n.getName().equalsIgnoreCase(name)).findFirst()
          .orElse(null);
      if (node != null) {
        return node;
      } else {
        System.out.println("Sorry, " + name + " is not in the database. Try again.");
      }
    }
    return null;
  }
}
