package edu.utexas.ece382v.tm_reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class TMDBReader {
  private static HashMap<Integer, AgentNode> nodes = new HashMap<Integer, AgentNode>();
  private static HashMap<Integer, ArrayList<Connection>> inconnections =
      new HashMap<Integer, ArrayList<Connection>>();
  private static HashMap<Integer, ArrayList<Connection>> outconnections =
      new HashMap<Integer, ArrayList<Connection>>();
  private static ArrayList<Connection> connections = new ArrayList<Connection>();

  private static void insertConnection(Connection edge) {
    Integer to = edge.getTargetNode().getIdentifier();
    Integer from = edge.getSourceNode().getIdentifier();

    if (outconnections.containsKey(from)) {
      outconnections.get(from).add(edge);
    } else {
      ArrayList<Connection> edges = new ArrayList<Connection>();
      edges.add(edge);
      outconnections.put(from, edges);
    }

    if (inconnections.containsKey(to)) {
      inconnections.get(to).add(edge);
    } else {
      ArrayList<Connection> edges = new ArrayList<Connection>();
      edges.add(edge);
      inconnections.put(to, edges);
    }

    connections.add(edge);
  }

  private static void buildGraph(List<TMDBRecord> records) {
    for (TMDBRecord rec : records) {
      for (Credit cred : rec.getCastList()) {
        AgentNode agent = new AgentNode(cred.getId(), cred.getName());
        nodes.putIfAbsent(agent.getIdentifier(), agent);
        agent = nodes.get(agent.getIdentifier());
        for (Credit credLink : rec.getCastList()) {
          if (cred.equals(credLink) == false) {
            Double weight = Math.abs((double) (cred.getOrder() - credLink.getOrder()));
            AgentNode credAgent = new AgentNode(credLink.getId(), credLink.getName());
            nodes.putIfAbsent(credAgent.getIdentifier(), credAgent);
            credAgent = nodes.get(credAgent.getIdentifier());
            Connection con =
                new Connection(agent, credAgent, weight, rec.getMovieId(), rec.getTitle());
            insertConnection(con);
          }
        }
        for (Crew crewLink : rec.getCrewList()) {
          if (cred.getId().equals(crewLink.getId()) == false) {
            Double crewweight = (double) (30);
            AgentNode crewAgent = new AgentNode(crewLink.getId(), crewLink.getName());
            nodes.putIfAbsent(crewAgent.getIdentifier(), crewAgent);
            crewAgent = nodes.get(crewAgent.getIdentifier());
            Connection crewcon =
                new Connection(agent, crewAgent, crewweight, rec.getMovieId(), rec.getTitle());
            insertConnection(crewcon);
          }
        }
      }
      for (Crew crew : rec.getCrewList()) {
        AgentNode agent = new AgentNode(crew.getId(), crew.getName());
        nodes.putIfAbsent(agent.getIdentifier(), agent);
        agent = nodes.get(agent.getIdentifier());
        for (Crew crewLink : rec.getCrewList()) {
          if (crew.equals(crewLink) == false) {
            Double crewweight = (double) (5);
            AgentNode crewAgent = new AgentNode(crewLink.getId(), crewLink.getName());
            nodes.putIfAbsent(crewAgent.getIdentifier(), crewAgent);
            crewAgent = nodes.get(crewAgent.getIdentifier());
            Connection crewcon =
                new Connection(agent, crewAgent, crewweight, rec.getMovieId(), rec.getTitle());
            insertConnection(crewcon);
          }
        }
        for (Credit credLink : rec.getCastList()) {
          if (crew.getId().equals(credLink.getId()) == false) {
            Double weight = (double) (30);
            AgentNode credAgent = new AgentNode(credLink.getId(), credLink.getName());
            nodes.putIfAbsent(credAgent.getIdentifier(), credAgent);
            credAgent = nodes.get(credAgent.getIdentifier());
            Connection con =
                new Connection(agent, credAgent, weight, rec.getMovieId(), rec.getTitle());
            insertConnection(con);
          }
        }
      }
    }
  }

  public static ArrayList<Connection> scrubEdges(ArrayList<Connection> edges, Boolean isIn) {
    HashMap<Integer, Connection> smallestEdges = new HashMap<Integer, Connection>();

    if (edges == null) {
      return new ArrayList<Connection>();
    }

    for (Connection edge : edges) {
      Integer matchKey =
          isIn ? edge.getSourceNode().getIdentifier() : edge.getTargetNode().getIdentifier();
      if (smallestEdges.containsKey(matchKey)) {
        if (smallestEdges.get(matchKey).getWeight() > edge.getWeight()) {
          smallestEdges.put(matchKey, edge);
        }
      } else {
        smallestEdges.put(matchKey, edge);
      }
    }

    return new ArrayList<Connection>(smallestEdges.values());
  }

  public static void applyEdges() {
    int i = 0;
    System.out.println("processing apply edges...");
    for (AgentNode node : nodes.values()) {
      i = i + 1;
      if (i % 10000 == 0) {
        System.out.println("processing: " + i + "...");
      }
      node.setIncomingEdges(scrubEdges(inconnections.get(node.getIdentifier()), true));
      node.setOutgoingEdges(scrubEdges(outconnections.get(node.getIdentifier()), false));
    }
  }

  public static void computeStats(String agent) {
    HashSet<Integer> films = new HashSet<Integer>();
    int i = 0;
    for (Connection connection : connections) {
      if (connection.getSourceNode().getName().equals(agent)) {
        i = i + 1;
        films.add(connection.getFilmId());
      }
    }
    System.out.println(agent + " has " + i + " connections from " + films.size() + " films");
  }

  public static void computeGraphStats() {
    System.out.println("Nodes: " + nodes.size());
    System.out.println("Edges: " + connections.size());
  }

  public static TMDBGraph generateGraph() throws IOException {
    File csvFile = new File("./data/tmdb_5000_credits.csv");

    CsvMapper csvMapper = new CsvMapper();

    CsvSchema csvSchema = CsvSchema.builder().addColumn("movieId").addColumn("title")
        .addColumn("cast").addColumn("crew").build().withHeader();
    System.out.println("importing records...");
    MappingIterator<TMDBRecord> recordIter =
        csvMapper.readerWithTypedSchemaFor(TMDBRecord.class).with(csvSchema).readValues(csvFile);
    List<TMDBRecord> records = recordIter.readAll();
    System.out.println("hydrating cast and crew...");
    for (TMDBRecord rec : records) {
      rec.setCrewAndCastList();
    }

    buildGraph(records);
    applyEdges();
    System.out.println("---- DONE ----");

    ArrayList<AgentNode> nodeslist =
        (ArrayList<AgentNode>) nodes.values().stream().collect(Collectors.toList());

    TMDBGraph g = new TMDBGraph(nodeslist, connections);
    return g;
  }

  public static void printStats() {
    computeGraphStats();
    computeStats("Rob Reiner");
    computeStats("Kevin Bacon");
    computeStats("Drew Barrymore");
    computeStats("Owen Wilson");
    computeStats("Aubrey Plaza");
    computeStats("Julie Andrews");
  }
}
