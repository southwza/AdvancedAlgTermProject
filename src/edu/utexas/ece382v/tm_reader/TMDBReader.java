package edu.utexas.ece382v.tm_reader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class TMDBReader {

  public static void main(String[] args) throws IOException {

    File csvFile = new File("./data/tmdb_5000_credits.csv");

    CsvMapper csvMapper = new CsvMapper();

    CsvSchema csvSchema = CsvSchema.builder().addColumn("movieId").addColumn("title")
        .addColumn("cast").addColumn("crew").build().withHeader();
    System.out.println("importing records...");
    MappingIterator<TMDBRecord> recordIter =
        csvMapper.readerWithTypedSchemaFor(TMDBRecord.class).with(csvSchema).readValues(csvFile);
    List<TMDBRecord> records = recordIter.readAll();
    System.out.println("hydrating cast and crew...");
    for (TMDBRecord rec : records) {
      rec.setCrewAndCastList();
    }
    System.out.println(records.get(0).getCastList().get(0).getName());
    System.out.println(records.get(0).getCastList().get(0).getId());
    System.out.println(records.get(0).getCrewList().get(0).getName());
    System.out.println(records.get(0).getCrewList().get(0).getId());
  }
}
