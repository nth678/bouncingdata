package com.bouncingdata.plfdemo.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.JdbcBcDatastore;
import com.bouncingdata.plfdemo.util.dataparsing.DatasetColumn;

@Transactional
public class BcDatastoreService {

  private Logger logger = LoggerFactory.getLogger(BcDatastoreService.class);
  
  @Autowired
  private DataStorage dataStorage;
  
  @Autowired
  private JdbcBcDatastore jdbcBcDatastore;
        
  public List<Map> getDatasetToList(String dataset) throws Exception {
    return jdbcBcDatastore.getDatasetToList(dataset);
  }
  
  public List<Object[]> getDatasetToListOfArray(String dataset) throws Exception {
    return jdbcBcDatastore.getDatasetToListOfArray(dataset);
  }
  
  public List<Map> getDatasetToList(String dataset, int begin, int maxNumber) throws Exception {
    return jdbcBcDatastore.getDatasetToList(dataset, begin, maxNumber);
  }
  
  public String getDatasetToString(String dataset) throws Exception {
    return jdbcBcDatastore.getDataset(dataset);
  }
  
  public List<Object[]> getDatasetToListOfArray(String dataset, int begin, int maxNumber) throws Exception {
    return jdbcBcDatastore.getDatasetToListOfArray(dataset, begin, maxNumber);
  }
  
  //--- vinhpq : custom for search query
  public List<Object[]> getDatasetSearchQuery(String query) throws Exception {
    return jdbcBcDatastore.getDatasetSearchQuery(query);
  }
  
  public boolean dropDataset(String dsFullname) throws Exception {
    return jdbcBcDatastore.dropDataset(dsFullname);
  }
  //--- end of search function
  
  public String getDatasetToString(String dataset, int begin, int maxNumber) throws Exception {
    return jdbcBcDatastore.getDataset(dataset, begin, maxNumber);
  }
  
  public int getDatasetSize(String dataset) throws Exception {
    return jdbcBcDatastore.getDatasetSize(dataset);
  }
  
  public List<Map> query(String query) throws Exception {
    return jdbcBcDatastore.query(query);
  }
  
  public void storeData(String dsFullName, String[] headers, List<Object[]> data) throws Exception {
    for (int i = 0; i < headers.length; i++) {
      headers[i] = headers[i].trim();
    }
    jdbcBcDatastore.persistDataset(dsFullName , headers, data);
  }
  
  public void storeData(String dsFullName, DatasetColumn[] columns, List<String[]> data) throws Exception {
    jdbcBcDatastore.persistDataset(dsFullName , columns, data);
  }
  
  public String[] getColumnNames(String dsFullname) throws Exception {
    return jdbcBcDatastore.getColumnNames(dsFullname);
  }
  
  public String[] getColumnNamesByQuery(String query) throws Exception {
    return jdbcBcDatastore.getColumnNamesByQuery(query);
  }
  
  public void getCsvStream(String dsFullname, OutputStream os) throws Exception {
    jdbcBcDatastore.getCsvStream(dsFullname, os);
  }
  
  public String buildSchema(String tableName, DatasetColumn[] columns) {
    return jdbcBcDatastore.buildSchema(tableName, columns);
  }
  
  public boolean renameDataset(String oldName, String newName) {
	    return jdbcBcDatastore.renameDataset(oldName, newName);
  }
      
}
