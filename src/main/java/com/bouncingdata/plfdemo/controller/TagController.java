package com.bouncingdata.plfdemo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Tag;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.service.DatastoreService;

@Controller
@RequestMapping(value = "/tag")
public class TagController {

	private Logger logger = LoggerFactory.getLogger(AnalysisController.class);

	@Autowired
	private DatastoreService datastoreService;

	@RequestMapping(value = "/{tag}", method = RequestMethod.GET)
	public String getTagPage(@PathVariable String tag, ModelMap model) {
		for (String tagItem : tag.split("[,]+")) {
			Tag t = datastoreService.getTag(tagItem);
			if (t == null) {
				model.addAttribute("errorMsg", "Tag \"" + tagItem
						+ "\" does not exist.");
				return "error";
			}

			List<Analysis> analyses = datastoreService
					.getAnalysesByTag(tagItem);
			model.addAttribute("anlsList", analyses);
			model.addAttribute("tag", tagItem);
			model.addAttribute("menuId", tagItem);
		}
		return "tag";
	}

	@RequestMapping(value = "/addtag", method = RequestMethod.POST)
	public @ResponseBody
	ActionResult addTag(
			@RequestParam(value = "guid", required = true) String guid,
			@RequestParam(value = "tag", required = true) String taglist,
			@RequestParam(value = "type", required = true) String type,
			ModelMap model, Principal principal) throws Exception {

		int numAvaiableTags = 0;
		int numberTagtoAdd = 0;
		String result = "";
		User user = (User) ((Authentication) principal).getPrincipal();
		if (!"analysis".equals(type) && !"scraper".equals(type)
				&& !"dataset".equals(type)) {
			return new ActionResult(-1, "Unknown type");
		}

		if ("analysis".equals(type)) {
			Analysis anls = datastoreService.getAnalysisByGuid(guid);
			if (anls == null) {
				return new ActionResult(-1, "Error: Analysis does not exist");
			}

			if (!user.getUsername().equals(anls.getUser().getUsername())) {
				return new ActionResult(-1,
						"Error: User does not have permission to add tag");
			}

			Set<Tag> tagset = anls.getTags();

			for (String tagItem : taglist.split("[,]+")) {
				tagItem = tagItem.trim();
				 if(tagItem== null || tagItem.equals(""))
		    		  break;
				numberTagtoAdd++;
				Tag tagObj = datastoreService.getTag(tagItem);

				if (tagObj == null) {
					logger.debug(
							"Tag {} does not exist. Trying create new tag.",
							tagItem);
					try {
						datastoreService.createTag(tagItem);
						tagObj = datastoreService.getTag(tagItem);

					} catch (Exception e) {
						logger.debug("Failed to create new tag {}", tagItem);
						logger.debug("", e);
						// return new ActionResult(-1, result);
					}
				}

				boolean flag = true;

				if (tagset != null) {
					for (Tag t : tagset) {
						if (t.getTag().equals(tagItem)) {
							flag = false;
						}
					}
				} else {
					flag = true;
				}

				if (flag) {
					result += tagItem + ",";
					datastoreService.addAnalysisTag(anls.getId(),
							tagObj.getId());
					tagset.add(tagObj);
				}

			}

		}

		if ("dataset".equals(type)) {
			Dataset dataset = datastoreService.getDatasetByGuid(guid);
			if (dataset == null)
				return new ActionResult(-1, "Dataset does not exist");

			if (!user.getUsername().equals(dataset.getUser().getUsername())) {
				return new ActionResult(-1,
						"Error: User does not have permission to add tag");
			}

			Set<Tag> tagset = dataset.getTags();
			
			for (String tagItem : taglist.split("[,]+")) {
				tagItem = tagItem.trim();
				 if(tagItem== null || tagItem.equals(""))
		    		  break;
				 
				numberTagtoAdd++;
				Tag tagObj = datastoreService.getTag(tagItem);
				
				if (tagObj == null) {
					logger.debug(
							"Tag {} does not exist. Trying create new tag.",
							tagItem);
					try {
						datastoreService.createTag(tagItem);
						tagObj = datastoreService.getTag(tagItem);
					} catch (Exception e) {
						logger.debug("Failed to create new tag {}", tagItem);
						logger.debug("", e);
						// return new ActionResult(-1, result);
					}

				}
				
				
				
				
				boolean flag = true;

				if (tagset != null) {
					for (Tag t : tagset) {
						if (t.getTag().equals(tagItem)) {
							flag = false;
						}
					}
				} else {
					flag = true;
				}

				if (flag) {
					result += tagItem + ",";
					datastoreService.addDatasetTag(dataset.getId(),
							tagObj.getId());
					tagset.add(tagObj);
				}

			}

		}

		if ("scraper".equals(type)) {
			/*
			 * Scraper scraper = datastoreService.getScraperByGuid(guid); if
			 * (scraper == null) return new ActionResult(-1,
			 * "Scraper does not exist");
			 * 
			 * Tag tagObj = datastoreService.getTag(tag); if (tagObj == null) {
			 * logger.debug("Tag {} does not exist. Trying create new tag.",
			 * tag); try { datastoreService.createTag(tag); tagObj =
			 * datastoreService.getTag(tag); } catch (Exception e) {
			 * logger.debug("Failed to create new tag {}", tag);
			 * logger.debug("", e); return new ActionResult(-1,
			 * "Failed to create new tag"); } }
			 */
			return new ActionResult(-1, "No support for scraper now");

		}

		if (result != "") {
			result = result.substring(0, result.length() - 1);
		}

		if (numAvaiableTags == numberTagtoAdd) {
			return new ActionResult(-1, result);
		} else {
			return new ActionResult(0, result);
		}

	}

