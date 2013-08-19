package com.bouncingdata.plfdemo.datastore.pojo.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class PageView {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  private int id;
  @Index
  private int objectId;
  private int count;
  private String type;
  
  public PageView(int objectId, int count, String type) {
    this.objectId = objectId;
    this.count = count;
    this.type = type;
  }
  
  public int getObjectId() {
    return objectId;
  }
  public void setObjectId(int objectId) {
    this.objectId = objectId;
  }
  public int getCount() {
    return count;
  }
  public void setCount(int count) {
    this.count = count;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
}
