package com.bouncingdata.plfdemo.util.dataparsing;

import java.util.HashMap;
import java.util.Map;


public class DataParserFactory {
  
  private DataParserFactory() {}
  
  public static DataParser getDataParser(int type) {
    switch(type) {
    case FileType.EXCEL:
      return new ExcelParser();
    case FileType.CSV:
      return new CsvParser();
    case FileType.TEXT:
      return new TextParser();
    default:
      return null;
    }
  }
  
  public static class FileType {
    public static final int EXCEL = 1;
    public static final int CSV = 2;
    public static final int TEXT = 3;
  }
  
  public static Map<Class, String> datamap = new HashMap<Class, String>() {{
    put(java.lang.Integer.class, "INTEGER");
    put(java.lang.Long.class, "LONG");
    put(java.lang.String.class, "VARCHAR");
    put(java.lang.Boolean.class, "BIT");
    put(java.lang.Double.class, "DOUBLE");
  }};

  
  public static void main(String args[]) {
    int a =  1;
    Class clazz = java.lang.Long.class;
    
    //System.out.println(a.getClass().getCanonicalName());
    System.out.println(((Object)a).getClass().getCanonicalName());
  }

}
