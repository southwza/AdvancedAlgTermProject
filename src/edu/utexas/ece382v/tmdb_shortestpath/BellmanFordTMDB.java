package edu.utexas.ece382v.tmdb_shortestpath;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import edu.utexas.ece382v.tm_reader.AgentNode;
import edu.utexas.ece382v.tm_reader.Connection;
import edu.utexas.ece382v.tm_reader.TMDBGraph;

public class BellmanFordTMDB {
  private Set<AgentNode> nodesToRelax;

  private long start = 0;
  private long end = 0;

  private long sequentialTime = 0;
  private long parallelTime = 0;
  private int threads = 0;

  private ExecutorService executorService;
  private ForkJoinPool forkJoinPool;

  public Double calculateShortestPath(TMDBGraph g, AgentNode source, AgentNode target) {
    return calculateShortestPath(g, source, target, false,
        Runtime.getRuntime().availableProcessors());
  }

  /**
   * 
   * @param g The graph containing source and target nodes
   * @param source
   * @param target
   * @param printStats Print execution information about parallelism
   * @param singleThread Perform the delta stepping calculation sequentially
   * @return
   */
  public Double calculateShortestPath(TMDBGraph g, AgentNode source, AgentNode target,
      boolean printStats, int threads) {
    this.threads = threads;
    executorService = Executors.newFixedThreadPool(threads);
    forkJoinPool = new ForkJoinPool(threads);

    sequentialTime = 0;
    parallelTime = 0;

    // Collect data on execution time
    start = System.nanoTime();

    // Initialize the weight of each node to a maximal value
    g.getNodes().stream().forEach(node -> node.setWeight(Double.MAX_VALUE));

    Map<AgentNode, Connection> req; // Nodes to be relaxed to the given value

    nodesToRelax = new ConcurrentSkipListSet<>();

    // Initialize our source node and first bucket
    relax(source, 0D, null);

    // We'll just iteratively relax all nodes until we are done
    while (nodesToRelax.size() > 0) {

      // We'll relax all the edges in our set. These edges can connect to new nodes with new
      // edges which will also be added to this bucket, so we have a while loop to continue
      // the process until all edges have been processed. After this while loop executes, we
      // can say that all nodes have been fixed.

      req = nodesToRelax.stream().flatMap(node -> node.getOutgoingEdges().stream())
          .collect(Collectors.toMap(e -> e.getTargetNode(), e -> e, (edge1, edge2) -> {
            double edge1Relaxation = calculateRelaxation(edge1);
            double edge2Relaxation = calculateRelaxation(edge2);
            return edge2Relaxation < edge1Relaxation ? edge2 : edge1;
          }));
      // Just a quick note about the stream expression above: we have the possibility
      // that our set contains multiple relaxations to the same target node. This is
      // handled by the mergeFunction in the collector which takes the tightest
      // relaxation for a given target node

      nodesToRelax.clear();
      // relax nodes in parallel
      relaxInParallel(req);

    }

    printStats();
    printPath(target);

    return target.getWeight().equals(Double.MAX_VALUE) ? null : target.getWeight();
  }


  private void relaxInParallel(Map<AgentNode, Connection> req) {
    end = System.nanoTime();
    sequentialTime += end - start;
    req.entrySet().stream().forEach(entry -> forkJoinPool.submit(() -> relax(entry.getKey(),
        calculateRelaxation(entry.getValue()), entry.getValue().getSourceNode())));
    forkJoinPool.awaitQuiescence(1, TimeUnit.MINUTES);
    start = System.nanoTime();
    parallelTime += start - end;
  }

  private void printStats() {
    end = System.nanoTime();
    sequentialTime += end - start;

    System.out.println("Execution statistics");
    System.out.println("Number of processors available on this system: "
        + Runtime.getRuntime().availableProcessors());
    System.out.println("Number of threads used in this execution: " + threads);
    System.out.println("Sequential time: " + sequentialTime / 1000000);
    System.out.println("Parallel time: " + parallelTime / 1000000);
    System.out.println("p / p + s: " + ((parallelTime + 0.0) / (parallelTime + sequentialTime)));
  }

  private double calculateRelaxation(Connection edge) {
    return edge.getSourceNode().getWeight() + edge.getWeight();
  }

  private void relax(AgentNode node, double weight, AgentNode sourceNode) {
    if (weight < node.getWeight()) {
      node.setWeight(weight);
      node.setPredecessor(sourceNode);
      nodesToRelax.add(node);
    }
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
