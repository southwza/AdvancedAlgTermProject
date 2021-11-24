package edu.utexas.ece382v.shortest_path;

import edu.utexas.ece382v.shortest_path.entities.Graph;
import edu.utexas.ece382v.shortest_path.util.GraphGenerator;

public class DemoShortestPathAlgorithms {

    public static void main(String[] args) throws Exception {
        //Generate a graph
        Long nodes = 50000L;
        GraphGenerator graphGenerator = new GraphGenerator();
        long start = System.nanoTime();
        Graph g = graphGenerator.generateGraph(nodes, 6, .8F, .1F);
        long end = System.nanoTime();
        long graphGenTime = end - start;
        int warmupExecutions = 3;
        int testExecutions = 10;
        long totalTime = 0;

        int totalIterations = warmupExecutions + testExecutions;


        //Choose two nodes that are likely to have some distance between them
        int oppositeIndex = (int)Math.sqrt(nodes) - 1;
        String sourceNode = "0000.0000";
        String targetNode = String.format("%04d", oppositeIndex) + "." + String.format("%04d", oppositeIndex);

        //Run Dijkstra's Algorithm
        System.out.println("Testing Dijkstra's Algorithm");
        for (int i = 0; i < totalIterations; i++) {
            start = System.nanoTime();
            Double shortestPathDist = DijkstraShortestPath.dijkstraShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode));
            end = System.nanoTime();
            //Discard the first few warm-up executions as the JVM optimizes the execution
            if (i >= warmupExecutions) {
                totalTime += (end - start);
                System.out.println("Dijkstra execution #" + i + ". Shortest path: " + shortestPathDist + " calculated in " + (end - start)/1000000 + "ms");
            } else {
                System.out.println("Dijkstra warm-up execution #" + i + ". Shortest path: " + shortestPathDist + " calculated in " + (end - start)/1000000 + "ms");
            }
        }
        long avgTimeForDijkstra = totalTime / (testExecutions - warmupExecutions);


        //Run Delta-Stepping
        System.out.println("Testing Delta Stepping Algorithm");
        DeltaSteppingShortestPath ds = new DeltaSteppingShortestPath();
        totalTime = 0;
        for (int i = 0; i < totalIterations; i++) {
            Double delta = 10D;
            start = System.nanoTime();
            Double deltaSteppingShortestPathDist = ds.calculateShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode), delta, true, 12);
            end = System.nanoTime();
            if (i >= warmupExecutions) {
                totalTime += (end - start);
                System.out.println("Delta Stepping execution #" + i + ". Shortest path with delta of " + delta + ": " + deltaSteppingShortestPathDist + " calculated in " + (end - start)/1000000 + "ms" + System.lineSeparator());
            } else {
                System.out.println("Delta Stepping warm-up execution #" + i + ". Shortest path with delta of " + delta + ": " + deltaSteppingShortestPathDist + " calculated in " + (end - start)/1000000 + "ms" + System.lineSeparator());
            }
        }
        long avgTimeForDeltaStepping = totalTime / (testExecutions - warmupExecutions);

        //Run Bellman-Ford
        System.out.println("Testing Bellman-Ford Algorithm");
        BellmanFordShortestPath bf = new BellmanFordShortestPath();
        totalTime = 0;
        for (int i = 0; i < totalIterations; i++) {
            start = System.nanoTime();
            Double bellmanFordShortestPathDist = bf.calculateShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode), true, 12);
            end = System.nanoTime();
            if (i >= warmupExecutions) {
                totalTime += (end - start);
                System.out.println("Bellman-Ford execution #" + i + ". Shortest path: " + bellmanFordShortestPathDist + " calculated in " + (end - start)/1000000 + "ms" + System.lineSeparator());
            } else {
                System.out.println("Bellman-Ford warm-up execution #" + i + ". Shortest path: " + bellmanFordShortestPathDist + " calculated in " + (end - start)/1000000 + "ms" + System.lineSeparator());
            }
        }
        long avgTimeForBellmanFord = totalTime / (testExecutions - warmupExecutions);

        //Print summary results
        System.out.println("----- RESULTS -----");
        System.out.println("Time taken to generate a graph with " + nodes + " nodes with average edge weight of " + g.averageEdgeWeight() + ": " + graphGenTime / 1000000 + "ms");
        System.out.println("Calculated shortest path from " + sourceNode + " to " + targetNode);
        System.out.println("Dijkstra algorithm average execution time: " + avgTimeForDijkstra / 1000000 + "ms");
        System.out.println("Delta Stepping algorithm average execution time: " + avgTimeForDeltaStepping / 1000000 + "ms");
        System.out.println("Bellman-Ford algorithm average execution time: " + avgTimeForBellmanFord / 1000000 + "ms");
    }
}
