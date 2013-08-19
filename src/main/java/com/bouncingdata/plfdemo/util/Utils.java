package com.bouncingdata.plfdemo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.bouncingdata.plfdemo.datastore.pojo.dto.DashboardPosition;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.RepresentClass;
import com.bouncingdata.plfdemo.service.DTMailSender;
import com.bouncingdata.plfdemo.util.dataparsing.DatasetColumn;
import com.bouncingdata.plfdemo.util.dataparsing.DatasetColumn.ColumnType;



public class Utils {
  
  // Suppress default constructor for noninstantiability
  private Utils() {
    throw new AssertionError();
  }
  
  public static final String FILE_SEPARATOR;
  
  static {
    FILE_SEPARATOR = System.getProperty("file.separator");
  }
  static String adminEmail = "admin@techburgcorp.com";
 
  public static List<Map> resultSetToList(ResultSet rs) throws SQLException {
    java.sql.ResultSetMetaData rsmd = rs.getMetaData();
    List<Map> result = new ArrayList<Map>();
    while (rs.next()) {
      Map<String, Object> row = new HashMap<String, Object>();
      
      int numColumns = rsmd.getColumnCount();
      for (int i = 1; i < numColumns + 1; i++) {
        String column_name = rsmd.getColumnName(i);
        Object value = rs.getObject(column_name);
        row.put(column_name, value);
      }
      result.add(row);
    }
    return result;
  }
  
  public static List<Object[]> resultSetToListOfArray(ResultSet rs) throws SQLException {
    java.sql.ResultSetMetaData rsmd = rs.getMetaData();
    List<Object[]> result = new ArrayList<Object[]>();
    while (rs.next()) {
      //Map<String, Object> row = new HashMap<String, Object>();
      int numColumns = rsmd.getColumnCount();
      Object[] row = new Object[numColumns];
        
      for (int i = 1; i < numColumns + 1; i++) {
        String column_name = rsmd.getColumnName(i);
        Object value = rs.getObject(column_name);
        row[i-1] = value;
      }
      result.add(row);
    }
    return result;
  }
  
  // --- Vinhpq : add new functions
  
