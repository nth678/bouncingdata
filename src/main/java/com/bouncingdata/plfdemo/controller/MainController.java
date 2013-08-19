package com.bouncingdata.plfdemo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bouncingdata.plfdemo.datastore.pojo.dto.ActionResult;
import com.bouncingdata.plfdemo.datastore.pojo.dto.ExecutionResult;
import com.bouncingdata.plfdemo.datastore.pojo.dto.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.BcDataScript;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Scraper;
import com.bouncingdata.plfdemo.datastore.pojo.model.Tag;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.UserActionLog;
import com.bouncingdata.plfdemo.service.ApplicationExecutor;
import com.bouncingdata.plfdemo.service.ApplicationStoreService;
import com.bouncingdata.plfdemo.service.DatastoreService;
import com.bouncingdata.plfdemo.util.ScriptType;
import com.bouncingdata.plfdemo.util.Utils;

@Controller
public class MainController {

  private Logger                  logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private DatastoreService        datastoreService;

  @Autowired
  private ApplicationStoreService appStoreService;

  @Autowired
  private ApplicationExecutor     appExecutor;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String defaultPage(ModelMap model, Principal principal) {
    return "redirect:/stream";
  }

  @RequestMapping(value = "/main/mystuff", method = RequestMethod.GET)
  public @ResponseBody Map<String, List> getMyStuff(ModelMap model, Principal principal) {
    User user = (User) ((Authentication) principal).getPrincipal();
    if (user == null)
      return null;

    int userId = user.getId();
    Map<String, List> stuffs = new HashMap<String, List>();
    try {

      try {
        ObjectMapper logmapper = new ObjectMapper();
        String data = logmapper.writeValueAsString(new String[] { "0" });
        datastoreService.logUserAction(user.getId(), UserActionLog.ActionCode.MY_STUFF, data);
      } catch (Exception e) {
        logger.debug("Failed to log action", e);
      }
      stuffs.put("analyses", datastoreService.getAnalysisList(userId));
      stuffs.put("scrapers", datastoreService.getScraperList(userId));
      stuffs.put("datasets", datastoreService.getDatasetList(userId));
    } catch (Exception e) {
      logger.error("Failed  to retrieve stuffs, user {} ", user.getUsername());
      logger.error("Exception detail: ", e);
    }
    return stuffs;
  }

  @RequestMapping(value = "/main/dataset", method = RequestMethod.GET)
  public @ResponseBody List<Dataset> getDatasets(ModelMap model, Principal principal) {
    User user = (User) ((Authentication) principal).getPrincipal();
    try {
      if (user == null) {
        return null;
      }
      int userId = user.getId();
      try {
        ObjectMapper logmapper = new ObjectMapper();
        String data = logmapper.writeValueAsString(new String[] { "0" });
        datastoreService.logUserAction(user.getId(), UserActionLog.ActionCode.DATASET, data);
      } catch (Exception e) {
        logger.debug("Failed to log action", e);
      }
      return datastoreService.getDatasetList(userId);
    } catch (Exception e) {
      logger.error("Failed to retrieve dataset list for user " + user.getUsername(), e);
      return null;
    }

  }

  @RequestMapping(value = "/main/table/{datastore}")
  public @ResponseBody List<Dataset> getTables(@PathVariable String datastore) {
    return null;
  }

  @RequestMapping(value = "/main/application", method = RequestMethod.GET)
  public @ResponseBody List<Analysis> getApplications(ModelMap model, Principal principal) {
    User user = (User) ((Authentication) principal).getPrincipal();
    if (user == null)
      return null;

    try {
      ObjectMapper logmapper = new ObjectMapper();
      String data = logmapper.writeValueAsString(new String[] { "0" });
      datastoreService.logUserAction(user.getId(), UserActionLog.ActionCode.APPLICATION, data);
    } catch (Exception e) {
      logger.debug("Failed to log action", e);
    }

    try {
      return datastoreService.getAnalysisList(user.getId());
    } catch (Exception e) {
      logger.error("Failed to retrieve application list for user " + user.getUsername(), e);
      return null;
    }
  }

