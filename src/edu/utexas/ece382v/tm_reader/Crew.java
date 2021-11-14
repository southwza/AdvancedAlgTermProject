package edu.utexas.ece382v.tm_reader;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Crew {
  @JsonProperty("credit_id")
  private String creditId;
  @JsonProperty("department")
  private String department;
  @JsonProperty("gender")
  private Integer gender;
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("job")
  private String job;
  @JsonProperty("name")
  private String name;

  public Crew() {}

  public Crew(@JsonProperty("credit_id") String creditId,
      @JsonProperty("department") String department, @JsonProperty("gender") Integer gender,
      @JsonProperty("id") Integer id, @JsonProperty("job") String job,
      @JsonProperty("name") String name) {
    this.name = name;
    this.creditId = creditId;
    this.gender = gender;
    this.id = id;
    this.department = department;
    this.job = job;
  }

  @Override
  public String toString() {
    return this.name + " " + this.id;
  }

  public String getName() {
    return this.name;
  }
}
