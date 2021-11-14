package edu.utexas.ece382v.tm_reader;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@Setter
@AllArgsConstructor
public class Credit {
  private String name;
  private Integer castId;
  private String character;
  private String creditId;
  private Integer gender;
  private Integer id;
  private Integer order;

  // public Credit() {}

  // public Credit(String cast_id, String character, String credit_id, String gender, String id,
  // String name, String order) {
  // this.name = name;
  // this.cast_id = cast_id;
  // this.character = character;
  // this.credit_id = credit_id;
  // this.gender = gender;
  // this.id = id;
  // this.order = order;
  // }
}
