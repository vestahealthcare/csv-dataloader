package com.vestahealthcare.csvdataloader.source.repository;

import java.util.List;

public interface FileRepository<S> {
  List<S> findAll();
}
