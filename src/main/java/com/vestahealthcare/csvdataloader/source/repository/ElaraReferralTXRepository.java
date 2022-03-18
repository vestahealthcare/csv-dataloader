package com.vestahealthcare.csvdataloader.source.repository;

import com.vestahealthcare.csvdataloader.readers.CSVFileReader;
import com.vestahealthcare.csvdataloader.source.model.ElaraReferralTX;

import java.util.List;

public class ElaraReferralTXRepository implements FileRepository<ElaraReferralTX> {

  public List<ElaraReferralTX> findAll() {
    CSVFileReader<ElaraReferralTX> csvFileReader = new CSVFileReader<>();
    return (List<ElaraReferralTX>) csvFileReader.readCSVFile(System.getenv("filePath"),ElaraReferralTX.class);
  }
}
