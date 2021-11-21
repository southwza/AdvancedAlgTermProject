package edu.utexas.ece382v.tm_reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TestTMDBReader {
  @Test
  void testGenerateGraph() throws Exception {
    TMDBReader tmdbReader = new TMDBReader();
    TMDBGraph g = tmdbReader.generateGraph();
    // System.out.println(g.findNode("Aubrey Plaza").getIncomingEdges());
    for (Connection edge : g.findNode("Aubrey Plaza").getOutgoingEdges()) {
      System.out.println(edge);
    }
    assertEquals(104842, g.getNodes().size(), "Should have 104842 nodes");
    assertEquals(20079886, g.getEdges().size(), "Should have 20079886 edges");

  }
}
