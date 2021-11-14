package edu.utexas.ece382v.tm_reader;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@Setter
@AllArgsConstructor
public class Crew {
  private String creditId;
  private String department;
  private String gender;
  private String id;
  private String job;
  private String name;

  // public Crew() {}

  // public Crew(String credit_id, String department, String gender, String id, String job,
  // String name) {
  // this.name = name;
  // this.credit_id = credit_id;
  // this.gender = gender;
  // this.id = id;
  // this.department = department;
  // this.job = job;
  // }

  @Override
  public String toString() {
    return this.name + " " + this.id;
  }

}
