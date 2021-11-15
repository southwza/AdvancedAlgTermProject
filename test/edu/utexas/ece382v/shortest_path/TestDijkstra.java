package edu.utexas.ece382v.shortest_path;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.utexas.ece382v.shortest_path.entities.Graph;
import edu.utexas.ece382v.shortest_path.entities.Node;
import edu.utexas.ece382v.shortest_path.util.GraphGenerator;

public class TestDijkstra {
    @Test
    void testDijkstra1() throws Exception {
        GraphGenerator graphGenerator = new GraphGenerator();
        Graph g = graphGenerator.generateGraph(2500L, 8, 1F, .9F);
        // System.out.println(g.toString());
        //Calculate the shortest distance from node 0.0 to node 49.49

        Double shortestPathDist = DijkstraShortestPath.dijkstraShortestPath(g, g.findNode("0000.0000"), g.findNode("0049.0049"));
        System.out.println("Shortest path: " + shortestPathDist);
    }

    @Test
    void testDijkstra2() throws Exception {

        Graph g = new Graph();

        Node node_1 = new Node("1");
        Node node_2 = new Node("2");
        Node node_3 = new Node("3");
        Node node_4 = new Node("4");
        Node node_5 = new Node("5");

        g.setNodes(List.of(node_1, node_2, node_3, node_4, node_5));

        g.addEdge(node_1, node_2, 22);
        g.addEdge(node_1, node_4, 45);

        g.addEdge(node_2, node_3, 10);
        g.addEdge(node_2, node_5, 75);

        g.addEdge(node_3, node_4, 7);
        g.addEdge(node_3, node_5, 45);

        g.addEdge(node_4, node_5, 80);

        assertEquals(22, DijkstraShortestPath.dijkstraShortestPath(g, node_1, node_2));
        assertEquals(32, DijkstraShortestPath.dijkstraShortestPath(g, node_1, node_3));
        assertEquals(39, DijkstraShortestPath.dijkstraShortestPath(g, node_1, node_4));
        assertEquals(77, DijkstraShortestPath.dijkstraShortestPath(g, node_1, node_5));
    }
}
