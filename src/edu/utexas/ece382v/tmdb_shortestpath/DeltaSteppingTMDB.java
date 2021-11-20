package edu.utexas.ece382v.tmdb_shortestpath;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import edu.utexas.ece382v.tm_reader.AgentNode;
import edu.utexas.ece382v.tm_reader.Connection;
import edu.utexas.ece382v.tm_reader.TMDBGraph;

public class DeltaSteppingTMDB {
  private Double delta;
  private ConcurrentSkipListMap<Integer, Set<AgentNode>> B;

  private long start = 0;
  private long end = 0;

  private long sequentialTime = 0;
  private long parallelTime = 0;
  private int threads = 0;

  private ExecutorService executorService;
  private ForkJoinPool forkJoinPool;

  public Double calculateShortestPath(TMDBGraph g, AgentNode source, AgentNode target,
      double delta) {
    return calculateShortestPath(g, source, target, delta, false,
        Runtime.getRuntime().availableProcessors());
  }

  /**
   * 
   * @param g The graph containing source and target nodes
   * @param source
   * @param target
   * @param delta
   * @param printStats Print execution information about parallelism
   * @param singleThread Perform the delta stepping calculation sequentially
   * @return
   */
  public Double calculateShortestPath(TMDBGraph g, AgentNode source, AgentNode target, Double delta,
      boolean printStats, int threads) {
    this.threads = threads;
    executorService = Executors.newFixedThreadPool(threads);
    forkJoinPool = new ForkJoinPool(threads);

    sequentialTime = 0;
    parallelTime = 0;

    // Collect data on execution time
    start = System.nanoTime();
    this.delta = delta;

    // Initialize the weight of each node to a maximal value
    g.getNodes().stream().forEach(node -> node.setWeight(Double.MAX_VALUE));

    Map<AgentNode, Connection> req; // Nodes to be relaxed to the given value
    Set<AgentNode> S; // The set of nodes whose light edges have been relaxed but not the heavy
                      // edges

    B = new ConcurrentSkipListMap<>(); // List of buckets (Well, really a map, but the key is an
                                       // integer representing an 'index')

    // Initialize our source node and first bucket
    relax(source, 0D, null);

    // In our outer while loop, we'll process each bucket in our list of buckets
    while (B.size() > 0) {
      S = new HashSet<AgentNode>();
      Set<AgentNode> bucket = B.firstEntry().getValue();

      // First, we'll relax all the 'light' edges in our bucket. These edges can connect
      // to nodes which will also be added to this bucket, so we have a while loop to
      // continue the process until all light edges have been processed. After this while
      // loop executes, we can say that all nodes in the bucket have been fixed.
      while (bucket.size() > 0) {
        // Let's gather all of the outgoing nodes along light edges.
        req = bucket.stream().flatMap(node -> node.getOutgoingEdges().stream())
            .filter(e -> e.getWeight() <= delta) // filter out heavy edges
            .collect(Collectors.toMap(e -> e.getTargetNode(), e -> e, (edge1, edge2) -> {
              double edge1Relaxation = calculateRelaxation(edge1);
              double edge2Relaxation = calculateRelaxation(edge2);
              return edge2Relaxation < edge1Relaxation ? edge2 : edge1;
            }));
        // Just a quick note about the stream expression above: we have the possibility
        // that our bucket contains multiple relaxations to the same target node. This is
        // handled by the mergeFunction in the collector which takes the tightest
        // relaxation for a given target node
        S.addAll(bucket);
        bucket.clear();

        // relax light nodes in parallel
        relaxInParallel(req);

      }

      // If S contains our target node, then we can short-circuit and return it now
      if (S.contains(target)) {
        if (printStats) {
          printStats();
        }

        return target.getWeight();
      }

      // Now, let's relax all the heavy nodes from our bucket
      req = S.stream().flatMap(node -> node.getOutgoingEdges().stream())
          .filter(e -> e.getWeight() > delta) // filter out light edges
          .collect(Collectors.toMap(e -> e.getTargetNode(), e -> e, (edge1, edge2) -> {
            double edge1Relaxation = calculateRelaxation(edge1);
            double edge2Relaxation = calculateRelaxation(edge2);
            return edge2Relaxation < edge1Relaxation ? edge2 : edge1;
          }));
      // relax heavy nodes in parallel
      relaxInParallel(req);

      // Let's remove this bucket.
      B.pollFirstEntry();
    }
    if (printStats) {
      printStats();
    }

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
      if (node.getWeight().equals(Double.MAX_VALUE)) {
        int prevBucketIndex = Double.valueOf(node.getWeight() / delta).intValue();
        if (B.containsKey(prevBucketIndex)) {
          B.get(prevBucketIndex).remove(node);
        }
      }
      node.setWeight(weight);
      node.setPredecessor(sourceNode);
      int bucketIndex = Double.valueOf(weight / delta).intValue();
      if (!B.containsKey(bucketIndex)) {
        B.putIfAbsent(bucketIndex, new ConcurrentSkipListSet<>());
      }
      B.get(bucketIndex).add(node);
    }
  }

}
