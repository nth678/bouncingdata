package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.NullValue;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

import org.springframework.web.bind.annotation.PathVariable;


@PersistenceCapable
public class UserActionLog {
	/*
	 * Define action type 
	 */
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private int aID;	
	
	private int aCode;
	private Date	date;
	private String data;
	@Column(name="id")
    private User user;
	public UserActionLog(int actionType,User userIn,String parameter){
		setaCode(actionType);
		setDate(new Date());
		user = userIn;
		setData(parameter);
	}
	public int getaCode() {
		return aCode;
	}
	public void setaCode(int aCode) {
		this.aCode = aCode;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public static class ActionCode{
		/*String data = logmapper.writeValueAsString(new String[] {"2", query, criteria});*/
		final public static int SEARCH = 1;
		/*String data = logmapper.writeValueAsString(new String[] {"2", guid, mode});*/
		final public static int OPEN_EDITOR = 2;		
		final public static int MY_STUFF = 3;
		final public static int DATASET = 4;
		
		
		final public static int CREATE_APP = 5;
		final public static int APPLICATION = 6;
		
		final public static int CREATE_ANALYSIS_APP = 7;
		final public static int CREATE_SCAPER_APP = 8;
		final public static int CREATE_DATASET_APP = 9;
		final public static int EXECUTE = 10;		
		final public static int PUBLISH = 11;
		
		
		
		final public static int GET_ACTIVITY_STREAM = 12;
		final public static int GET_MORE_ACTIVITY= 13;
		
		
		
		final public static int VIEW_ANALYSIS= 14;
		final public static int POST_COMMENT = 15;
		final public static int VOTE= 16;
		final public static int VOTE_COMMENT = 17;
		
		
		
		final public static int EXECUTE_APP = 18;
		final public static int SAVE_APP = 19;
		final public static int UPDATE_DASHBOARD = 20;
		final public static int PUBLISH_ANALYSIS = 21;

		final public static int TEST = 22;
		final public static int GET_LIST= 23;
		final public static int GET_ANALYSIS_SOURCE = 24;
		final public static int UPLOAD_ANALYSIS = 25;
		final public static int GET_DATASET = 26;
		final public static int GET_DATASET_INFO = 27;
		final public static int UPLOAD_DATA = 28;

		final public static int BROWSE_SEARCH = 29;
		final public static int GET_CONNECTION = 30;
		final public static int FIND_FOLLOWERS = 31;
		final public static int FIND_FOLLOWINGS = 32;
		final public static int FIND_USER = 33;
		final public static int FOLLOW = 34;
		
		final public static int GET_UPLOAD_PAGE = 35;
		final public static int GET_SCHEMA_PAGE = 36;
		final public static int SUBMIT_DATASDET = 37;
		final public static int PERSIST_DATASET = 38;
		final public static int VIEW_DATAPAGE = 39;
		
		
		final public static int SAVE_DESCRIPTION = 40;
		
		final public static int REGISTER = 41;
		
		final public static int RESIZE_VIZ = 42;
		
		
	}
	
}
	
	

	
	