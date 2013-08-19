package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.NullValue;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

/**
 * This class represents an entity in BouncingData, it can be an Analysis, Dataset or Scraper
 *
 */
public class BcDataEntity {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  private int id;
  private String name;
  @Index
  private @Unique String guid;
  @Persistent(defaultFetchGroup="true", nullValue=NullValue.EXCEPTION)
  private User user;
  private Date createAt;
  private Date lastUpdate;
  private String type;
}
