package com.bouncingdata.plfdemo.datastore;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

import com.bouncingdata.plfdemo.datastore.pojo.model.Activity;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Tag;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;

@ContextConfiguration
public class JdoDataStorageTest extends AbstractJUnit38SpringContextTests {
  
  @Autowired
  private JdoDataStorage jdoDataStorage;
  
  public void setUp() {
    User demo = jdoDataStorage.findUserByUsername("test");
    if (demo == null) {
      // create user demo
      demo = new User();
      demo.setUsername("test");
      demo.setEmail("test@bouncingdata.com");
      jdoDataStorage.createUser(demo);
    }
  }
  
  public void tearDown() {
    User demo = jdoDataStorage.findUserByUsername("test");
    if (demo != null) jdoDataStorage.deleteUser(demo.getId());
  }
  
  public void testJdoDataStorage() {
    assertNotNull(jdoDataStorage);
    
    User demo = jdoDataStorage.findUserByUsername("test");
    assertNotNull(demo);
    List<Analysis> apps = jdoDataStorage.getAnalysisList(demo.getId());
    //assertNotNull(apps);
    System.out.println("Number of application by demo: " + apps.size());
  }
    
  public void _testCreateAnalysis() {
    Analysis anls = new Analysis();
    anls.setName("testAnalysis");
    anls.setLanguage("python");
    anls.setDescription("This is just a test analysis");
    //anls.setTags("test");
    String guid="abcd12345ef";
    anls.setGuid(guid);
    User demo = jdoDataStorage.findUserByUsername("test");
    anls.setUser(demo);
    jdoDataStorage.createAnalysis(anls);
    Analysis anls1 = jdoDataStorage.getAnalysisByGuid(guid);
    assertNotNull(anls1);
    assertTrue(anls1.getName().equals("testAnalysis"));
    jdoDataStorage.deleteAnalysis(anls1.getId());
  }
  
  public void _testUpdateApplication() {
    Analysis anls = new Analysis();
    anls.setName("testAnalysis");
    anls.setLanguage("python");
    anls.setDescription("This is just a test analysis");
    //anls.setTags("test");
    String guid="abcd12345ef";
    anls.setGuid(guid);
    User demo = jdoDataStorage.findUserByUsername("test");
    anls.setUser(demo);
    jdoDataStorage.createAnalysis(anls);
    Analysis anls1 = jdoDataStorage.getAnalysisByGuid(guid);
    anls1.setName("testAnalysis1");
    jdoDataStorage.updateAnalysis(anls1);
    anls1 = jdoDataStorage.getAnalysisByGuid(guid);
    assertNotNull(anls1);
    assertTrue(anls1.getName().equals("testAnalysis1"));
    jdoDataStorage.deleteAnalysis(anls1.getId());
  }
  
  public void _testGetFeed() {
    User demo = jdoDataStorage.findUserByUsername("test");
    assertNotNull(demo);
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -1);
    List<Activity> activities = jdoDataStorage.getFeed(demo.getId(), 20);
    assertNotNull(activities);
    //assertTrue(activities.size() > 0);
  }
  
  public void _testGetScraperDatasets() {
    int scraperId = 4;
    List<Dataset> datasetList = jdoDataStorage.getScraperDatasets(scraperId);
    System.out.println("Number of dataset for scraper 4: " + datasetList.size());
  }
  

  
  public void _testCreateTag() {
	  Tag tag;
	  /*Create tag
	   * tag = new Tag("ZACHMAN");
	  jdoDataStorage.createTag(tag);*/
	 
	 //tag = jdoDataStorage.getTag("EA");	  
	 // assertNotNull(tag);	  	  
		
	  
	  
	 /* Create dataset tag
	  * jdoDataStorage.addDataSetTag(1, tag.getId());
	  jdoDataStorage.addDataSetTag(2, tag.getId());
	  jdoDataStorage.addDataSetTag(6, tag.getId());
	  jdoDataStorage.addDataSetTag(7, tag.getId());
	  jdoDataStorage.addDataSetTag(8, tag.getId());
	  jdoDataStorage.addDataSetTag(9, tag.getId());*/
	  
	
	 /* Delete dataset tag
	  * jdoDataStorage.deleteDataSetTag(1, tag.getId());
	  jdoDataStorage.deleteDataSetTag(2, tag.getId());
	  jdoDataStorage.deleteDataSetTag(6, tag.getId());*/
	  
	  /*Queries
	   * jdoDataStorage.getDataSetByTag(tag.getId());
	  jdoDataStorage.getTagByDataSet(7);
	  */
	  	  
  }
  
  public void _testLogUserAction() {
	  Tag tag;
	  /*Create tag
	   * tag = new Tag("ZACHMAN");
	  jdoDataStorage.createTag(tag);*/
	 
	 tag = jdoDataStorage.getTag("EA");	  
	 assertNotNull(tag);	  	  
	 jdoDataStorage.getAnalysisList(31);	
	  
	  
	 /* Create dataset tag
	  * jdoDataStorage.addDataSetTag(1, tag.getId());
	  jdoDataStorage.addDataSetTag(2, tag.getId());
	  jdoDataStorage.addDataSetTag(6, tag.getId());
	  jdoDataStorage.addDataSetTag(7, tag.getId());
	  jdoDataStorage.addDataSetTag(8, tag.getId());
	  jdoDataStorage.addDataSetTag(9, tag.getId());*/
	  
	
	 /* Delete dataset tag
	  * jdoDataStorage.deleteDataSetTag(1, tag.getId());
	  jdoDataStorage.deleteDataSetTag(2, tag.getId());
	  jdoDataStorage.deleteDataSetTag(6, tag.getId());*/
	  
	  /*Queries
	   * jdoDataStorage.getDataSetByTag(tag.getId());
	  jdoDataStorage.getTagByDataSet(7);
	  */
	  	  
  }
}