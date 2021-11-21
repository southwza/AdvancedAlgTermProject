package edu.utexas.ece382v.tmdb_shortestpath;

import java.util.ArrayList;
import edu.utexas.ece382v.tm_reader.AgentNode;
import edu.utexas.ece382v.tm_reader.Connection;
import edu.utexas.ece382v.tm_reader.TMDBGraph;
import edu.utexas.ece382v.tm_reader.TMDBReader;

public class DemoTMDBShortestPath {
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
    TMDBReader TMDBReader = new TMDBReader();
    long start = System.nanoTime();
    TMDBGraph g = TMDBReader.generateGraph();
    long end = System.nanoTime();
    System.out.println("Time taken to import and generate graph from tmdb dataset: "
        + (end - start) / 1000000 + "ms" + System.lineSeparator());
    System.out.println(g.getNodes().size() + " Nodes and " + g.getEdges().size() + " Edges");

    String sourceNode = "Kevin Bacon";
    String targetNode = "Terry Crews";

    System.out.println();
    System.out.println("-----------------------");
    System.out.println("Evaluating the path from " + sourceNode + " to " + targetNode);
    System.out.println(sourceNode + " has " + g.findNode(sourceNode).getOutgoingEdges().size()
        + " outgoing edges and " + g.findNode(sourceNode).getIncomingEdges().size()
        + " incoming edges");

    System.out.println(targetNode + " has " + g.findNode(targetNode).getOutgoingEdges().size()
        + " outgoing edges and " + g.findNode(targetNode).getIncomingEdges().size()
        + " incoming edges");

    System.out.println("-----------------------");
    System.out.println();

    start = System.nanoTime();
    Double shortestPathDist =
        DijkstraTMDB.dijkstraShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode));
    end = System.nanoTime();
    printPath(g.findNode(targetNode));
    System.out.println(
        "Dijkstra shortest path from " + sourceNode + " to " + targetNode + ": " + shortestPathDist
            + " calculated in " + (end - start) / 1000000 + "ms" + System.lineSeparator());

    DeltaSteppingTMDB ds = new DeltaSteppingTMDB();

    Double delta = 1D;
    start = System.nanoTime();
    Double deltaSteppingShortestPathDist =
        ds.calculateShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode), delta);
    end = System.nanoTime();
    printPath(g.findNode(targetNode));
    System.out.println(
        "Delta Stepping shortest path with delta of " + delta + ": " + deltaSteppingShortestPathDist
            + " calculated in " + (end - start) / 1000000 + "ms" + System.lineSeparator());

    BellmanFordTMDB bf = new BellmanFordTMDB();
    start = System.nanoTime();
    Double bellmanFordShortestPathDist =
        bf.calculateShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode));
    end = System.nanoTime();
    printPath(g.findNode(targetNode));
    System.out.println("Bellman-Ford shortest path: " + bellmanFordShortestPathDist
        + " calculated in " + (end - start) / 1000000 + "ms" + System.lineSeparator());
  }
}
