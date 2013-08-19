package com.bouncingdata.plfdemo.util;

public enum PageType {
  ANALYSIS("analysis"), SCRAPER("scraper"), DATASET("dataset");
  
  String type;
  private PageType(String s) {
    type = s;
  }
  
  public String getType() {
    return type;
  }
  
  public static PageType getPageType(String s) {
    if ("analysis".equalsIgnoreCase(s)) {
      return ANALYSIS;
    } else if ("scraper".equalsIgnoreCase(s)) {
      return SCRAPER;
    } else if ("dataset".equalsIgnoreCase(s)) {
      return DATASET;
    } else return null;
  }
  
}
