package edu.utexas.ece382v.tm_reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TestTMDBReader {
  @Test
  void testGenerateGraph() throws Exception {
    TMDBReader tmdbReader = new TMDBReader();
    TMDBGraph g = tmdbReader.generateGraph();
    assertEquals(104842, g.getNodes().size(), "Should have 104842 nodes");
    assertEquals(20079886, g.getEdges().size(), "Should have 20079886 edges");
  }
}
