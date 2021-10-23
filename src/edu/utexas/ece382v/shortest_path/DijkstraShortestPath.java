package edu.utexas.ece382v.shortest_path;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import edu.utexas.ece382v.shortest_path.entities.Edge;
import edu.utexas.ece382v.shortest_path.entities.Graph;
import edu.utexas.ece382v.shortest_path.entities.Node;

public class DijkstraShortestPath {

    public static void main(String[] args) {

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

        System.out.println("shortest path from node1 to node2: " + dijkstraShortestPath(g, node_1, node_2));
        System.out.println("shortest path from node1 to node3: " + dijkstraShortestPath(g, node_1, node_3));
        System.out.println("shortest path from node1 to node4: " + dijkstraShortestPath(g, node_1, node_4));
        System.out.println("shortest path from node1 to node5: " + dijkstraShortestPath(g, node_1, node_5));
        System.out.println("shortest path from node5 to node1: " + dijkstraShortestPath(g, node_5, node_1));
        System.out.println("shortest path from node4 to node1: " + dijkstraShortestPath(g, node_4, node_1));
        System.out.println("shortest path from node3 to node1: " + dijkstraShortestPath(g, node_3, node_1));
        System.out.println("shortest path from node2 to node1: " + dijkstraShortestPath(g, node_2, node_1));
    }

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
        PriorityQueue<Node> minHeap = new PriorityQueue<>(Comparator.comparing(Node::getWeight));

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
                    .filter(edge -> !fixedNodes.contains(edge.getTarget(fixedNode))) // Remove those already fixed
                    .collect(Collectors.toList());

            for (Edge edge : newEdges) {
                Node targetNode = edge.getTarget(fixedNode);

                if (targetNode.getWeight() != null && (targetNode.getWeight() <= fixedNode.getWeight() + edge.getWeight())) {
                 // in this case, the edge is already in heap and cannot be relaxed
                    continue;
                }

                if (targetNode.getWeight() != null) {
                    // In this case, the element has already been added to the heap and we need to
                    // remove it and add it back in to maintain the heap invariant.
                    // Currently this is not very efficient O(n)
                    minHeap.remove(targetNode);
                }
                targetNode.setWeight(fixedNode.getWeight() + edge.getWeight());
                minHeap.add(targetNode);

            }
        }

        return target.getWeight();
    }
}
