package com.bouncingdata.plfdemo.datastore.pojo.dto;

/**
 * Represents the common result returned from the REST service method. <br />
 * <code>code</code> represents the status code, status code < 0 means 
 *
 */
public class ActionResult {
  private int code;
  private String message;
  private Object object;

  public ActionResult(int code, String message) {
    this.code = code;
    this.message = message;
  }
  
  public ActionResult(int code, String message, Object object) {
    this.code = code;
    this.message = message;
    this.object = object;
  }
  
  public void setObject(Object object) {
    this.object = object;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getObject() {
    return object;
  }
}
