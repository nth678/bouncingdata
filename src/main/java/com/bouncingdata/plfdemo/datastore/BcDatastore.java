package com.bouncingdata.plfdemo.datastore;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.bouncingdata.plfdemo.util.dataparsing.DatasetColumn;

public interface BcDatastore {

  /**
   * @param dataset
   * @return
   */
  String getDataset(String dataset) throws Exception;

  /**
   * @param tableName
   * @param headers
   * @param data
   */
  void persistDataset(String tableName, String[] headers, List<Object[]> data);

  /**
   * @param sql
   * @return
   */
  List<Map> query(String sql);

  /**
   * @param dataset
   * @return
   */
  List<Map> getDatasetToList(String dataset);
  
  /**
   * @param datasetName
   */
  boolean dropDataset(String datasetName);
  
  void getCsvStream(String dsFullname, OutputStream os) throws Exception;

  void persistDataset(String tableName, DatasetColumn[] columns, List<String[]> data);
  
  String buildSchema(String tableName, DatasetColumn[] columns);

  boolean renameDataset(String oldName, String newName);
}
