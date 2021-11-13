package edu.utexas.ece382v.tm_reader;

public class TMDBRecord {
  private String movieId;
  private String title;
  private String cast;
  private String crew;

  public TMDBRecord() {}

  public TMDBRecord(String movieId, String title, String cast, String crew) {
    this.movieId = movieId;
    this.title = title;
    this.cast = cast;
    this.crew = crew;
  }

  @Override
  public String toString() {
    return this.movieId + " " + this.title;
  }

  public String getMovieId() {
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
}
