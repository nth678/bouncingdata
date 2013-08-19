package com.bouncingdata.plfdemo.service;

import java.util.Properties;


public class MailService {
  
  private Properties mailConfiguration;
  
  public void setMailConfiguration(Properties mailConfig) {
    this.mailConfiguration = mailConfig;
  }
  
  public void printConfiguration() {
    for (Object key : mailConfiguration.keySet()) {
      String k = (String) key;
      String v = mailConfiguration.getProperty(k);
      System.out.format("Key %s, value %s", k, v);
    }
  }
}
