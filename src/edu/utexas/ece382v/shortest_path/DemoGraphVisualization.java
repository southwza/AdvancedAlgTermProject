package edu.utexas.ece382v.shortest_path;

import java.nio.file.Files;
import java.nio.file.Path;

import edu.utexas.ece382v.shortest_path.entities.Graph;
import edu.utexas.ece382v.shortest_path.util.GraphGenerator;

public class DemoGraphVisualization {

    public static void main(String[] args) throws Exception {
        Long nodes = 64L;
        GraphGenerator graphGenerator = new GraphGenerator();
        Graph g = graphGenerator.generateGraph(nodes, 4, .8F, .6F);

        int oppositeIndex = (int)Math.sqrt(nodes) - 1;
        String sourceNode = "0000.0000";
        String targetNode = String.format("%04d", oppositeIndex) + "." + String.format("%04d", oppositeIndex);

        DeltaSteppingShortestPath ds = new DeltaSteppingShortestPath();
        ds.calculateShortestPath(g, g.findNode(sourceNode), g.findNode(targetNode), 30);

        Graph subGraph = graphGenerator.generateSubGraph(g.findNode(targetNode), 5);
        System.out.println("Nodes in graph: " + g.getNodes().size());
        System.out.println("Nodes in subgraph: " + subGraph.getNodes().size());
        String dotFormat = subGraph.toDotString(subGraph, g.findNode(targetNode));
        Files.write(Path.of("graph.txt"), dotFormat.getBytes());
    }

}
