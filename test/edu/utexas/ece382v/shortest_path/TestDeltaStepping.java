package edu.utexas.ece382v.shortest_path;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.utexas.ece382v.shortest_path.entities.Graph;
import edu.utexas.ece382v.shortest_path.entities.Node;
import edu.utexas.ece382v.shortest_path.util.GraphGenerator;

public class TestDeltaStepping {
    @Test
    void testDeltaStepping() throws Exception {
        Long nodes = 40000L;

        GraphGenerator graphGenerator = new GraphGenerator();
        Graph g = graphGenerator.generateGraph(nodes, 4, .8F, .4F);

        int oppositeIndex = (int)Math.sqrt(nodes) - 1;
        String sourceNode = "0000.0000";
        String targetNode = String.format("%04d", oppositeIndex) + "." + String.format("%04d", oppositeIndex);

        Double shortestPathDist = DijkstraShortestPath.dijkstraShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode));
        System.out.println("Dijkstra shortest path from " + sourceNode + " to " + targetNode + ": " + shortestPathDist + System.lineSeparator());

        DeltaSteppingShortestPath ds = new DeltaSteppingShortestPath();

        Double delta = 30D;
        Double deltaSteppingShortestPathDist = ds.calculateShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode), delta);
        System.out.println("Delta Stepping shortest path with delta of " + delta + ": " + deltaSteppingShortestPathDist + System.lineSeparator());

        assertEquals(shortestPathDist, deltaSteppingShortestPathDist);
    }

    @Test
    void testDeltaStepping2() throws Exception {

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

        DeltaSteppingShortestPath ds = new DeltaSteppingShortestPath();

        assertEquals(22, ds.calculateShortestPath(g, node_1, node_2, 2D));
        assertEquals(32, ds.calculateShortestPath(g, node_1, node_3, 2D));
        assertEquals(39, ds.calculateShortestPath(g, node_1, node_4, 2D));
        assertEquals(77, ds.calculateShortestPath(g, node_1, node_5, 2D));
    }
}
