package com.bouncingdata.plfdemo.util.dataparsing;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bouncingdata.plfdemo.util.Utils;
import com.bouncingdata.plfdemo.util.dataparsing.DatasetColumn.ColumnType;

class ExcelParser implements DataParser {
  
  private boolean firstRowAsHeader;
  
  private Logger logger = LoggerFactory.getLogger(ExcelParser.class);
  
  protected ExcelParser() {}
  
  public void setFirstRowAsHeader(boolean header) {
    this.firstRowAsHeader = header;
  }

  @Override
  public List<Object[]> parse(InputStream is) throws Exception {
    // read excel file
    Workbook wb = WorkbookFactory.create(is);
    Sheet sheet = wb.getSheetAt(0);

    // from the first row, determine the schema
    int firstRowNum = sheet.getFirstRowNum();
    int lastRowNum = sheet.getLastRowNum();
    Row firstRow = sheet.getRow(firstRowNum);
    int firstCellNum = firstRow.getFirstCellNum();
    int lastCellNum = firstRow.getLastCellNum() - 1;

    int columnNum = lastCellNum - firstCellNum + 1;
    String[] headers = new String[columnNum];
    List<Object[]> result = null;
    for (int i = firstCellNum; i <= lastCellNum; i++) {
      Cell headerCell = firstRow.getCell(i);
      headers[i - firstCellNum] = getCellStringValue(headerCell);
    }

    result = new ArrayList<Object[]>();
    result.add(headers);

    // now the data range is from [firstRow+1, firstCell] -> [lastRow, lastCell]
    logger.debug("Data range is from [{}, {}] to [{}, {}]", new String[] { String.valueOf(firstRowNum + 1), String.valueOf(firstCellNum), String.valueOf(lastRowNum), String.valueOf(lastCellNum) });
    for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
      String[] rowValues = new String[columnNum];
      Row row = sheet.getRow(i);
      for (int j = firstCellNum; j <= lastCellNum; j++) {
        Cell cell = row.getCell(j);
        if (cell != null) {
          String value = getCellStringValue(cell);
          rowValues[j - firstCellNum] = value;
        } else
          rowValues[j - firstCellNum] = null;
      }
      result.add(rowValues);
    }
    return result;
  }

  /**
   * Get string value from excel cell
   * 
   * @param cell
   *          the <code>Cell</code> object to read
   * @return the <code>String</code> value of <i>cell</i>
   */
  public static String getCellStringValue(Cell cell) {
    try {
      if (cell == null)
        return null;
      return cell.getStringCellValue();
    } catch (IllegalStateException e) {
      int type = cell.getCellType();
      switch (type) {
      case Cell.CELL_TYPE_BLANK:
        return null;
      case Cell.CELL_TYPE_BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());
      case Cell.CELL_TYPE_ERROR:
        return null;
      case Cell.CELL_TYPE_FORMULA:
        return cell.getCellFormula();
      case Cell.CELL_TYPE_NUMERIC:
        return String.valueOf(cell.getNumericCellValue());
      default:
        return cell.toString();
      }
    }
  }

  @Override
  public List<DatasetColumn> parseSchema(InputStream is) throws Exception {
    Workbook wb = WorkbookFactory.create(is);
    Sheet sheet = wb.getSheetAt(0);

    // from the first row, determine the schema
    int firstRowNum = sheet.getFirstRowNum();
    int lastRowNum = sheet.getLastRowNum();
    Row firstRow = sheet.getRow(firstRowNum);
    int firstCellNum = firstRow.getFirstCellNum();
    int lastCellNum = firstRow.getLastCellNum() - 1;

    List<Row> rows = new ArrayList<Row>();
    int patternSize = (int) Math.min(100, lastRowNum - firstRowNum);
    
    for (int i = 1 ; i <= patternSize; i++) {
      rows.add(sheet.getRow(firstRowNum + i));
    }
    
    List<DatasetColumn> columns = new ArrayList<DatasetColumn>();
    for (int i = firstCellNum; i <= lastCellNum; i++) {
      Cell headerCell = firstRow.getCell(i);
      String header = getCellStringValue(headerCell).trim();
      DatasetColumn column = new DatasetColumn(header);
      
      boolean isBoolean = true;
      boolean isInt = true;
      boolean isLong = true;
      boolean isDouble = true;
      
      /*// read first 100 value of this column to guess the datatype
      for (int j = 0; j < rows.size(); j++) {
        Cell cell = rows.get(j).getCell(i);
        if (cell != null) {
          int type = cell.getCellType();
          switch(type) {
          case Cell.CELL_TYPE_BOOLEAN:
            isInt = false;
            isLong = false;
            isDouble = false;
            break;
          case Cell.CELL_TYPE_NUMERIC:
            isBoolean = false;
            // set as double
            isInt = false;
            isLong = false;
            break;
          case Cell.CELL_TYPE_STRING:
            isBoolean = false;
            isInt = false;
            isLong = false;
            isDouble = false;
            break;
          default:
            isBoolean = isInt = isLong = isDouble = false;
          }
        }
      }
      
      if (isBoolean) {
        column.setTypeName("Boolean");
        column.setType(ColumnType.BOOLEAN);
        columns.add(column);
        continue;
      }
      
      if (isInt) {
        column.setTypeName("Integer");
        column.setType(ColumnType.INTEGER);
        columns.add(column);
        continue;
      }
      
      if (isLong) {
        column.setTypeName("Long");
        column.setType(ColumnType.LONG);
        columns.add(column);
        continue;
      }
      
      if (isDouble) {
        column.setTypeName("Double");
        column.setType(ColumnType.DOUBLE);
        columns.add(column);
        continue;
      }
      
      column.setTypeName("String");
      column.setType(ColumnType.STRING);
      columns.add(column);
      continue;*/
      
      for (int j = 0; j < rows.size(); j++) {
        String value = getCellStringValue(rows.get(j).getCell(i));
        if (isBoolean && !Utils.isBoolean(value)) isBoolean = false;
        if (isInt && !Utils.isInt(value)) isInt = false;
        if (isLong && !Utils.isLong(value)) isLong = false;
        if (isDouble && !Utils.isDouble(value)) isDouble = false;
      }
      
      if (isBoolean) {
        column.setTypeName("Boolean");
        column.setType(ColumnType.BOOLEAN);
        columns.add(column);
        continue;
      }
      
      if (isInt) {
        column.setTypeName("Integer");
        column.setType(ColumnType.INTEGER);
        columns.add(column);
        continue;
      }
      
      if (isLong) {
        column.setTypeName("Long");
        column.setType(ColumnType.LONG);
        columns.add(column);
        continue;
      }
      
      if (isDouble) {
        column.setTypeName("Double");
        column.setType(ColumnType.DOUBLE);
        columns.add(column);
        continue;
      }
      
      // default
      column.setTypeName("String");
      column.setType(ColumnType.STRING);
      columns.add(column);
      continue;
      
    }
    
    return columns;
    
  }
}