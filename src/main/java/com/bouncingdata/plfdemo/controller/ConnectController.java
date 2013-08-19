package com.bouncingdata.plfdemo.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

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

import com.bouncingdata.plfdemo.datastore.pojo.dto.UserInfo;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.UserActionLog;
import com.bouncingdata.plfdemo.service.DatastoreService;

@Controller
@RequestMapping(value = "/connect")
public class ConnectController {

	private Logger logger = LoggerFactory.getLogger(ConnectController.class);

	@Autowired
	private DatastoreService datastoreService;

	@RequestMapping(method = RequestMethod.GET)
	public String connect(ModelMap model, Principal principal) {
		return "connect";
	}

	@SuppressWarnings("serial")
	@RequestMapping(value = "/connections", method = RequestMethod.GET)
	public @ResponseBody Object getConnections(ModelMap model, Principal principal) throws Exception {
		User user = (User) ((Authentication) principal).getPrincipal();
		if (user == null)
			return null;
		try {
			ObjectMapper logmapper = new ObjectMapper();
			String data = logmapper.writeValueAsString(new String[] { "0" });

			try {
				datastoreService.logUserAction(user.getId(),
						UserActionLog.ActionCode.GET_CONNECTION, data);
			} catch (Exception e) {
				logger.debug("Failed to log action", e);
			}

			final List<UserInfo> followers = datastoreService.getFollowers(user
					.getId());
			final List<UserInfo> followings = datastoreService
					.getFollowingUsers(user.getId());
			return new HashMap<String, List<UserInfo>>() {
				{
					put("followers", followers);
					put("followings", followings);
				}
			};
		} catch (Exception e) {
			logger.debug("", e);
			return null;
		}
	}

	@RequestMapping(value = "/followers", method = RequestMethod.GET)
	public @ResponseBody List<UserInfo> findFollowers(ModelMap model, Principal principal)
			throws Exception {
		User user = (User) ((Authentication) principal).getPrincipal();
		if (user == null)
			return null;
		try {
			ObjectMapper logmapper = new ObjectMapper();
			String data = logmapper.writeValueAsString(new String[] { "0" });
			datastoreService.logUserAction(user.getId(),
					UserActionLog.ActionCode.FIND_FOLLOWERS, data);

			return datastoreService.getFollowers(user.getId());
		} catch (Exception e) {
			logger.debug("", e);
			return null;
		}
	}

	@RequestMapping(value = "/followings", method = RequestMethod.GET)
	public @ResponseBody List<UserInfo> findFollowings(ModelMap model, Principal principal)
			throws Exception {
		User user = (User) ((Authentication) principal).getPrincipal();
		if (user == null)
			return null;
		try {
			ObjectMapper logmapper = new ObjectMapper();
			String data = logmapper.writeValueAsString(new String[] { "0" });
			datastoreService.logUserAction(user.getId(),
					UserActionLog.ActionCode.FIND_FOLLOWINGS, data);

			return datastoreService.getFollowingUsers(user.getId());
		} catch (Exception e) {
			logger.debug("", e);
			return null;
		}
	}

	@RequestMapping(value = "/find/{query}", method = RequestMethod.GET)
	public @ResponseBody List<UserInfo> findUser(@PathVariable String query, ModelMap model,
			Principal principal) throws Exception {
		User user = (User) ((Authentication) principal).getPrincipal();
		if (user == null)
			return null;
		try {
			ObjectMapper logmapper = new ObjectMapper();
			String data = logmapper.writeValueAsString(new String[] { "1",
					query });
			datastoreService.logUserAction(user.getId(),
					UserActionLog.ActionCode.FIND_USER, data);

			return datastoreService.findFriends(user, query);
		} catch (Exception e) {
			logger.debug("", e);
			return null;
		}
	}

	@RequestMapping(value = "/friendship", method = RequestMethod.POST)
	public @ResponseBody int follow(@RequestParam(value = "target", required = true) int target,
			@RequestParam(value = "follow", required = true) boolean follow,
			ModelMap model, Principal principal) throws Exception {
		User user = (User) ((Authentication) principal).getPrincipal();
		if (user == null)
			return -1;
		try {
			ObjectMapper logmapper = new ObjectMapper();
			String data = logmapper.writeValueAsString(new String[] { "2",
					Integer.toString(target), Boolean.toString(follow) });
			datastoreService.logUserAction(user.getId(),
					UserActionLog.ActionCode.FOLLOW, data);
		} catch (Exception e) {
			logger.debug("Failed to log action", e);
		}

		if (target < 0)
			return -1;
		User tarUser = datastoreService.getUser(target);
		if (tarUser == null)
			return -1;
		try {
			if (follow) {
				datastoreService.createFollowing(user.getId(), target);
			} else {
				datastoreService.removeFollowing(user.getId(), target);
			}
			return 1;
		} catch (Exception e) {
			return -1;
		}
	}

}
