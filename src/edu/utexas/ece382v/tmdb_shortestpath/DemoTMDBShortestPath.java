package edu.utexas.ece382v.tmdb_shortestpath;

import edu.utexas.ece382v.tm_reader.TMDBGraph;
import edu.utexas.ece382v.tm_reader.TMDBReader;

public class DemoTMDBShortestPath {
  public static void main(String[] args) throws Exception {
    TMDBReader TMDBReader = new TMDBReader();
    long start = System.nanoTime();
    TMDBGraph g = TMDBReader.generateGraph();
    long end = System.nanoTime();
    System.out.println("Time taken to import and generate graph from tmdb dataset: "
        + (end - start) / 1000000 + "ms" + System.lineSeparator());

    String sourceNode = "Luke Wilson";
    String targetNode = "Owen Wilson";

    // start = System.nanoTime();
    // Double shortestPathDist =
    // DijkstraTMDB.dijkstraShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode));
    // end = System.nanoTime();
    // System.out.println(
    // "Dijkstra shortest path from " + sourceNode + " to " + targetNode + ": " + shortestPathDist
    // + " calculated in " + (end - start) / 1000000 + "ms" + System.lineSeparator());

    DeltaSteppingTMDB ds = new DeltaSteppingTMDB();

    Double delta = 30D;
    start = System.nanoTime();
    Double deltaSteppingShortestPathDist =
        ds.calculateShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode), delta);
    end = System.nanoTime();
    System.out.println(
        "Delta Stepping shortest path with delta of " + delta + ": " + deltaSteppingShortestPathDist
            + " calculated in " + (end - start) / 1000000 + "ms" + System.lineSeparator());

    // BellmanFordTMDB bf = new BellmanFordTMDB();
    // start = System.nanoTime();
    // Double bellmanFordShortestPathDist =
    // bf.calculateShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode));
    // end = System.nanoTime();
    // System.out.println("Bellman-Ford shortest path: " + bellmanFordShortestPathDist
    // + " calculated in " + (end - start) / 1000000 + "ms" + System.lineSeparator());
  }
}
