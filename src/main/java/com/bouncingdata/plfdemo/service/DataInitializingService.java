package com.bouncingdata.plfdemo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.pojo.model.Tag;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;

@Transactional
public class DataInitializingService {
  
  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private DataStorage dataStorage;
  
  public void setDataStorage(DataStorage ds) {
    this.dataStorage = ds;
  }
  
  public void init() {
    logger.info("Initializing CustomUserDetailService...");
    User demo = dataStorage.findUserByUsername("demo");
    if (demo == null) {
      demo = new User();
      demo.setEmail("demo@bouncingdata.com");
      demo.setUsername("demo");
      demo.setEnabled(true);
      demo.setPassword("demo");
      demo.setJoinedDate(new Date());
      dataStorage.createUser(demo);
    }
    logger.info("Finished CustomUserDetailService.");
    
    Set<Tag> tagset = new HashSet<Tag>();
    tagset.add(new Tag("Football"));
    tagset.add(new Tag("Money"));
    tagset.add(new Tag("Finance"));
    tagset.add(new Tag("Health"));
    tagset.add(new Tag("Education"));
    tagset.add(new Tag("Sports"));
    tagset.add(new Tag("US"));
    tagset.add(new Tag("Economics"));
    tagset.add(new Tag("WorldBank"));
    tagset.add(new Tag("Payroll"));
    
    Iterator<Tag> iter = tagset.iterator();
    while (iter.hasNext()) {
      Tag tag = iter.next();
      Tag t = dataStorage.getTag(tag.getTag());
      if (t != null) {
        iter.remove();
      }
    }
    
    dataStorage.createTags(new ArrayList<Tag>(tagset));
  }
  
  public void destroy() {
    
  }
}
