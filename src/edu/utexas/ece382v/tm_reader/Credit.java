package edu.utexas.ece382v.tm_reader;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Credit {
  @JsonProperty("name")
  private String name;
  @JsonProperty("cast_id")
  private Integer castId;
  @JsonProperty("character")
  private String character;
  @JsonProperty("credit_id")
  private String creditId;
  @JsonProperty("gender")
  private Integer gender;
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("order")
  private Integer order;

  public Credit(@JsonProperty("name") String name, @JsonProperty("cast_id") Integer castId,
      @JsonProperty("character") String character, @JsonProperty("credit_id") String creditId,
      @JsonProperty("gender") Integer gender, @JsonProperty("id") Integer id,
      @JsonProperty("order") Integer order) {
    this.name = name;
    this.castId = castId;
    this.character = character;
    this.creditId = creditId;
    this.gender = gender;
    this.id = id;
    this.order = order;
  }

  public Credit() {}

  public String getName() {
    return this.name;
  }

  public Integer getCastId() {
    return this.castId;
  }

  public String getCharacter() {
    return this.character;
  }

  public String getCreditId() {
    return this.creditId;
  }

  public Integer getGender() {
    return this.gender;
  }

  public Integer getId() {
    return this.id;
  }

  public Integer getOrder() {
    return this.order;
  }

  @Override
  public String toString() {
    return this.creditId;
  }
}
