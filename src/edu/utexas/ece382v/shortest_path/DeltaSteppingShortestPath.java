package edu.utexas.ece382v.shortest_path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import edu.utexas.ece382v.shortest_path.entities.Edge;
import edu.utexas.ece382v.shortest_path.entities.Graph;
import edu.utexas.ece382v.shortest_path.entities.Node;

public class DeltaSteppingShortestPath {

    private Double delta;
    private ConcurrentSkipListMap<Integer, Set<Node>> B;

    private long start = 0;
    private long end = 0;

    private long sequentialTime = 0;
    private long parallelTime = 0;

    private long fetime = 0;

    public Double DeltaSteppingShortestPath(Graph g, Node source, Node target, Double delta) {
        start = System.nanoTime();
        this.delta = delta;

        //Initialize the weight of each node to a maximal value
        g.getNodes().stream().forEach(node -> node.setWeight(Double.MAX_VALUE));

        Map<Node, Double> req; //Nodes to be relaxed to the given value
        Set<Node> S; //The set of nodes whose light edges have been relaxed but not the heavy edges

        B = new ConcurrentSkipListMap<>(); //List of buckets (Well, really a map, but the key is an integer representing an 'index')

        //Initialize our source node and first bucket
        relax(source, 0D);
        while (B.size() > 0) {
            S = new HashSet<Node>();
            Set<Node> bucket = B.firstEntry().getValue();
            while (bucket.size() > 0) {
                //Let's gather all of the outgoing nodes along light edges.
long fe = System.nanoTime();
                req = bucket.stream().flatMap(node -> g.getOutgoingEdges(node).stream())
                        .filter(e -> e.getWeight() <= delta) //filter out heavy edges
                        .sorted(Comparator.comparing(e -> e.getSourceNode().getWeight() + e.getWeight()))
                        .collect(Collectors.toMap(e -> e.getTargetNode(),
                                e -> e.getSourceNode().getWeight() + e.getWeight(),
                                (existing, replacement) -> existing));
fetime += System.nanoTime() - fe;
                // Just a quick note about the stream expression above: we have the possibility
                // that our bucket contains multiple relaxations to the same target node. This is
                // handled by 1) sorting values by the relaxation amount and 2)using a
                // 'mergeFunction' in the collector which just takes the first value, so if there
                // is a conflict, we get the tightest relaxation for a given target node
                S.addAll(bucket);
                bucket.clear();

                end = System.nanoTime();
                sequentialTime += end - start;
                //relax light nodes in parallel
                req.entrySet().parallelStream().forEach(entry -> relax(entry.getKey(), entry.getValue()));
//                req.entrySet().stream().forEach(entry -> relax(entry.getKey(), entry.getValue()));
                start = System.nanoTime();
                parallelTime += start - end;
            }
            //If S contains our target node, then we can short-circuit and return it now
            if (S.contains(target)) {
                end = System.nanoTime();
                sequentialTime += end - start;

                System.out.println("Sequential time: " + sequentialTime / 1000000);
                System.out.println("Parallel time: " + parallelTime / 1000000);
                System.out.println("p / p + s: " + ((parallelTime + 0.0) / (parallelTime + sequentialTime)));

                System.out.println("filter exp time: " + (fetime)/1000000);
                return target.getWeight();
            }
            //Now, let's relax all the heavy nodes from our bucket
            req = S.stream().flatMap(node -> g.getOutgoingEdges(node).stream())
                    .filter(e -> e.getWeight() > delta) //filter out heavy edges
                    .sorted(Comparator.comparing(e -> e.getSourceNode().getWeight() + e.getWeight()))
                    .collect(Collectors.toMap(e -> e.getTargetNode(),
                            e -> e.getSourceNode().getWeight() + e.getWeight(),
                            (existing, replacement) -> existing));

            end = System.nanoTime();
            sequentialTime += end - start;
            //relax heavy nodes in parallel
            req.entrySet().parallelStream().forEach(entry -> relax(entry.getKey(), entry.getValue()));
//            req.entrySet().stream().forEach(entry -> relax(entry.getKey(), entry.getValue()));

            start = System.nanoTime();
            parallelTime += start - end;

            //Let's remove this bucket.
            B.pollFirstEntry();
        }
        end = System.nanoTime();
        sequentialTime += end - start;

        System.out.println("Sequential time: " + sequentialTime);
        System.out.println("Parallel time: " + parallelTime);
        System.out.println("p / p + s: " + (parallelTime + 0.0 / (parallelTime + sequentialTime)));
        return target.getWeight().equals(Double.MAX_VALUE) ? null : target.getWeight();
    }

    private void relax(Node node, double weight) {
        if (weight < node.getWeight()) {
            if (node.getWeight().equals(Double.MAX_VALUE)) {
                int prevBucketIndex = Double.valueOf(node.getWeight() / delta).intValue();
                if (B.containsKey(prevBucketIndex)) {
                    B.get(prevBucketIndex).remove(node);
                }
            }
            node.setWeight(weight);
            int bucketIndex = Double.valueOf(weight / delta).intValue();
            if(!B.containsKey(bucketIndex)) {
                B.putIfAbsent(bucketIndex, new ConcurrentSkipListSet<>());
            }
            B.get(bucketIndex).add(node);
        }
    }
}
