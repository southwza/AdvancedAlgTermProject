package edu.utexas.ece382v.tmdb_shortestpath;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import edu.utexas.ece382v.shortest_path.data_structure.DijkstraHeap;
import edu.utexas.ece382v.tm_reader.AgentNode;
import edu.utexas.ece382v.tm_reader.Connection;
import edu.utexas.ece382v.tm_reader.TMDBGraph;

public class DijkstraTMDB {
  /**
   * 
   * @param g
   * @param source
   * @param target
   * @return the weight of the shortest path from the source to the target in g, or otherwise null
   */
  public static Double dijkstraShortestPath(TMDBGraph g, AgentNode source, AgentNode target) {
    // All node weights should be null at the start of this method.
    g.getNodes().stream() //
        .forEach(node -> node.setWeight(null));

    Set<AgentNode> fixedNodes = new HashSet<>();
    DijkstraHeap<AgentNode> minHeap = new DijkstraHeap<>();

    // initialize the heap
    source.setWeight(0D);
    minHeap.add(source);

    while (!minHeap.isEmpty()) {
      AgentNode fixedNode = minHeap.poll();
      fixedNodes.add(fixedNode);
      if (fixedNodes.contains(target)) {
        // We can stop once the target node is fixed
        break;
      }

      List<Connection> newEdges = fixedNode.getOutgoingEdges().stream() // Stream over outgoing
                                                                        // edges
          .filter(edge -> !fixedNodes.contains(edge.getTargetNode())) // Remove those already fixed
          .collect(Collectors.toList());

      for (Connection edge : newEdges) {
        AgentNode targetNode = edge.getTargetNode();

        if (targetNode.getWeight() != null
            && (targetNode.getWeight() <= fixedNode.getWeight() + edge.getWeight())) {
          // in this case, the target node edge is already in heap and cannot be relaxed
          // because there is already a shorter path to it
          continue;
        }

        targetNode.setWeight(fixedNode.getWeight() + edge.getWeight());
        targetNode.setPredecessor(fixedNode); // We'll keep track of the predecessor node so that we
                                              // can construct the shortest path

        minHeap.insertOrAdjust(targetNode);

      }
    }
    printPath(target);
    return target.getWeight();
  }

  public static void printPath(AgentNode node) {
    AgentNode pathNode = node;
    while (pathNode.getPredecessor() != null) {
      System.out.println(pathNode);
      pathNode = pathNode.getPredecessor();
    }
    System.out.println(pathNode);
  }
}
