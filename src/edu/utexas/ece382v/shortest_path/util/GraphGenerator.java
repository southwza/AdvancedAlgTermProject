package edu.utexas.ece382v.shortest_path.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import edu.utexas.ece382v.shortest_path.entities.Edge;
import edu.utexas.ece382v.shortest_path.entities.Graph;
import edu.utexas.ece382v.shortest_path.entities.Node;

public class GraphGenerator {

    Random random = new Random();
    Map<String, Node> matrixPositionToNode = null;
    TreeSet<Node> connectedNodes = null;
    TreeSet<Node> unconnectedNodes = null;
    int graphDimension = 0;

    /**
     * 
     * @param nodes approximate number of nodes to generate
     * @param degree number of edges to generate per node
     * @param euclidianFactor value from 0 to 1. 1 => edge weights are just euclidean distances, 0 => edge weights between any two points have no relation to edge weights between any other two points
     * @param edgeWeight value from 0 to 1. 1 => edges can connect any two nodes in the graph. 0 => edges only connect adjacent nodes
     * @return
     * @throws Exception
     */
    public Graph generateGraph(Long nodes, int degree, Float euclidianFactor, Float edgeWeight) throws Exception {
        //Will use a strategy of building a graph from an nxn matrix where n is sqrt(nodes). Each element of the
        //matrix will be a list of edges. To ensure the graph is connected, as each new node is added, it must
        //have at least one edge connected to another node that has been added.

        Graph g = new Graph();

        if (euclidianFactor < 0 || euclidianFactor > 1) {
            throw new Exception("euclidianFactor must be between 0 and 1");
        }

        if (edgeWeight < 0 || edgeWeight > 1) {
            throw new Exception("edgeWeight must be between 0 and 1");
        }

        graphDimension = (int) (Double.valueOf(Math.sqrt(nodes)).longValue());

        matrixPositionToNode = new HashMap<>();

        connectedNodes = new TreeSet<>(Comparator.comparing(node -> Double.valueOf(node.getIdentifier())));
        unconnectedNodes = new TreeSet<>(Comparator.comparing(node -> Double.valueOf(node.getIdentifier())));


        //Initialize all nodes and add them to the unconnected set
        for (long row = 0; row < graphDimension; row++) {
            for (long col = 0; col < graphDimension; col++) {
                String nodeId = String.format("%04d", row) + "." + String.format("%04d", col);
                Node node = new Node(nodeId);
                matrixPositionToNode.put(nodeId, node);
                unconnectedNodes.add(node);
            }
        }

        while (!unconnectedNodes.isEmpty()) {
            Node node = unconnectedNodes.pollFirst();
            Set<Node> targetNodes = findTargetNodes(node, degree, edgeWeight);

            for (Node targetNode : targetNodes) {
                Edge newEdge = new Edge(node, targetNode, calculateEdgeWeight(node, targetNode, euclidianFactor));
                node.getEdges().add(newEdge);
                targetNode.getEdges().add(newEdge);
                g.getEdges().add(newEdge);
                if (targetNode.getEdges().size() == degree) {
                    unconnectedNodes.remove(targetNode);
                    connectedNodes.add(targetNode);
                }
            }
            connectedNodes.add(node);
        }

        g.getNodes().addAll(connectedNodes);
        return g;
    }


    private Double calculateEdgeWeight(Node node, Node targetNode, Float euclideanFactor) {
        String[] nodeStringCoords = node.getIdentifier().split("\\.");
        String[] targetNodeStringCoords = targetNode.getIdentifier().split("\\.");

        int source_X = Integer.parseInt(nodeStringCoords[0]);
        int source_Y = Integer.parseInt(nodeStringCoords[1]);

        int target_X = Integer.parseInt(targetNodeStringCoords[0]);
        int target_Y = Integer.parseInt(targetNodeStringCoords[1]);

        //Let's calculate the distance between the two nodes
        Double euclideanComponent = Math.sqrt(Math.pow(source_X - target_X, 2) + Math.pow(source_Y - target_Y, 2));

        //Let's calculate some noise:
        Double randomComponent = random.nextDouble() * graphDimension;

        return (euclideanFactor * euclideanComponent) + (1-euclideanFactor) * randomComponent;
    }


    private Set<Node> findTargetNodes(Node node, int degree, Float edgeWeight) {

        Set<Node> targetNodes = new HashSet<Node>();

        int edgesToCreate = degree - node.getEdges().size();

        //'Search distance' is how far away in x/y that we want to search for a target node.
        //Will define that for now as edgeweight^3 * n in order to try getting a reasonable value.
        int searchDistance = (int) (Math.pow(edgeWeight, 3) * graphDimension);

        //If n is large, it may mean that we need to increase the search distance in order to find enough nodes to
        //connect to. The total possible nodes that we can connect to 'd' distance in x/y coords is (2d)^2 - 1
        int minSearchDistance = (int) Math.sqrt(Double.valueOf(degree)/4);
        if (searchDistance < minSearchDistance) {
            searchDistance = minSearchDistance;
        }

        if (edgesToCreate > 0) {
            List<Node> eligibleNodes = findEligibleTargetNodes(node, searchDistance);

            //We just need to return some random subset of all the eligible nodes;
            while (eligibleNodes.size() > 0 && edgesToCreate > 0) {
                int randomIndex = random.nextInt(eligibleNodes.size());
                targetNodes.add(eligibleNodes.remove(randomIndex));
                edgesToCreate--;
            }
        }

        return targetNodes;
    }


    private List<Node> findEligibleTargetNodes(Node node, int searchDistance) {
        String[] coords = node.getIdentifier().split("\\.");
        int nodeXCoord = Integer.parseInt(coords[0]);
        int nodeYCoord = Integer.parseInt(coords[1]);

        int minX = Math.max(nodeXCoord - searchDistance, 0);
        int maxX = Math.min(nodeXCoord + searchDistance, graphDimension);
        int minY = Math.max(nodeYCoord - searchDistance, 0);
        int maxY = Math.min(nodeYCoord + searchDistance, graphDimension);

        //Stream over unconnectedNodes to find a set of eligible nodes;
        List<Node> eligibleNodes = unconnectedNodes.stream()
                .map(searchNode -> searchNode.getIdentifier().split("\\."))
                .map(stringCoords -> { //Map the node into it's coordinate set
                    int targetX = Integer.parseInt(stringCoords[0]);
                    int targetY = Integer.parseInt(stringCoords[1]);
                    return List.of(targetX, targetY);
                })
                .filter(c -> c.get(0) >= minX && c.get(0) <= maxX && c.get(1) >= minY && c.get(1) <= maxY)
                .map(c -> matrixPositionToNode.get(String.format("%04d", c.get(0)) + "." + String.format("%04d", c.get(1))))
                .collect(Collectors.toList());

        return eligibleNodes;
    }

}