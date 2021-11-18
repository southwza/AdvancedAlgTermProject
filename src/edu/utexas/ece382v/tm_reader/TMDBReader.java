package edu.utexas.ece382v.tm_reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class TMDBReader {
  private static HashSet<AgentNode> nodes = new HashSet<AgentNode>();
  private static ArrayList<Connection> connections = new ArrayList<Connection>();

  private static void buildGraph(List<TMDBRecord> records) {
    for (TMDBRecord rec : records) {
      for (Credit cred : rec.getCastList()) {
        AgentNode agent = new AgentNode(cred.getId(), cred.getName());
        nodes.add(agent);
        for (Credit credLink : rec.getCastList()) {
          if (cred.equals(credLink) == false) {
            Double weight = Math.abs((double) (cred.getCastId() - credLink.getCastId()));
            Connection con =
                new Connection(agent, new AgentNode(credLink.getId(), credLink.getName()), weight,
                    rec.getMovieId(), rec.getTitle());
            connections.add(con);
          }
        }
        for (Crew crewLink : rec.getCrewList()) {
          if (cred.getId().equals(crewLink.getId()) == false) {
            Double crewweight = (double) (50);
            Connection crewcon =
                new Connection(agent, new AgentNode(crewLink.getId(), crewLink.getName()),
                    crewweight, rec.getMovieId(), rec.getTitle());
            connections.add(crewcon);
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
            connections.add(crewcon);
          }
        }
        for (Credit credLink : rec.getCastList()) {
          if (crew.getId().equals(credLink.getId()) == false) {
            Double weight = (double) (50);
            Connection con =
                new Connection(agent, new AgentNode(credLink.getId(), credLink.getName()), weight,
                    rec.getMovieId(), rec.getTitle());
            connections.add(con);
          }
        }
      }
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

  public static void main(String[] args) throws IOException {

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
    computeGraphStats();
    computeStats("Rob Reiner");
    computeStats("Kevin Bacon");
    computeStats("Drew Barrymore");
    computeStats("Owen Wilson");
    computeStats("Aubrey Plaza");
    computeStats("Julie Andrews");
    System.out.println("---- DONE ----");
  }
}
