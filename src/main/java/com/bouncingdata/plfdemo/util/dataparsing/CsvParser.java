package com.bouncingdata.plfdemo.util.dataparsing;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bouncingdata.plfdemo.util.Utils;
import com.bouncingdata.plfdemo.util.dataparsing.DatasetColumn.ColumnType;

public class CsvParser implements DataParser {
  
  private Logger logger = LoggerFactory.getLogger(CsvParser.class);
  
  /**
   * Custom options for CSV Parser
   */
  private boolean firstRowAsHeader;
  private String delimiter;
  
  public void setFirstRowAsHeader(boolean header) {
    this.firstRowAsHeader = header;
  }
  
  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }
  
  protected CsvParser() {}

  @Override
  public List<Object[]> parse(InputStream is) throws Exception {
    CSVParser parser = new CSVParser(new InputStreamReader(is), CSVFormat.DEFAULT);
    List<CSVRecord> records = parser.getRecords();
    long lineNumber = parser.getRecordNumber();
    logger.info("Reading CSV file: {} rows found.", lineNumber);
    List<Object[]> results = new ArrayList<Object[]>();
    boolean isFirst = true;
    int columnNum = 0;
    for (CSVRecord record : records) {
      if(isFirst) columnNum = record.size();
      
      String[] row = new String[columnNum];
      Iterator<String> iter = record.iterator();
      int i = 0;
      while (iter.hasNext()) {
        if (i < columnNum) {
          row[i++] = iter.next();
        } else break;
      }
         
      results.add(row);
      isFirst = false;
    }
    return results;
  }
  
  @Override
  public List<DatasetColumn> parseSchema(InputStream is) throws Exception {
    CSVParser parser = new CSVParser(new InputStreamReader(is), CSVFormat.DEFAULT);
    Iterator<CSVRecord> iter = parser.iterator();
    CSVRecord header = iter.next();
    int fieldNumber = (int) header.size();
    
    List<CSVRecord> recordList = new ArrayList<CSVRecord>();
    
    int patternSize = (int) Math.min(100, parser.getRecordNumber());
    // guess column type from the first 100 records
    for (int i = 1; i <= patternSize; i++) {
      if (iter.hasNext()) {
        recordList.add(iter.next());
      }
    }
    
    List<DatasetColumn> dsColumns = new ArrayList<DatasetColumn>();
    
    int untitledCount = 1;
    for (int i = 0; i < fieldNumber; i++) {
      String column = header.get(i).trim();
      if (StringUtils.isEmpty(column)) {
        column = "Untitled column " + untitledCount++;
      }
      DatasetColumn dsCol = new DatasetColumn(column);
      boolean isBoolean = true;
      boolean isInt = true;
      boolean isLong = true;
      boolean isDouble = true;
      
      for (int j = 0; j < recordList.size(); j++) {
        String value = recordList.get(j).get(i);
        if (isBoolean && !Utils.isBoolean(value)) isBoolean = false;
        if (isInt && !Utils.isInt(value)) isInt = false;
        if (isLong && !Utils.isLong(value)) isLong = false;
        if (isDouble && !Utils.isDouble(value)) isDouble = false;
      }
      
      if (isBoolean) {
        dsCol.setTypeName("Boolean");
        dsCol.setType(ColumnType.BOOLEAN);
        dsColumns.add(dsCol);
        continue;
      }
      
      if (isInt) {
        dsCol.setTypeName("Integer");
        dsCol.setType(ColumnType.INTEGER);
        dsColumns.add(dsCol);
        continue;
      }
      
      if (isLong) {
        dsCol.setTypeName("Long");
        dsCol.setType(ColumnType.LONG);
        dsColumns.add(dsCol);
        continue;
      }
      
      if (isDouble) {
        dsCol.setTypeName("Double");
        dsCol.setType(ColumnType.DOUBLE);
        dsColumns.add(dsCol);
        continue;
      }
      
      dsCol.setTypeName("String");
      dsCol.setType(ColumnType.STRING);
      dsColumns.add(dsCol);
      continue;
      
    }
    
    return dsColumns;
  }
}