	@RequestMapping(value = "/removetag", method = RequestMethod.POST)
	public @ResponseBody
	ActionResult removeTag(
			@RequestParam(value = "guid", required = true) String guid,
			@RequestParam(value = "tag", required = true) String tag,
			@RequestParam(value = "type", required = true) String type,
			ModelMap model, Principal principal) throws Exception {

		if (!"analysis".equals(type) && !"scraper".equals(type)
				&& !"dataset".equals(type)) {
			return new ActionResult(-1, "Unknown type");
		}

		User user = (User) ((Authentication) principal).getPrincipal();
		if (user == null) {
			return new ActionResult(-1, "Error: User does not exist");
		}

		if ("analysis".equals(type)) {
			Analysis anls = datastoreService.getAnalysisByGuid(guid);
			if (anls == null) {
				return new ActionResult(-1, "Error: Analysis does not exist");
			}

			if (!user.getUsername().equals(anls.getUser().getUsername())) {
				return new ActionResult(-1,
						"Error: User does not have permission to remove tag");
			}

			Tag tagObj = datastoreService.getTag(tag);
			if (tagObj == null) {
				return new ActionResult(-1, "Tag does not exist");
			}

			try {
				datastoreService.removeAnalysisTag(anls, tagObj);
				return new ActionResult(0, "OK");
			} catch (Exception e) {
				logger.debug("Failed to remove tag", e);
				return new ActionResult(-1, "Failed");
			}
		}

		if ("dataset".equals(type)) {
			Dataset dts = datastoreService.getDatasetByGuid(guid);
			if (dts == null) {
				return new ActionResult(-1, "Error: Dataset does not exist");
			}

			if (!user.getUsername().equals(dts.getUser().getUsername())) {
				return new ActionResult(-1,
						"Error: User does not have permission to remove tag");
			}

			Tag tagObj = datastoreService.getTag(tag);
			if (tagObj == null) {
				return new ActionResult(-1, "Tag does not exist");
			}

			try {
				datastoreService.removeDatasetTag(dts, tagObj);
				return new ActionResult(0, "OK");
			} catch (Exception e) {
				logger.debug("Failed to remove tag", e);
				return new ActionResult(-1, "Failed");
			}
		}

		return new ActionResult(-1, "No support");

	}

}
