package edu.utexas.ece382v.tm_reader;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TMDBRecord {
  // private ArrayList<Crew> crew;
  // private ArrayList<Credit> cast;
  // private Crew[] crew;
  // private Credit[] cast;
  private String cast;
  private String crew;
  private String title;
  private String movieId;

  public TMDBRecord() {}

  public TMDBRecord(String movieId, String title, String cast, String crew) {
    ObjectMapper objectMapper = new ObjectMapper();

    this.movieId = movieId;
    this.title = title;
    this.cast = cast;
    this.crew = crew;

    // try {
    // this.cast = objectMapper.readValue(cast, new TypeReference<ArrayList<Credit>>() {});
    // } catch (JsonParseException e1) {
    // // TODO Auto-generated catch block
    // e1.printStackTrace();
    // } catch (JsonMappingException e1) {
    // // TODO Auto-generated catch block
    // e1.printStackTrace();
    // } catch (IOException e1) {
    // // TODO Auto-generated catch block
    // e1.printStackTrace();
    // }
    //
    // try {
    // this.crew = objectMapper.readValue(crew, new TypeReference<ArrayList<Crew>>() {});
    // } catch (JsonParseException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (JsonMappingException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
  }

  public String getMovieId() {
    return this.movieId;
  }

  public String getTitle() {
    return this.title;
  }

  // public ArrayList<Credit> getCast() {
  // return this.cast;
  // }
  //
  // public ArrayList<Crew> getCrew() {
  // return this.crew;
  // }

  public String getCast() {
    return this.cast;
  }

  public String getCrew() {
    return this.crew;
  }

  // public Credit[] getCast() {
  // return this.cast;
  // }
  //
  // public Crew[] getCrew() {
  // return this.crew;
  // }

  @Override
  public String toString() {
    return this.movieId + " " + this.title;
  }


}
