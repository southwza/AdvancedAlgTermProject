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
  private static HashSet<AgentNode> nodes = new HashSet<AgentNode>();
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
        nodes.add(agent);
        for (Credit credLink : rec.getCastList()) {
          if (cred.equals(credLink) == false) {
            Double weight = Math.abs((double) (cred.getOrder() - credLink.getOrder()));
            Connection con =
                new Connection(agent, new AgentNode(credLink.getId(), credLink.getName()), weight,
                    rec.getMovieId(), rec.getTitle());
            insertConnection(con);
          }
        }
        for (Crew crewLink : rec.getCrewList()) {
          if (cred.getId().equals(crewLink.getId()) == false) {
            Double crewweight = (double) (50);
            Connection crewcon =
                new Connection(agent, new AgentNode(crewLink.getId(), crewLink.getName()),
                    crewweight, rec.getMovieId(), rec.getTitle());
            insertConnection(crewcon);
          }
        }
      }
      for (Crew crew : rec.getCrewList()) {
        AgentNode agent = new AgentNode(crew.getId(), crew.getName());
        nodes.add(agent);
        for (Crew crewLink : rec.getCrewList()) {
          if (crew.equals(crewLink) == false) {
            Double crewweight = (double) (5);
            Connection crewcon =
                new Connection(agent, new AgentNode(crewLink.getId(), crewLink.getName()),
                    crewweight, rec.getMovieId(), rec.getTitle());
            insertConnection(crewcon);
          }
        }
        for (Credit credLink : rec.getCastList()) {
          if (crew.getId().equals(credLink.getId()) == false) {
            Double weight = (double) (50);
            Connection con =
                new Connection(agent, new AgentNode(credLink.getId(), credLink.getName()), weight,
                    rec.getMovieId(), rec.getTitle());
            insertConnection(con);
          }
        }
      }
    }
  }

  public static void applyEdges() {
    int i = 0;
    System.out.println("processing apply edges...");
    for (AgentNode node : nodes) {
      i = i + 1;
      if (i % 10000 == 0) {
        System.out.println("processing: " + i + "...");
      }
      node.setIncomingEdges(inconnections.get(node.getIdentifier()));
      node.setOutgoingEdges(outconnections.get(node.getIdentifier()));
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
        (ArrayList<AgentNode>) nodes.stream().collect(Collectors.toList());

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
