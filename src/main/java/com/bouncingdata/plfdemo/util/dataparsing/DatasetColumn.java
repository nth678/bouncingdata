package com.bouncingdata.plfdemo.util.dataparsing;

import org.codehaus.jackson.annotate.JsonIgnore;

public class DatasetColumn {
  
  private String name;
  private ColumnType type;
  private String typeName;
  
  public DatasetColumn(String name) {
    this.name = name;
  }
  
  @JsonIgnore
  public ColumnType getType() {
    return type;
  }

  public void setType(ColumnType type) {
    this.type = type;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  /**
   *
   */
  public enum ColumnType {
    INTEGER("Integer"), LONG("Long"), DOUBLE("Double"), BOOLEAN("Boolean"), STRING("String");
    
    private String typeName;
    
    private ColumnType(String s) {
      this.typeName = s;
    }
    
    public String getTypeName() {
      return typeName;
    }
    
    public static ColumnType getTypeFromName(String typeName) {
      if ("Integer".equalsIgnoreCase(typeName)) return INTEGER;
      else if ("Long".equalsIgnoreCase(typeName)) return LONG;
      else if ("Double".equalsIgnoreCase(typeName)) return DOUBLE;
      else if ("Boolean".equalsIgnoreCase(typeName) || "Bit".equalsIgnoreCase(typeName)) return BOOLEAN;
      else if ("String".equalsIgnoreCase(typeName) || "Text".equalsIgnoreCase(typeName)) return STRING;
      else return null;
    }
  }
  
}
