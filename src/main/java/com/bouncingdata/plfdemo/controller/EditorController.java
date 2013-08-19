package com.bouncingdata.plfdemo.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jackson.map.ObjectMapper;
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
import org.springframework.web.context.request.WebRequest;

import com.bouncingdata.plfdemo.datastore.pojo.dto.Attachment;
import com.bouncingdata.plfdemo.datastore.pojo.dto.DashboardDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.DashboardPosition;
import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationType;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.AnalysisDataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Scraper;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.UserActionLog;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;
import com.bouncingdata.plfdemo.service.ApplicationExecutor;
import com.bouncingdata.plfdemo.service.ApplicationStoreService;
import com.bouncingdata.plfdemo.service.DatastoreService;
import com.bouncingdata.plfdemo.util.Utils;

@Controller
@RequestMapping(value = "/editor")
public class EditorController {

  private Logger                  logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private DatastoreService        datastoreService;

  @Autowired
  private ApplicationStoreService appStoreService;

  @Autowired
  private ApplicationExecutor     appExecutor;

  @RequestMapping(value = "/scraper/{guid}/{mode}", method = RequestMethod.GET)
  public String openSpEditor(@PathVariable String guid, @PathVariable String mode, ModelMap model, Principal principal, WebRequest req) {
	  if (!"edit".equalsIgnoreCase(mode)) {
	      model.addAttribute("errorMsg", "Unknown page.");
	      return "error";
	  }
	  
	  try { 
		  Scraper anls = datastoreService.getScraperByGuid(guid);
	      if (anls == null) {
	        model.addAttribute("errorMsg", "Scraper not found!");
	        return "error";
	      }

	      User user = (User) ((Authentication) principal).getPrincipal();
	      if (user == null || !user.getUsername().equals(anls.getUser().getUsername())) {
	        model.addAttribute("errorMsg", "You have no permission to edit this scraper!");
	        return "error";
	      }

	      try {
	        ObjectMapper logmapper = new ObjectMapper();
	        String data = logmapper.writeValueAsString(new String[] { "2", guid, mode });
	        datastoreService.logUserAction(user.getId(), UserActionLog.ActionCode.OPEN_EDITOR, data);
	      } catch (Exception e) {
	        logger.debug("Failed to log action", e);
	      }
	      model.addAttribute("anls", anls);

	      String code = appStoreService.getScriptCode(guid, null);
	      model.addAttribute("anlsCode", StringEscapeUtils.escapeJavaScript(code));
	        
	      if ("edit".equals(mode)) {
	          String feature = req.getParameter("feature");
	          if ("new".equals(feature)) {
	            model.addAttribute("feature", "new");
	          } else {
	            model.addAttribute("feature", "edit");
	            String execId = req.getParameter("execid");
	            if (execId != null && !execId.isEmpty()) {
	              // do something
	              model.addAttribute("lastOutput", StringEscapeUtils.escapeJavaScript(anls.getLastOutput()));
	            }
	          }
	          return "speditor";
	        }
	      
	  } catch (Exception e) {
	      logger.debug("Failed to load analysis {}", guid);
	      model.addAttribute("errorMsg", e.getMessage());
	      return "error";
	  }

    return "speditor";
  }
  
