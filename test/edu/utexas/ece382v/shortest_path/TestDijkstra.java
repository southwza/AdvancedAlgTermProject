package edu.utexas.ece382v.shortest_path;

import org.junit.jupiter.api.Test;

import edu.utexas.ece382v.shortest_path.entities.Graph;
import edu.utexas.ece382v.shortest_path.util.GraphGenerator;

public class TestDijkstra {
    @Test
    void testDijkstra1() throws Exception {
        GraphGenerator graphGenerator = new GraphGenerator();
        Graph g = graphGenerator.generateGraph(2500L, 8, 1F, .9F);
        System.out.println(g.toString());
        //Calculate the shortest distance from node 0.0 to node 49.49

        Double shortestPathDist = DijkstraShortestPath.dijkstraShortestPath(g, g.findNode("0000.0000"), g.findNode("0049.0049"));
        System.out.println("Shortest path: " + shortestPathDist);
    }

}