  @RequestMapping(value = "/main/createapp", method = RequestMethod.POST)
  @ResponseBody
  public BcDataScript createApplication(@RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "language", required = true) String language,
      @RequestParam(value = "description", required = true) String description,
      @RequestParam(value = "code", required = true) String code,
      @RequestParam(value = "isPublic", required = true) boolean isPublic,
      @RequestParam(value = "tags", required = false) String tags,
      @RequestParam(value = "type", required = true) String type, ModelMap model, Principal principal) throws Exception {
    User user = (User) ((Authentication) principal).getPrincipal();
    if (user == null) {
      logger.debug("Can't get the user. Skip application creation.");
      return null;
    }

    ObjectMapper mapper = new ObjectMapper();

    try {
      String data = mapper.writeValueAsString(new String[] { "7", name, language, description, code,
          String.valueOf(isPublic), tags, type });
      datastoreService.logUserAction(user.getId(), UserActionLog.ActionCode.CREATE_APP, data);
    } catch (Exception e) {
      logger.debug("Failed to log action", e);
    }

    int userId = user.getId();

    Set<Tag> tagSet = null;
    if (tags != null && !tags.isEmpty()) {
      tagSet = new HashSet<Tag>();

      try {

        ArrayNode tagArr = mapper.readValue(tags, ArrayNode.class);
        Iterator<JsonNode> iter = tagArr.iterator();
        while (iter.hasNext()) {
          JsonNode node = iter.next();
          int id = node.get("id").getIntValue();
          String value = node.get("tag").getValueAsText();
          Tag tag = new Tag(value);
          if (id < 1) {
            tag.setId(-1);
            tag.setCreateAt(new Date());
            tag.setCreator(user.getId());
            tag.setPopularity(0);
          } else {
            tag.setId(id);
          }
          tagSet.add(tag);
        }

      } catch (IOException e) {
        logger.debug("Can't parse the tags for analysis name {}, user id {}", name, userId);
        logger.debug("Exception details", e);
      }
    }

    String guid = null;
    BcDataScript script;
    if (ScriptType.SCRAPER.getType().equals(type)) {
      script = new Scraper();
      script.setType("scraper");
    } else if (ScriptType.ANALYSIS.getType().equals(type)) {
      script = new Analysis();
      script.setType("analysis");
    } else
      return null;

    script.setName(name);
    script.setDescription(description);
    script.setLanguage(language);
    script.setLineCount(Utils.countLines(code));
    script.setPublished(isPublic);
    script.setTags(tagSet);
    Date date = Utils.getCurrentDate();
    script.setCreateAt(date);
    script.setLastUpdate(date);
    script.setUser(user);
    script.setExecuted(false);
    script.setCreateSource("web");

    try {
      guid = datastoreService.createBcDataScript(script, type);
    } catch (Exception e) {
      logger.error("Failed to create application " + name + " for user " + user.getUsername(), e);
    }

    if (guid == null) {
      logger.error("Can't get the guid of application {} so the code cannot saved", name);
      return null;
    }

    // store application code
    try {
      appStoreService.createApplicationFile(guid, language, code);
    } catch (Exception e) {
      logger.error("Error occurs when save application code, guid {}", guid);
    }

    if (ScriptType.ANALYSIS.getType().equals(type)) {
      script = datastoreService.getAnalysis(script.getId());
    } else if (ScriptType.SCRAPER.getType().equals(type)) {
      script = datastoreService.getScraperByGuid(guid);
    }
    return script;
  }

  @RequestMapping(value = "/main/execute", method = RequestMethod.POST)
  public @ResponseBody ExecutionResult executeApp(@RequestParam(value = "code", required = true) String code,
      @RequestParam(value = "language", required = true) String language,
      @RequestParam(value = "type", required = true) String type, ModelMap model, Principal principal) {
    // invoke executor to execute code, pass the id as parameter
    User user = (User) ((Authentication) principal).getPrincipal();
    if (user == null)
      return null;
    try {

      try {
        ObjectMapper logmapper = new ObjectMapper();
        String data = logmapper.writeValueAsString(new String[] { "3", code, language, type });
        datastoreService.logUserAction(user.getId(), UserActionLog.ActionCode.EXECUTE, data);
      } catch (Exception e) {
        logger.debug("Failed to log action", e);
      }
      
      ExecutionResult result = null;
      if ("python".equals(language)) {
        result = appExecutor.executePython(null, code, user);
      } else if ("r".equals(language)) {
        result = appExecutor.executeR(null, code, user);
      } else {
        return new ExecutionResult(null, "Not support", 0, 0, 1, "Not support");
      }
      
      return result;
    } catch (Exception e) {
      logger.error("Error occurs when executing analysis code", e);
      return new ExecutionResult(null, "Error", 0, 0, 1, "Error occurs");
    }
  }

  @RequestMapping(value = "/main/search", method = RequestMethod.GET)
  public String search(@RequestParam(value = "query", required = true) String query,
      @RequestParam(value = "criteria", required = true) String criteria, ModelMap model, Principal principal) {
    SearchResult result = null;
    User user = (User) ((Authentication) principal).getPrincipal();
    try {

      try {
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(new String[] { "2", query, criteria });
        datastoreService.logUserAction(user.getId(), UserActionLog.ActionCode.SEARCH, data);
      } catch (Exception e) {
        logger.debug("Failed to log action", e);
      }
      if ("global".equals(criteria)) {
        result = datastoreService.search(query.toLowerCase());
      } else if ("mystuff".equals(criteria)) {
        result = datastoreService.search(query, user.getId());
      }
    } catch (Exception e) {
      logger.error("Failed to execute search with query: " + query, e);
    }
    // return result;
    model.addAttribute("query", query);
    model.addAttribute("searchResult", result);
    return "search";
  }

  @RequestMapping(value = "/main/browsersearch", method = RequestMethod.GET)
  public @ResponseBody SearchResult browerSearch(@RequestParam(value = "query", required = true) String query,
      @RequestParam(value = "criteria", required = true) String criteria, ModelMap model, Principal principal) {
    SearchResult result = null;
    User user = (User) ((Authentication) principal).getPrincipal();
    try {

      try {
        ObjectMapper logmapper = new ObjectMapper();
        String data = logmapper.writeValueAsString(new String[] { "2", query, criteria });
        datastoreService.logUserAction(user.getId(), UserActionLog.ActionCode.BROWSE_SEARCH, data);
      } catch (Exception e) {
        logger.debug("Failed to log action", e);
      }
      if ("global".equals(criteria)) {
        result = datastoreService.search(query.toLowerCase());
      } else if ("mystuff".equals(criteria)) {
        result = datastoreService.search(query.toLowerCase(), user.getId());
      }
    } catch (Exception e) {
      logger.debug("Failed to execute browser search with query: " + query, e);
    }
    return result;
  }

  @RequestMapping(value = "/main/publish", method = RequestMethod.POST)
  public @ResponseBody ActionResult publish(@RequestParam(value = "guid", required = true) String guid,
      @RequestParam(value = "message", required = true) String message, Principal principal) throws Exception {
    User user = (User) ((Authentication) principal).getPrincipal();
    try {
      ObjectMapper logmapper = new ObjectMapper();
      String data = logmapper.writeValueAsString(new String[] { "2", guid, message });
      datastoreService.logUserAction(user.getId(), UserActionLog.ActionCode.PUBLISH, data);
    } catch (Exception e) {
      logger.debug("Failed to log action", e);
    }

    Analysis analysis = datastoreService.getAnalysisByGuid(guid);
    if (analysis == null)
      return new ActionResult(-1, "Analysis not found");
    datastoreService.createAnalysisPost(user, analysis, message);
    return new ActionResult(0, "OK");
  }

}