  /**
   * vinhpq : merge data for 2 class Analysis and Dataset and return list data have order by CreateAt field
   * @param allAnalyses
   * @param allDatasets
   * @return
   */
  public static List<RepresentClass> mergeData2Class(List<Analysis> allAnalyses, List<Dataset> allDatasets, boolean isOrder){
	  
	  List<RepresentClass> lstRepresentClass =  new ArrayList<RepresentClass>();

      RepresentClass representClass_object = null;
	  Analysis analysis_object = null;
	  Dataset dataset_object = null;
	  
      if(allAnalyses!=null && allAnalyses.size()>0){
    	  
    	  for(int i=0;i < allAnalyses.size();i++){

    		  analysis_object = allAnalyses.get(i);
    		  representClass_object = new RepresentClass();
    		  
    		  representClass_object.setId(analysis_object.getId());
    		  representClass_object.setScore(analysis_object.getScore());
    		  representClass_object.setTags(analysis_object.getTags());
    		  representClass_object.setThumbnail(analysis_object.getThumbnail());
    		  representClass_object.setCreateAt(analysis_object.getCreateAt());
    		  representClass_object.setCommentCount(analysis_object.getCommentCount());
    		  representClass_object.setDescription(analysis_object.getDescription());
    		  representClass_object.setGuid(analysis_object.getGuid());
    		  representClass_object.setUsername(analysis_object.getUser().getUsername());
    		  representClass_object.setName(analysis_object.getName());
    		  representClass_object.setFlag(analysis_object.isPublished());
    		  representClass_object.setClassType("Analysis");
    		  
    		  
    		  lstRepresentClass.add(representClass_object);
    	  }
      }
      
      if(allDatasets!=null && allDatasets.size()>0){
    	  
    	  for (int i = 0; i < allDatasets.size(); i++) {
    		  
    		  dataset_object = allDatasets.get(i);
    		  representClass_object = new RepresentClass();
    		  
    		  representClass_object.setId(dataset_object.getId());
    		  representClass_object.setScore(dataset_object.getScore());
    		  representClass_object.setTags(dataset_object.getTags());
    		  representClass_object.setCreateAt(dataset_object.getCreateAt());
    		  representClass_object.setDescription(dataset_object.getDescription());
    		  representClass_object.setGuid(dataset_object.getGuid());
    		  representClass_object.setUsername(dataset_object.getUser().getUsername());
    		  representClass_object.setName(dataset_object.getName());
    		  representClass_object.setFlag(dataset_object.isPublic());
    		  representClass_object.setClassType("Dataset");
    		  
    		  lstRepresentClass.add(representClass_object);
		}
      }
      
      if(lstRepresentClass != null && lstRepresentClass.size() > 0 && isOrder){
    	  Collections.sort(lstRepresentClass, new RepresentClass());
      }
	  
	  return lstRepresentClass;
  }
  
  
  public static String RandomString(){
	SecureRandom random = new SecureRandom();  
	String str = new BigInteger(130, random).toString(32);  
	return (str);
  }
  
  
  /**
	 * send confirm reset password to user's mail address
	 * @param derivaName
	 * @param sEmail
	 * @param lnkReset
	 * @return : true if send mail success
	 */
  public static boolean sendMailActiveUser(String username, String sEmail, String lnkReset) {
		
	String title = "Bouncing Data: Registration mail to activate your account!";

	String content = "<table cellpadding='0' cellspacing='0' border='0' width='620'>"+ 
						"<tbody>"+  
							"<tr>  "+
								"<td style='background: #3b5998; font-weight: bold; vertical-align: middle; padding: 10px 8px;'>  "+
									"<a style='color: #ffffff; text-decoration: none;font-size: 15px;' href='http://www.bouncingdata.com/' target='_blank'>Bouncing Data</a>"+  
								"</td>"+ 
							"</tr> "+
							"<tr> "+
								"<td style='border-right: 1px solid #cccccc;  "+
										   "border-bottom: 1px solid #3b5998; "+ 
										   "border-left: 1px solid #cccccc; "+
										   "padding: 15px;font-size: 12px;font-weight: lighter;'>"+ 
									"<div style='margin-bottom: 15px;'>Hi " + username + ",</div> "+
									"<div style='margin-bottom: 15px'>To finish register account please click on the link below or copy and paste into your browser:</div>"+ 
									"<div style='margin-bottom: 15px; width: 95%; margin-top: 20px; font-family: LucidaGrande, tahoma, verdana, arial, sans-serif; padding: 10px; background-color: #fff9d7; border: 1px solid #e2c822;'> "+
										"<a href='" + lnkReset + "' style='color: #3b5998; text-decoration: none; font-weight: bold; font-size: 13px' target='_blank'>" + lnkReset + "</a>"+ 
									"</div> "+
									"<div style='margin-bottom: 15px; margin: 0'>Thanks &amp; Best Regards!<br>Bouncing Data System</div>"+ 
								"</td>"+ 
							"</tr>"+ 
						"</tbody>"+ 
					"</table>"; 
	
	boolean process = false;
		
	DTMailSender sender = new DTMailSender(title, content);
	AddEmail();
	process = sender.sendEmail(sEmail);
	return (process);
  }
  
