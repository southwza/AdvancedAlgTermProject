package edu.utexas.ece382v.tm_reader;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TMDBRecord {
  private List<Crew> crewList;
  private List<Credit> castList;
  // private Crew[] crew;
  // private Credit[] cast;
  private String cast;
  private String crew;
  private String title;
  private Integer movieId;

  public TMDBRecord() {}

  public Integer getMovieId() {
    return this.movieId;
  }

  public String getTitle() {
    return this.title;
  }

  public String getCast() {
    return this.cast;
  }

  public String getCrew() {
    return this.crew;
  }

  public List<Credit> getCastList() {
    return this.castList;
  }

  public List<Crew> getCrewList() {
    return this.crewList;
  }

  public void setCrewAndCastList() {
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      this.castList = objectMapper.readValue(this.cast, new TypeReference<List<Credit>>() {});

    } catch (JsonParseException e1) {
      // TODO Auto-generated catch block
      System.out.println(this.movieId);
      e1.printStackTrace();
    } catch (JsonMappingException e1) {
      // TODO Auto-generated catch block
      System.out.println(this.movieId);
      e1.printStackTrace();
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      System.out.println(this.movieId);
      e1.printStackTrace();
    }

    try {
      this.crewList = objectMapper.readValue(this.crew, new TypeReference<List<Crew>>() {});
    } catch (JsonParseException e) {
      // TODO Auto-generated catch block
      System.out.println(this.movieId);
      e.printStackTrace();
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      System.out.println(this.movieId);
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println(this.movieId);
      e.printStackTrace();
    }
  }
}
