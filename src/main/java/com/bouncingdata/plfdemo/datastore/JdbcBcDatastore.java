package com.bouncingdata.plfdemo.datastore;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.util.Utils;
import com.bouncingdata.plfdemo.util.dataparsing.DatasetColumn;
import com.bouncingdata.plfdemo.util.dataparsing.InvalidColumnTypeException;
import com.bouncingdata.plfdemo.util.dataparsing.DatasetColumn.ColumnType;

public class JdbcBcDatastore extends JdbcDaoSupport implements BcDatastore {
  
  private Logger logger = LoggerFactory.getLogger(JdbcBcDatastore.class);
  
  private final static Map<ColumnType, String> typeMap;
  
  /**
   * The current mapping follows MySQL datatype definition
   */
  static {
    typeMap = new HashMap<DatasetColumn.ColumnType, String>();
    typeMap.put(ColumnType.INTEGER, "INTEGER");
    typeMap.put(ColumnType.LONG, "LONG");
    typeMap.put(ColumnType.DOUBLE, "DOUBLE");
    typeMap.put(ColumnType.BOOLEAN, "BIT");
    typeMap.put(ColumnType.STRING, "TEXT");
  }

  @Override
  public String getDataset(String dataset) throws Exception {
    String sql = "SELECT * FROM `" + dataset + "`";
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToJson(rs);
      
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when retrieved dataset {}", dataset);
        logger.debug("Exception detail: ", e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }  
  }
  
