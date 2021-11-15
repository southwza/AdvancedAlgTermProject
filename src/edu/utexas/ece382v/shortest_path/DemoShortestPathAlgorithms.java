package edu.utexas.ece382v.shortest_path;

import edu.utexas.ece382v.shortest_path.entities.Graph;
import edu.utexas.ece382v.shortest_path.util.GraphGenerator;

public class DemoShortestPathAlgorithms {

    public static void main(String[] args) throws Exception {
        Long nodes = 160000L;
        GraphGenerator graphGenerator = new GraphGenerator();
        long start = System.nanoTime();
        Graph g = graphGenerator.generateGraph(nodes, 4, .8F, .1F);
        long end = System.nanoTime();
        System.out.println("Time taken to generate a graph with " + nodes + " nodes with average edge weight of " + g.averageEdgeWeight() + ": " + (end - start) / 1000000 + "ms" + System.lineSeparator());

        int oppositeIndex = (int)Math.sqrt(nodes) - 1;
        String sourceNode = "0000.0000";
        String targetNode = String.format("%04d", oppositeIndex) + "." + String.format("%04d", oppositeIndex);

        start = System.nanoTime();
        Double shortestPathDist = DijkstraShortestPath.dijkstraShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode));
        end = System.nanoTime();
        System.out.println("Dijkstra shortest path from " + sourceNode + " to " + targetNode + ": " + shortestPathDist + " calculated in " + (end - start)/1000000 + "ms" + System.lineSeparator());

        DeltaSteppingShortestPath ds = new DeltaSteppingShortestPath();

        Double delta = 30D;
        start = System.nanoTime();
        Double deltaSteppingShortestPathDist = ds.calculateShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode), delta);
        end = System.nanoTime();
        System.out.println("Delta Stepping shortest path with delta of " + delta + ": " + deltaSteppingShortestPathDist + " calculated in " + (end - start)/1000000 + "ms" + System.lineSeparator());

        BellmanFordShortestPath bf = new BellmanFordShortestPath();
        start = System.nanoTime();
        Double bellmanFordShortestPathDist = bf.calculateShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode));
        end = System.nanoTime();
        System.out.println("Bellman-Ford shortest path: " + bellmanFordShortestPathDist + " calculated in " + (end - start)/1000000 + "ms" + System.lineSeparator());
    }
}
