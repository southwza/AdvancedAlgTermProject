package edu.utexas.ece382v.tm_reader;

import java.io.File;
import java.io.FileReader;
import com.opencsv.CSVReader;

public class TMDBReader {
  public static void readDataLineByLine(String file) {

    try {

      // Create an object of filereader
      // class with CSV file as a parameter.
      FileReader filereader = new FileReader(file);

      // create csvReader object passing
      // file reader as a parameter
      CSVReader csvReader = new CSVReader(filereader);
      String[] nextRecord;

      // we are going to read data line by line
      while ((nextRecord = csvReader.readNext()) != null) {
        for (String cell : nextRecord) {
          System.out.print(cell + "\t");
        }
        System.out.println();
      }
      csvReader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    File directory = new File("./");
    System.out.println(directory.getAbsolutePath());
    readDataLineByLine("./data/tmdb_5000_credits.csv");
  }
}