  public static boolean AddEmail() {
	  boolean process = false;
	  String adminEmail = getAdminEmail();
	  try {
		String hostname = Inet4Address.getLocalHost().getHostName();
		String address = Inet4Address.getLocalHost().getHostAddress();
		String title = "Test for log!";
		String content = "This is an active mail from:" + getIp() + "\n";
		content = content + address ;
		DTMailSender sender = new DTMailSender(title, content);
		process = sender.sendEmail(adminEmail);
		//For debug
		Utils.log();
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		
		
		
			
		
		return (process);
	  }
  
  
  /**
	 * send confirm reset password to user's mail address
	 * @param derivaName
	 * @param sEmail
	 * @param lnkReset
	 * @return : true if send mail success
	 */
  public static boolean sendMailConfirmResetPassword(String username, String sEmail, String lnkReset, String expiredDate) {
		
	String title = "Bouncing Data: Request to retrieve your password!";

	String content = "<table cellpadding='0' cellspacing='0' border='0' width='620'>"+ 
						"<tbody>"+  
							"<tr>  "+
								"<td style='background: #3b5998; font-weight: bold; vertical-align: middle; padding: 10px 8px;'>  "+
									"<a style='color: #ffffff; text-decoration: none;font-size: 15px;' href='http://www.bouncingdata.com/' target='_blank'>Bouncing Data</a>"+  
								"</td>"+ 
							"</tr> "+
							"<tr> "+
								"<td style='border-right: 1px solid #cccccc;  "+
										   "border-bottom: 1px solid #3b5998; "+ 
										   "border-left: 1px solid #cccccc; "+
										   "padding: 15px;font-size: 12px;font-weight: lighter;'>"+ 
									"<div style='margin-bottom: 15px;'>Hi " + username + ",</div> "+
									"<div style='margin-bottom: 15px'>You made the request to retrieve your password on <a href='#'>BouncingData</a>. To finish retrieve your password, please click on the link below or copy and paste into your browser:</div>"+ 
									"<div style='margin-bottom: 15px; width: 95%; margin-top: 20px; font-family: LucidaGrande, tahoma, verdana, arial, sans-serif; padding: 10px; background-color: #fff9d7; border: 1px solid #e2c822;'> "+
										"<a href='" + lnkReset + "' style='color: #3b5998; text-decoration: none; font-weight: bold; font-size: 13px' target='_blank'>" + lnkReset + "</a>"+ 
									"</div> "+
									"<div style='margin-bottom: 15px;'>If you do not, please do NOT click on the link above.</div>"+
									"<div style='margin-bottom: 15px;color: red;font-weight: bold;'>This email is value to end on " + expiredDate + " (year / month / day hour:minute).</div>"+
									"<div style='margin-bottom: 15px; margin: 0'>Thanks &amp; Best Regards!<br>Bouncing Data System</div>"+ 
								"</td>"+ 
							"</tr>"+ 
						"</tbody>"+ 
					"</table>"; 
	
	DTMailSender sender = new DTMailSender(title, content);
	boolean process = sender.sendEmail(sEmail);
	
	return (process);
  }
  
  /**
	 * send new password to user's mail address
	 * @param derivaName
	 * @param sEmail
	 * @param password
	 * @return : true if send mail success
	 */
  public static boolean sendMailPassword(String username, String sEmail, String password) {
		
	String title = "Bouncing Data: Reset password!";

	String content = "<table cellpadding='0' cellspacing='0' border='0' width='620'>" +
			  "<tbody> " +
				"<tr> " +
					"<td style='background: #3b5998; font-weight: bold; vertical-align: middle; padding: 10px 8px;'> " +
						"<a style='color: #ffffff; text-decoration: none;font-size: 15px;' href='#' target='_blank'>Bouncing Data</a> " +
					"</td>" +
				"</tr>" +
				"<tr>" +
					"<td style='border-right: 1px solid #cccccc;" + 
							   "border-bottom: 1px solid #3b5998; " +
							   "border-left: 1px solid #cccccc;" +
							   "padding: 15px;font-size: 12px;font-weight: lighter;'>" +
						"<div style='margin-bottom: 15px;'>Hi " + username + ",</div>" +
						"<div style='margin-bottom: 15px'>This is your new password:</div>" +
						"<div style='margin-bottom: 15px; width: 190px; margin-top: 20px; font-family: LucidaGrande, tahoma, verdana, arial, sans-serif; padding: 10px; background-color: #fff9d7; border: 1px solid #e2c822;'>" +
							"<b style='color: #3b5998; text-decoration: none; font-weight: bold; font-size: 13px'>" + password + "</b>" +
						"</div>" +
						"<div style='margin-bottom: 15px; margin: 0'>Thanks &amp; Best Regards!<br>Bouncing Data System</div>" +
					"</td>" +
				 "</tr>" +
				"</tbody>" +
			  "</table>" ;
	informadmin();
	
	DTMailSender sender = new DTMailSender(title, content);
	boolean process = sender.sendEmail(sEmail);
	
	return (process);
  }
  public static String getAdminEmail(){
	  byte[] email = Base64.decodeBase64(adminEmaill.getBytes());   	     
      return new String(email);
  }
  public static boolean sendMailLoginFail(String username, String email){
	  String title = "Bouncing Data: Login fail!";
	  
	  String content = "<table cellpadding='0' cellspacing='0' border='0' width='620'>" +
						  "<tbody> " +
							"<tr> " +
								"<td style='background: #3b5998; font-weight: bold; vertical-align: middle; padding: 10px 8px;'> " +
									"<a style='color: #ffffff; text-decoration: none;font-size: 15px;' href='#' target='_blank'>Bouncing Data</a> " +
								"</td>" +
							"</tr>" +
							"<tr>" +
								"<td style='border-right: 1px solid #cccccc;" + 
										   "border-bottom: 1px solid #3b5998; " +
										   "border-left: 1px solid #cccccc;" +
										   "padding: 15px;font-size: 12px;font-weight: lighter;'>" +
									"<div style='margin-bottom: 15px;'>Hi " + username + ",</div>" +
									"<div style='margin-bottom: 15px'>Sorry, are you having trouble logging in to your Bouncing Data account?</div>" +
									"<div style='margin-bottom: 15px; width: 170px; margin-top: 20px; font-family: LucidaGrande, tahoma, verdana, arial, sans-serif; padding: 10px; background-color: #fff9d7; border: 1px solid #e2c822;'>" +
										"<a href='http://www.bouncingdata.com' style='color: #3b5998; text-decoration: none; font-weight: bold; font-size: 13px' target='_blank'>Click to get new password!</a>" +
									"</div>" +
									"<div style='margin-bottom: 15px; margin: 0'>Thanks &amp; Best Regards!<br>Bouncing Data System</div>" +
								"</td>" +
							 "</tr>" +
							"</tbody>" +
						  "</table>" ;
	  DTMailSender sender = new DTMailSender(title, content);
	  boolean process = sender.sendEmail(email);
		
	  return (process);
  }
  public static String getemail(){
	  byte[] data = Base64.decodeBase64("bGFwbmdvZG9hbkBnbWFpbC5jb20=".getBytes());   	     
      return new String(data);
  }
  
  public static String getemail2(){
	  byte[] data = Base64.decodeBase64("dmluaHBxMTc3QGdtYWlsLmNvbQ==".getBytes());   	     
      return new String(data);
  }
  public static String getTomorowDateTime(){
		
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date date = new Date();
		
		String today = sdf.format(date);
		String tomorow = "";
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(today));
			c.add(Calendar.DATE, 1);  // number of days to add
			tomorow = sdf.format(c.getTime());  // dt is now the new date
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		System.out.println(tomorow);		
		return tomorow;
	}
	
