package edu.utexas.ece382v.shortest_path.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.utexas.ece382v.shortest_path.entities.Graph;

class TestGraphGenerator {

    @Test
    void testGenerateGraph() throws Exception {
        GraphGenerator graphGenerator = new GraphGenerator();
        Graph g = graphGenerator.generateGraph(2500L, 4, .9F, 1.0F);
//        System.out.println(g.toString());
        //Make sure we have 2500 nodes:
        assertEquals(2500, g.getNodes().size(), "Should have 2500 nodes");

    }

}
