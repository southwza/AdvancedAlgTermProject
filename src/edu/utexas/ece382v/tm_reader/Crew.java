package edu.utexas.ece382v.tm_reader;

public class Crew {
  private String creditId;
  private String department;
  private String gender;
  private String id;
  private String job;
  private String name;

  public Crew() {}

  public Crew(String creditId, String department, String gender, String id, String job,
      String name) {
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

}