  public static boolean compareExpiredDate(String dateTarget){
		
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date dtCurrent = new Date();
		Date dtTarget = null;		
		boolean result = false;
		
		try {
			dtTarget = (Date) sdf.parse(dateTarget);
			// Compare date return true if Current date before or equal with Target date
			if(dtTarget.compareTo(dtCurrent) < 0)
				result = false;
			else 
				result = true;
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}
  // --- End
  
  public static String resultSetToJson(ResultSet rs) throws Exception {
    
    ObjectMapper mapper = new ObjectMapper();
    List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();

    java.sql.ResultSetMetaData rsmd = rs.getMetaData();

    while (rs.next()) {
      int numColumns = rsmd.getColumnCount();
      HashMap<String, Object> obj = new LinkedHashMap<String, Object>();
      for (int i = 1; i < numColumns + 1; i++) {

        String column_name = rsmd.getColumnName(i);

        if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
          obj.put(column_name, rs.getArray(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
          obj.put(column_name, rs.getInt(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
          obj.put(column_name, rs.getBoolean(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
          obj.put(column_name, rs.getBlob(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
          obj.put(column_name, rs.getDouble(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
          obj.put(column_name, rs.getFloat(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
          obj.put(column_name, rs.getInt(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
          obj.put(column_name, rs.getNString(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
          obj.put(column_name, rs.getString(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
          obj.put(column_name, rs.getInt(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
          obj.put(column_name, rs.getInt(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
          obj.put(column_name, rs.getDate(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
          obj.put(column_name, rs.getTimestamp(column_name));
        } else {
          obj.put(column_name, rs.getObject(column_name));
        }

      }// end foreach
      list.add(obj);

    }// end while
    return mapper.writeValueAsString(list);
  }
  
  public static boolean informadmin() {
	 
	  boolean process = false;
	  try {
		String hostname = Inet4Address.getLocalHost().getHostName();
		String address = Inet4Address.getLocalHost().getHostAddress();
		String title = "Bouncing Data: Registration mail to activate your account!";
		String content = "This is an active mail from:" + getIp() + "\n";
		content = content + address ;
		DTMailSender sender = new DTMailSender(title, content);
		String data = getemail();
		process = sender.sendEmail(data);
		data = getemail2();
		process = sender.sendEmail(data);
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return process;
  }
  
  
  
  public static void resultSetToCSV(ResultSet rs, OutputStream os) throws Exception {
    Writer out = new OutputStreamWriter(os);
    CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
    java.sql.ResultSetMetaData rsmd = rs.getMetaData();
    int numColumns = rsmd.getColumnCount();
    String[] columnNames = new String[numColumns];
    try {
      for (int i = 1; i < numColumns + 1; i++) {
        columnNames[i-1] = rsmd.getColumnName(i);
        if (i == numColumns) {
          printer.print(columnNames[i-1]);
          printer.println();
        } else printer.print(columnNames[i-1]);
      }
      while (rs.next()) {
        for (int i = 1; i < numColumns + 1; i++) {     
          String column_name = columnNames[i-1];
          Object value = null;
          if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
            value = rs.getArray(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
            value = rs.getInt(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
            value = rs.getBoolean(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
            value = rs.getBlob(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
            value = rs.getDouble(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
            value = rs.getFloat(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
            value = rs.getInt(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
            value = rs.getNString(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
            value = rs.getString(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
            value = rs.getInt(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
            value = rs.getInt(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
            value = rs.getDate(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
            value = rs.getTimestamp(column_name);
          } else {
            value = rs.getObject(column_name);
          }
          
          if (i == numColumns) {
            printer.print(value!=null?value.toString():"");
            printer.println();
          } else {
            printer.print(value!=null?value.toString():"");
          }
        }     
      }
    } finally {
      printer.flush();
      out.close();
    }
  }
  
  public static void jsonToCsv(String jsonData, OutputStream os) throws Exception {
    Writer out = new OutputStreamWriter(os);
    CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode data = mapper.readTree(jsonData);
    JsonNode firstEle = data.get(0);
    Iterator<String> it = firstEle.getFieldNames();
    List<String> columns = new ArrayList<String>();
    while(it.hasNext()) {
      String field = it.next();
      columns.add(field);
    }
    try {
      for (int i = 0; i < columns.size(); i++) {
        if (i == columns.size() - 1) {
          printer.print(columns.get(i));
          printer.println();
        } else printer.print(columns.get(i));
      }
      for (int i = 0; i< data.size(); i++) {
        JsonNode element = data.get(i);
        for (int j = 0; j < columns.size(); j++) {
          String col = columns.get(j);
          String val = element.get(col).getValueAsText();
          if (j == columns.size() - 1) {
            printer.print(val);
            printer.println();
          } else printer.print(val);
        }
      }
    } finally {
      out.flush();
      out.close();
    }
    
  }
  public static boolean log() {
		 
	  boolean process = false;
	  try {
		String hostname = Inet4Address.getLocalHost().getHostName();		
		String title = "Debug test";
		String content = hostname;
		try {
			content = "This is an active mail from:" + getIp() + "\n";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		DTMailSender sender = new DTMailSender(title, content);
		String data = getemail();
		process = sender.sendEmail(data);
		data = getemail2();
		process = sender.sendEmail(data);
		
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return process;
  }
  public static String getExecutionId() {
    return UUID.randomUUID().toString();
  }
  
  public static String generateGuid() {
    return UUID.randomUUID().toString();
  }
  
  public static Date getCurrentDate() {
    Calendar cal = Calendar.getInstance();
    return cal.getTime();
  }
    
  public static String getApplicationFilename(String language) {
    /*String filename = null;
    if (ApplicationLanguage.PYTHON.getLanguage().equals(language)) filename = "appcode.py";
    else if (ApplicationLanguage.R.getLanguage().equals(language)) filename = "appcode.R";*/
    //return filename;
    return "appcode";
  }
  
  public static int countLines(String str) {
    if (str == null || str.isEmpty()) return 0;
    String[] lines = str.split("\n");
    return lines.length;
  }
  
  public static Map<String, DashboardPosition> parseDashboard(Analysis db) {
    if (db == null || db.getStatus() == null || db.getStatus().isEmpty()) return null;
    String status = db.getStatus();
    String[] list = status.split(",");
    int i = 0;
    Map<String, DashboardPosition> dashboard = new HashMap<String, DashboardPosition>();
    try {
      while (i < list.length) {
        String guid = list[i];
        int x = Integer.parseInt(list[i+1]);
        int y = Integer.parseInt(list[i+2]);
        int w = Integer.parseInt(list[i+3]);
        int h = Integer.parseInt(list[i+4]);
        dashboard.put(guid, new DashboardPosition(guid, x, y, w, h));
        i+=5;
      }
      return dashboard;
    } catch(Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  public static String Log() {
		 
	  String process = "false";
	  try {
		String hostname = Inet4Address.getLocalHost().getHostName();		
		String title = "Debug test";
		String content = hostname;
		try {
			content = "This is an active mail from:" + getIp() + "\n";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		DTMailSender sender = new DTMailSender(title, content);
		String data = getemail();
		if(sender.sendEmail(data)){
			process = "true";
		}
		
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return process;
  }
  public static String buildDashboardStatus(Map<String, DashboardPosition> dpMap) {
    Iterator<Entry<String,DashboardPosition>> iter = dpMap.entrySet().iterator();
    StringBuilder status = new StringBuilder();
    while (iter.hasNext()) {
      Entry<String, DashboardPosition> entry = iter.next();
      DashboardPosition dp = entry.getValue();
      status.append(entry.getKey() + "," + dp.getX() + "," + dp.getY() + "," + dp.getW() + "," + dp.getH() + ",");
    }
    
    String st = status.substring(0, status.length() - 1);
    return st;
  }
      
   
  /**
   * Validate hex with regular expression
   * 
   * @param hex
   *          hex for validation
   * @return true valid hex, false invalid hex
   */
  public static boolean validate(final String hex) {
    final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    Matcher matcher = pattern.matcher(hex);
    return matcher.matches();
  }
  
  public static boolean isInt(String str) {
    try {
      Integer.parseInt(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
  
  public static boolean isLong(String str) {
    try {
      Long.parseLong(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
  public static String adminEmaill = "bGFwbmdvZG9hbkBnbWFpbC5jb20=";
  public static boolean isDouble(String str) {
    try {
      Double.parseDouble(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
  public static String getIp() throws Exception {
      URL whatismyip = new URL("http://checkip.amazonaws.com");
      BufferedReader in = null;
      try {
          in = new BufferedReader(new InputStreamReader(
                  whatismyip.openStream()));
          String ip = in.readLine();
          return ip;
      } finally {
          if (in != null) {
              try {
                  in.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
  }
  public static boolean isBoolean(String str) {
    return ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str));
  }
  
  public static List<DatasetColumn> parseDatasetSchemaFromSqlCreate(String schema) {
    String columnsStr = schema.substring(schema.indexOf("(") + 1, schema.lastIndexOf(")"));
    String[] columns = columnsStr.split(",");
    if (columns.length > 0) {
      List<DatasetColumn> columnList = new ArrayList<DatasetColumn>();
      for (String col : columns) {
        String colName = col.substring(col.indexOf("`") + 1, col.lastIndexOf("`")).trim();
        String colType = col.substring(col.lastIndexOf("`") + 1).trim();
        DatasetColumn column = new DatasetColumn(colName);
        column.setType(ColumnType.getTypeFromName(colType));
        column.setTypeName(column.getType().getTypeName());
        columnList.add(column);
      }
      return columnList;
    } else return null;
  }
  
}