  public String getDataset(String dataset, int begin, int maxNumber) throws Exception {
    String sql = "SELECT * FROM `" + dataset + "` LIMIT " + begin + "," + maxNumber;
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToJson(rs);
      
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when retrieved dataset {}", dataset);
        logger.debug("Exception detail: ", e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
  }
  
//--- vinhpq : custom for search query 
  public List<Object[]> getDatasetSearchQuery(String query) throws DataAccessException {
    String sql = query;
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToListOfArray(rs);
      
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when retrieved dataset");
        logger.debug("Exception detail: ", e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }  
  }
  
  //--- end of search function
  
//  RENAME TABLE old_table TO tmp_table,
  public boolean renameDataset(String oldName, String newName){
	  String sql = "RENAME TABLE  `" + oldName + "` TO `" + newName + "`";
	    Connection conn = null;
	    Statement st = null;
	    ResultSet rs = null;
	    try {
	      conn = getDataSource().getConnection();
	      st = conn.createStatement();
	      rs = st.executeQuery(sql);
	     
	      
	    } catch (SQLException e) {
	      if (logger.isDebugEnabled()) {
	        logger.debug("Error when rename dataset");
	        logger.debug("Exception detail: ", e);
	      }
	      return false;
	      
	    } finally {
	      if (rs != null) try { rs.close(); } catch (Exception e) {return false;}
	      if (st != null) try { st.close(); } catch (Exception e) {return false;}
	      if (conn != null) try { conn.close(); } catch (Exception e) {return false;}
	    } 
	    
	    return true;
  }
  
  public List<Map> getDatasetToList(String dataset, int begin, int maxNumber) throws DataAccessException {
    String sql = "SELECT * FROM `" + dataset + "` LIMIT " + begin + "," + maxNumber;
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToList(rs);
      
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when retrieved dataset {}", dataset);
        logger.debug("Exception detail: ", e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }  
  }
  
  public List<Object[]> getDatasetToListOfArray(String dataset) throws DataAccessException {
    String sql = "SELECT * FROM `" + dataset + "`";
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToListOfArray(rs);
      
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when retrieved dataset {}", dataset);
        logger.debug("Exception detail: ", e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }  
  }
  
  public List<Object[]> getDatasetToListOfArray(String dataset, int begin, int maxNumber) throws DataAccessException {
    String sql = "SELECT * FROM `" + dataset + "` LIMIT " + begin + "," + maxNumber;
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToListOfArray(rs);
      
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when retrieved dataset {}", dataset);
        logger.debug("Exception detail: ", e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }  
  }
  
  @Override
  public List<Map> getDatasetToList(String dataset) throws DataAccessException {
    String sql = "SELECT * FROM `" + dataset + "`";
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToList(rs);
      
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when retrieved dataset {}", dataset);
        logger.debug("Exception detail: ", e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }  
  }
  
  @Override
  public List<Map> query(String sql) throws DataAccessException {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToList(rs);
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when execute query: " + sql, e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }  
  }
  
  /**
   * 
   */
  @Override
  public void persistDataset(String tableName, String[] headers, List<Object[]> data) {
    Connection conn = null;
    Statement st = null;
    PreparedStatement pst = null;
    int columnNum = headers.length;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      String sql = "DROP TABLE IF EXISTS `" + tableName + "`";
      st.executeUpdate(sql);
      
      StringBuilder createSql = new StringBuilder("CREATE TABLE `" + tableName + "` (");
      for (String header : headers) {
        createSql.append("`" + header + "`");
        createSql.append(" TEXT,");
      }
      
      sql = createSql.substring(0, createSql.length() -1) + ")";
      logger.debug("Creating table: {}...", sql);
      st.execute(sql);
      
      StringBuilder insertSql = new StringBuilder("INSERT INTO `" + tableName + "`(");
      for (int i = 0; i < columnNum; i++) {
        insertSql.append("`" + headers[i] + "`,");
      }
      insertSql = new StringBuilder(insertSql.substring(0, insertSql.length() - 1));
      insertSql.append(") VALUES ");
      
      for (int i = 0; i < data.size(); i++) {      
        StringBuilder row = new StringBuilder("(");
        for (int j = 0; j < columnNum; j++) {
          row.append("?,");
        }
        String rowStr = row.substring(0, row.length() - 1);
        rowStr += "),";
        insertSql.append(rowStr);
      }
      
      sql = insertSql.substring(0, insertSql.length() - 1);
      pst = conn.prepareStatement(sql);
      for (int i = 0; i < data.size(); i++) {
        String[] rowData = (String[]) data.get(i);
        for (int j = 0; j < columnNum; j++) {
          pst.setString(i*columnNum + j + 1, rowData[j]);
        }
      }
      //int result = st.executeUpdate(sql);
      
      boolean disabledKey = false;
      if (data.size() > 1000) {
        // disable keys
        sql = "ALTER TABLE `" + tableName + "` DISABLE KEYS";
        st.executeUpdate(sql);
        disabledKey = true;
      }
      
      logger.debug("Inserting data to table `{}`...", tableName);
      // execute insert
      int result = pst.executeUpdate();
      
      // enable keys
      if (disabledKey) {
        sql = "ALTER TABLE `" + tableName + "` ENABLE KEYS";
        st.executeUpdate(sql);
      }
      
      logger.debug("Insert completed: {} rows.", result);
      
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (pst != null) try { pst.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
  }
  
  @Override
  public void persistDataset(String tableName, DatasetColumn[] columns, List<String[]> data) {
    Connection conn = null;
    Statement st = null;
    PreparedStatement pst = null;
    int columnNum = columns.length;
    int currentColumn = -1;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      String sql = "DROP TABLE IF EXISTS `" + tableName + "`";
      st.executeUpdate(sql);
      
      sql = buildSchema(tableName, columns);
      logger.debug("Creating table: {}...", sql);
      st.execute(sql);
      
      StringBuilder insertSql = new StringBuilder("INSERT INTO `" + tableName + "`(");
      for (int i = 0; i < columnNum; i++) {
        insertSql.append("`" + columns[i].getName() + "`,");
      }
      insertSql = new StringBuilder(insertSql.substring(0, insertSql.length() - 1));
      insertSql.append(") VALUES ");
      
      for (int i = 0; i < data.size(); i++) {      
        StringBuilder row = new StringBuilder("(");
        for (int j = 0; j < columnNum; j++) {
          row.append("?,");
        }
        String rowStr = row.substring(0, row.length() - 1);
        rowStr += "),";
        insertSql.append(rowStr);
      }
      
      sql = insertSql.substring(0, insertSql.length() - 1);
      pst = conn.prepareStatement(sql);
      for (int i = 0; i < data.size(); i++) {
        String[] rowData = data.get(i);
        for (int j = 0; j < columnNum; j++) {
          currentColumn = j;
          switch (columns[j].getType()) {
            case INTEGER:
              int intVal = Integer.parseInt(rowData[j]);
              pst.setInt(i*columnNum + j + 1, intVal);
              break;
            case LONG:
              long longVal = Long.parseLong(rowData[j]);
              pst.setLong(i*columnNum + j + 1, longVal);
              break;
            case DOUBLE:
              double doubleVal = Double.parseDouble(rowData[j]); 
              pst.setDouble(i*columnNum + j + 1, doubleVal);
              break;
            case BOOLEAN:
              boolean boolVal = Boolean.parseBoolean(rowData[j]);
              pst.setBoolean(i*columnNum + j + 1, boolVal);
              break;
            case STRING:
              pst.setString(i*columnNum + j + 1, rowData[j]);
              break;
            default:
              pst.setString(i*columnNum + j + 1, rowData[j]);
          }
          
          pst.setString(i*columnNum + j + 1, rowData[j]);
        }
      }
      //int result = st.executeUpdate(sql);
      
      boolean disabledKey = false;
      if (data.size() > 1000) {
        // disable keys
        sql = "ALTER TABLE `" + tableName + "` DISABLE KEYS";
        st.executeUpdate(sql);
        disabledKey = true;
      }
      
      logger.debug("Inserting data to table `{}`...", tableName);
      // execute insert
      int result = pst.executeUpdate();
      
      // enable keys
      if (disabledKey) {
        sql = "ALTER TABLE `" + tableName + "` ENABLE KEYS";
        st.executeUpdate(sql);
      }
      
      logger.debug("Insert completed: {} rows.", result);
      
    } catch (NumberFormatException e) {
      if (currentColumn >= 0) {
        throw new InvalidColumnTypeException(String.format("Column %d %s is not %s", currentColumn,
            columns[currentColumn].getName(), columns[currentColumn].getTypeName()));
      } else throw new RuntimeException(e);
      
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (pst != null) try { pst.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
  }

  @Override
  public boolean dropDataset(String dsFullname) {
    Connection conn = null;
    Statement st = null;
    try {
      conn = getConnection();
      st = conn.createStatement();
      String sql = "DROP TABLE `" + dsFullname + "`";
      st.executeUpdate(sql);
    } catch (SQLException e) {
//      throw new RuntimeException(e);
      return false;
    } finally {
      if (st != null) try { st.close(); } catch(Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
    return true;
  }
  
  public int getDatasetSize(String dsFullname) {
    String sql = "SELECT id FROM `" + dsFullname + "`";
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      
      int rowCount = 0;
      while (rs.next()) {
        rowCount++;
       }
      return rowCount;
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error occurs when retrieve dataset {}", dsFullname);
        logger.debug("Exception detail: ", e);
      }
      return -1;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
  }
  
  public String[] getColumnNames(String dsFullname) {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery("SELECT * FROM `" + dsFullname + "` LIMIT 1");
      ResultSetMetaData rsmd = rs.getMetaData();
      String[] columns = new String[rsmd.getColumnCount()];
      for (int i = 1; i <= rsmd.getColumnCount(); i++) {
        columns[i-1] = rsmd.getColumnName(i);
      }
      return columns;
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error occurs when read dataset columns, dataset: {}", dsFullname);
        logger.debug("Exception detail: ", e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
  }
  
  public String[] getColumnNamesByQuery(String query) {
	    Connection conn = null;
	    Statement st = null;
	    ResultSet rs = null;
	    try {
	      conn = getDataSource().getConnection();
	      st = conn.createStatement();
	      rs = st.executeQuery(query);
	      ResultSetMetaData rsmd = rs.getMetaData();
	      String[] columns = new String[rsmd.getColumnCount()];
	      for (int i = 1; i <= rsmd.getColumnCount(); i++) {
	        columns[i-1] = rsmd.getColumnName(i);
	      }
	      return columns;
	    } catch (SQLException e) {
	      if (logger.isDebugEnabled()) {
	        logger.debug("Error occurs when read dataset columns");
	        logger.debug("Exception detail: ", e);
	      }
	      return null;
	    } finally {
	      if (rs != null) try { rs.close(); } catch (Exception e) {}
	      if (st != null) try { st.close(); } catch (Exception e) {}
	      if (conn != null) try { conn.close(); } catch (Exception e) {}
	    }
	  }
  
  public void getCsvStream(String dsFullname, OutputStream os) throws Exception {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery("SELECT * FROM `" + dsFullname + "`");
      Utils.resultSetToCSV(rs, os);
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error occurs while reading dataset: {}", dsFullname);
        logger.debug("Exception detail: ", e);
      }
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
  }

  @Override
  public String buildSchema(String tableName, DatasetColumn[] columns) {    
    StringBuilder createSql = new StringBuilder("CREATE TABLE `" + tableName + "` (");
    for (DatasetColumn column : columns) {
      createSql.append("`" + column.getName() + "` ");
      createSql.append(typeMap.get(column.getType()));
      createSql.append(",");
    }
    
    return createSql.substring(0, createSql.length() - 1) + ")";
  }
  
}
