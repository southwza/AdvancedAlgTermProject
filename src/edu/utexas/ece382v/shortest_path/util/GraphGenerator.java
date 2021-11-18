package edu.utexas.ece382v.shortest_path.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import edu.utexas.ece382v.shortest_path.entities.Edge;
import edu.utexas.ece382v.shortest_path.entities.Graph;
import edu.utexas.ece382v.shortest_path.entities.Node;

public class GraphGenerator {

    Random random = new Random();
    Map<String, Node> matrixPositionToNode = null;
    TreeSet<Node> allNodes = null;
    int graphDimension = 0;

    /**
     * 
     * @param nodes:           approximate number of nodes to generate
     * @param degree:          number of edges to generate per node
     * @param euclidianFactor: value from 0 to 1. 1 => edge weights are just
     *                         euclidean distances, 0 => edge weights between any
     *                         two points have no relation to edge weights between
     *                         any other two points
     * @param edgeWeight       value from 0 to 1. 1 => edges can connect any two
     *                         nodes in the graph. 0 => edges only connect adjacent
     *                         nodes
     * @return
     * @throws Exception
     */
    public Graph generateGraph(Long nodes, int degree, Float euclidianFactor, Float edgeWeight) throws Exception {
        // Will use a strategy of building a graph from an nxn matrix where n is
        // sqrt(nodes). Each element of the
        // matrix will be a list of edges. To ensure the graph is connected, as each new
        // node is added, it must
        // have at least one edge connected to another node that has been added.

        Graph g = new Graph();

        if (euclidianFactor < 0 || euclidianFactor > 1) {
            throw new Exception("euclidianFactor must be between 0 and 1");
        }

        if (edgeWeight < 0 || edgeWeight > 1) {
            throw new Exception("edgeWeight must be between 0 and 1");
        }

        graphDimension = (int) (Double.valueOf(Math.sqrt(nodes)).longValue());

        matrixPositionToNode = new HashMap<>();

        allNodes = new TreeSet<>(Comparator.comparing(node -> Double.valueOf(node.getIdentifier())));

        // Initialize all nodes
        for (long row = 0; row < graphDimension; row++) {
            for (long col = 0; col < graphDimension; col++) {
                String nodeId = String.format("%04d", row) + "." + String.format("%04d", col);
                Node node = new Node(nodeId);
                matrixPositionToNode.put(nodeId, node);
                allNodes.add(node);
            }
        }

        for (Node source : allNodes) {
            Set<Node> targetNodes = findTargetNodes(source, degree, edgeWeight);
            for (Node target : targetNodes) {
                Edge newEdge = new Edge(source, target, calculateEdgeWeight(source, target, euclidianFactor));
                g.addEdge(newEdge);
            }
        }

        g.getNodes().addAll(allNodes);
        return g;

    }

    private Set<Node> findTargetNodes(Node sourceNode, int degree, Float edgeWeight) {

        Set<Node> targetNodes = new HashSet<Node>();

        // 'Search distance' is how far away in x/y that we want to search for a target
        // node.
        // Will define that for now as edgeweight^3 * n in order to try getting a
        // reasonable value.
        int searchDistance = (int) (Math.pow(edgeWeight, 3) * graphDimension) + 2;

        // If n is large, it may mean that we need to increase the search distance in
        // order to find enough nodes to
        // connect to. The total possible nodes that we can connect to 'd' distance in
        // x/y coords is (2d)^2 - 1
        int minSearchDistance = (int) Math.sqrt(Double.valueOf(degree) / 4) + 1;

        String[] coords = sourceNode.getIdentifier().split("\\.");
        int nodeXCoord = Integer.parseInt(coords[0]);
        int nodeYCoord = Integer.parseInt(coords[1]);

        int minX = Math.max(nodeXCoord - searchDistance, 0);
        int maxX = Math.min(nodeXCoord + searchDistance, graphDimension - 1);
        int minY = Math.max(nodeYCoord - searchDistance, 0);
        int maxY = Math.min(nodeYCoord + searchDistance, graphDimension - 1);

        int xScan = maxX - minX;
        int yScan = maxY - minY;

        if (searchDistance < minSearchDistance) {
            searchDistance = minSearchDistance;
        }

        int attempts = 0;
        while (targetNodes.size() < degree && attempts < 2 * degree) {
            int targetXCoord = minX + random.nextInt(xScan + 1);
            int targetYCoord = minY + random.nextInt(yScan + 1);
            Node targetNode = matrixPositionToNode.get(String.format("%04d", targetXCoord) + "." + String.format("%04d", targetYCoord));
            targetNodes.add(targetNode);
            attempts++;
        }




//            List<Node> eligibleNodes = findEligibleTargetNodes(sourceNode, searchDistance);
//            // We just need to return some random subset of all the eligible nodes;
//            while (eligibleNodes.size() > 0 && edgesToCreate > 0) {
//                int randomIndex = random.nextInt(eligibleNodes.size());
//                targetNodes.add(eligibleNodes.remove(randomIndex));
//                edgesToCreate--;
//            }

        return targetNodes;
    }