  @RequestMapping(value = "/anls/{guid}/{mode}", method = RequestMethod.GET)
  public String openEditor(@PathVariable String guid, @PathVariable String mode, ModelMap model, Principal principal, WebRequest req) {

    if (!"edit".equalsIgnoreCase(mode) && !"size".equalsIgnoreCase(mode) 
        && !"describe".equalsIgnoreCase(mode)) {
      model.addAttribute("errorMsg", "Unknown page.");
      return "error";
    }

    // business logic here
    try {
      Analysis anls = datastoreService.getAnalysisByGuid(guid);
      if (anls == null) {
        model.addAttribute("errorMsg", "Analysis not found!");
        return "error";
      }

      User user = (User) ((Authentication) principal).getPrincipal();
      if (user == null || !user.getUsername().equals(anls.getUser().getUsername())) {
        model.addAttribute("errorMsg", "You have no permission to edit this analysis!");
        return "error";
      }

      try {
        ObjectMapper logmapper = new ObjectMapper();
        String data = logmapper.writeValueAsString(new String[] { "2", guid, mode });
        datastoreService.logUserAction(user.getId(), UserActionLog.ActionCode.OPEN_EDITOR, data);
      } catch (Exception e) {
        logger.debug("Failed to log action", e);
      }
      model.addAttribute("anls", anls);

      String code = appStoreService.getScriptCode(guid, null);
      model.addAttribute("anlsCode", StringEscapeUtils.escapeJavaScript(code));
            
      if ("edit".equals(mode)) {
        String feature = req.getParameter("feature");
        if ("new".equals(feature)) {
          model.addAttribute("feature", "new");
        } else {
          model.addAttribute("feature", "edit");
          String execId = req.getParameter("execid");
          if (execId != null && !execId.isEmpty()) {
            // do something
            model.addAttribute("lastOutput", StringEscapeUtils.escapeJavaScript(anls.getLastOutput()));
          }
        }
        return "editor";
        // Vinhpq : change mode for clone feature
//        mode = "size";
      }

      if ("size".equals(mode)) {
        List<Visualization> visuals = datastoreService.getAnalysisVisualizations(anls.getId());
        Map<String, VisualizationDetail> visualsMap = null;
        if (visuals != null) {
          visualsMap = new HashMap<String, VisualizationDetail>();
          for (Visualization v : visuals) {
            if ("html".equals(v.getType())) {
              visualsMap.put(v.getName(), new VisualizationDetail(v.getGuid(), "visualize/app/" + guid + "/" + v.getGuid() + "/html",
                      VisualizationType.getVisualType(v.getType())));
            } else if ("png".equals(v.getType())) {
              try {
                String source = appStoreService.getVisualization(guid, v.getGuid(), v.getType());
                visualsMap.put(v.getName(), new VisualizationDetail(v.getGuid(), source, VisualizationType.getVisualType(v.getType())));
              } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                  logger.debug("Error occurs when retrieving visualizations {} from analysis {}", v.getGuid(), guid);
                  logger.debug("Exception detail", e);
                }
                continue;
              }
            }
          }
        }
        Map<String, DashboardPosition> dashboard = Utils.parseDashboard(anls);

        DashboardDetail dbDetail = new DashboardDetail(visualsMap, dashboard);
        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("dashboardDetail", mapper.writeValueAsString(dbDetail));

        // retrives related datasets
        try {
          List<AnalysisDataset> relations = datastoreService.getAnalysisDatasets(anls.getId());
          if (relations != null) {
            // key: dataset guid, value: dataset name
            Map<String, String> datasetList = new HashMap<String, String>();
            for (AnalysisDataset relation : relations) {
              Dataset ds = relation.getDataset();
              datasetList.put(ds.getGuid(), ds.getName());
            }
            model.addAttribute("datasetList", datasetList);
          }
        } catch (Exception e) {
          logger.debug("Error when trying to get relation datasets", e);
        }

        List<Attachment> attachments = appStoreService.getAttachmentData(guid);
        model.addAttribute("attachments", attachments);
        
        String execId = req.getParameter("execid");
        if (execId != null && !execId.isEmpty()) {
          model.addAttribute("execId", execId);
        }
        
        return "size";
      }

      if ("describe".equals(mode)) {
        return "describe";
      }

    } catch (Exception e) {
      logger.debug("Failed to load analysis {}", guid);
      model.addAttribute("errorMsg", e.getMessage());
      return "error";
    }

    return "editor";
  }

  @RequestMapping(value = "/anls/{guid}/describe", method = RequestMethod.POST)
  public @ResponseBody void saveDescription(@PathVariable String guid, @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "description", required = true) String description,
      @RequestParam(value = "isPublic", required = true) boolean isPublic, ModelMap model, Principal principal) {
    User user = (User) ((Authentication) principal).getPrincipal();
    if (user == null) {
      return;
    }

    try {
      try {
        ObjectMapper logmapper = new ObjectMapper();
        String data = logmapper.writeValueAsString(new String[] { "3", guid, name, description });
        datastoreService.logUserAction(user.getId(), UserActionLog.ActionCode.SAVE_DESCRIPTION, data);
      } catch (Exception e) {
        logger.debug("Failed to log action", e);
      }

      Analysis anls = datastoreService.getAnalysisByGuid(guid);
      anls.setName(name);
      anls.setDescription(description);
      anls.setPublished(isPublic);
      datastoreService.updateAnalysis(anls);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @RequestMapping(value = "/scraper/{guid}/describe", method = RequestMethod.POST)
  public @ResponseBody void saveDescriptionScraper(@PathVariable String guid, @RequestParam(value = "name", required = true) String name,
      @RequestParam(value = "description", required = true) String description,
      @RequestParam(value = "isPublic", required = true) boolean isPublic, ModelMap model, Principal principal) {
    User user = (User) ((Authentication) principal).getPrincipal();
    if (user == null) {
      return;
    }

    try {
      try {
        ObjectMapper logmapper = new ObjectMapper();
        String data = logmapper.writeValueAsString(new String[] { "3", guid, name, description });
        datastoreService.logUserAction(user.getId(), UserActionLog.ActionCode.SAVE_DESCRIPTION, data);
      } catch (Exception e) {
        logger.debug("Failed to log action", e);
      }

      Scraper scp = datastoreService.getScraperByGuid(guid);
      scp.setName(name);
      scp.setDescription(description);
      scp.setPublished(isPublic);
      datastoreService.updateScraper(scp);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
  
}
