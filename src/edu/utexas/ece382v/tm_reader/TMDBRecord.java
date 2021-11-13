package edu.utexas.ece382v.tm_reader;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class TMDBRecord {
  private List<Crew> crew;
  private List<Credit> cast;
  private String title;
  private String movieId;

  public TMDBRecord() {}

  @JsonPropertyOrder({"movieId", "title", "cast", "crew"})
  public TMDBRecord(String movieId, String title, List<Credit> cast, List<Crew> crew) {
    this.movieId = movieId;
    this.title = title;
    this.cast = cast;
    this.crew = crew;
  }

  public String getMovieId() {
    return this.movieId;
  }

  public String getTitle() {
    return this.title;
  }

  public List<Credit> getCast() {
    return this.cast;
  }

  public List<Crew> getCrew() {
    return this.crew;
  }

  @Override
  public String toString() {
    return this.movieId + " " + this.title;
  }


}
