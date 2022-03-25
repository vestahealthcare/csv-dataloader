package com.vestahealthcare.csvdataloader.readers;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Slf4j
public class CSVFileReader<S> {

  public List<?> readCSVFile(final String filePath, Class<?> dtoClass) {
    try {
      return new CsvToBeanBuilder(new FileReader(filePath)).withType(dtoClass).build().parse();
    } catch (FileNotFoundException ex) {
      log.info("File not found");
      return null;
    } catch (Exception e) {
      log.info("Couldnt read data from the file due to the exception " + e.getMessage());
      return null;
    }
  }
}
