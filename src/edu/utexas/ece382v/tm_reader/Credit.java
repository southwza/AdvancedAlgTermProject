package edu.utexas.ece382v.tm_reader;

public class Credit {
  private String name;
  private String castId;
  private String character;
  private String creditId;
  private String gender;
  private String id;
  private String order;

  public Credit() {}

  public Credit(String castId, String character, String creditId, String gender, String id,
      String name, String order) {
    this.name = name;
    this.castId = castId;
    this.character = character;
    this.creditId = creditId;
    this.gender = gender;
    this.id = id;
    this.order = order;
  }

  @Override
  public String toString() {
    return this.name + " " + this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getCastId() {
    return this.castId;
  }

  public String getCharacter() {
    return this.character;
  }

  public String getCreditId() {
    return this.creditId;
  }

  public String getGender() {
    return this.gender;
  }

  public String getId() {
    return this.id;
  }

  public String getOrder() {
    return this.order;
  }
}