    private List<Node> findEligibleTargetNodes(Node node, int searchDistance) {
        String[] coords = node.getIdentifier().split("\\.");
        int nodeXCoord = Integer.parseInt(coords[0]);
        int nodeYCoord = Integer.parseInt(coords[1]);

        int minX = Math.max(nodeXCoord - searchDistance, 0);
        int maxX = Math.min(nodeXCoord + searchDistance, graphDimension - 1);
        int minY = Math.max(nodeYCoord - searchDistance, 0);
        int maxY = Math.min(nodeYCoord + searchDistance, graphDimension - 1);

        List<Node> eligibleNodes = new ArrayList<>();
        for (int xpos = minX; xpos <= maxX; xpos++) {
            for (int ypos = minY; ypos <= maxY; ypos++) {
                if (nodeXCoord == xpos && nodeYCoord == ypos) {
                    //Let's not allow edges with the same source and target node
                    continue;
                }
                Node eligibleNode = matrixPositionToNode.get(String.format("%04d", xpos) + "." + String.format("%04d", ypos));
                eligibleNodes.add(eligibleNode);
            }
        }
//System.out.println("Found " + eligibleNodes.size() + "eligible nodes");
        return eligibleNodes;
    }

    private Double calculateEdgeWeight(Node node, Node targetNode, Float euclideanFactor) {
        String[] nodeStringCoords = node.getIdentifier().split("\\.");
        String[] targetNodeStringCoords = targetNode.getIdentifier().split("\\.");

        int source_X = Integer.parseInt(nodeStringCoords[0]);
        int source_Y = Integer.parseInt(nodeStringCoords[1]);

        int target_X = Integer.parseInt(targetNodeStringCoords[0]);
        int target_Y = Integer.parseInt(targetNodeStringCoords[1]);

        // Let's calculate the distance between the two nodes
        Double euclideanComponent = Math.sqrt(Math.pow(source_X - target_X, 2) + Math.pow(source_Y - target_Y, 2));

        // Let's calculate some noise:
        Double randomComponent = random.nextDouble() * graphDimension;

        return (euclideanFactor * euclideanComponent) + (1 - euclideanFactor) * randomComponent;
    }

    /**
     * Given an input graph, generates a sub-graph which includes the specified target node, all
     * nodes that are 'degree' degrees of separation, and all edges that connect these nodes. Will
     * use this reduced graph to generate a visualization of a path in the graph
     * @param g
     * @param target
     * @param degree
     * @return
     */
    public Graph generateSubGraph(Node target, int degree) {
        Graph outputGraph = new Graph();
        Node node = target;

        //First, let's add all the nodes from the source to the target.
        outputGraph.getNodes().add(target);
        while (node.getPredecessor()!= null) {
            node = node.getPredecessor();
            outputGraph.getNodes().add(node);
        }

        //let's add all the connected nodes (up to the specified degree). We're doing this in a
        //separate while loop because we want to remove any 'predecessor' node specification to
        //any of these nodes that we add. Since we will have already added all of the predecessor
        //nodes from our target, then we won't accidentally remove a predecessor along our main path.
        node = target;
        while (node.getPredecessor()!= null) {
            addOutgoingNodes(outputGraph, node, degree);
            node = node.getPredecessor();
        }

        //Now we need to handle edges
        for (Node n : outputGraph.getNodes()) {
            List<Edge> edgesToRemove = new ArrayList<>();

            for (Edge e : n.getOutgoingEdges()) {
                if (outputGraph.getNodes().contains(e.getTargetNode())) {
                    outputGraph.getEdges().add(e);
                } else {
                    edgesToRemove.add(e);
                }
            }
            edgesToRemove.forEach(e -> n.getOutgoingEdges().remove(e));

            edgesToRemove.clear();
            for (Edge e : n.getIncomingEdges()) {
                if (!outputGraph.getNodes().contains(e.getSourceNode())) {
                    edgesToRemove.add(e);
                }
            }
            edgesToRemove.forEach(e -> n.getIncomingEdges().remove(e));
        }

        return outputGraph;
    }

    private void addOutgoingNodes(Graph outputGraph, Node node, int degree) {
        //Add this node
        if (!outputGraph.getNodes().contains(node)) {
            node.setPredecessor(null);
            outputGraph.getNodes().add(node);
        }
        //Base case: the degree is 1, don't add any additional nodes.
        if (degree <= 1) {
            return;
        }

        //recursive step: add child nodes
        node.getOutgoingEdges().forEach(edge -> {
            addOutgoingNodes(outputGraph, edge.getTargetNode(), degree - 1);
        });

    }
}