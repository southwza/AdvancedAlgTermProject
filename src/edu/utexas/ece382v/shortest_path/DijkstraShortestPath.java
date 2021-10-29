package edu.utexas.ece382v.shortest_path;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.utexas.ece382v.shortest_path.data_structure.DijkstraHeap;
import edu.utexas.ece382v.shortest_path.entities.Edge;
import edu.utexas.ece382v.shortest_path.entities.Graph;
import edu.utexas.ece382v.shortest_path.entities.Node;

public class DijkstraShortestPath {

    /**
     * 
     * @param g
     * @param source
     * @param target
     * @return the weight of the shortest path from the source to the target in g, or otherwise null
     */
    public static Double dijkstraShortestPath(Graph g, Node source, Node target) {
        // All node weights should be null at the start of this method.
        g.getNodes().stream() //
                .forEach(node -> node.setWeight(null));

        Set<Node> fixedNodes = new HashSet<>();
        DijkstraHeap<Node> minHeap = new DijkstraHeap<>();

        // initialize the heap
        source.setWeight(0D);
        minHeap.add(source);

        while (!minHeap.isEmpty()) {
            Node fixedNode = minHeap.poll();
            fixedNodes.add(fixedNode);
            if (fixedNodes.contains(target)) {
                //We can stop once the target node is fixed
                break;
            }

            List<Edge> newEdges = g.getOutgoingEdges(fixedNode).stream() // Stream over outgoing edges
                    .filter(edge -> !fixedNodes.contains(edge.getTargetNode())) // Remove those already fixed
                    .collect(Collectors.toList());

            for (Edge edge : newEdges) {
                Node targetNode = edge.getTargetNode();

                if (targetNode.getWeight() != null && (targetNode.getWeight() <= fixedNode.getWeight() + edge.getWeight())) {
                    // in this case, the target node edge is already in heap and cannot be relaxed
                    // because there is already a shorter path to it
                    continue;
                }

                targetNode.setWeight(fixedNode.getWeight() + edge.getWeight());
                minHeap.insertOrAdjust(targetNode);

            }
        }

        return target.getWeight();
    }
}
