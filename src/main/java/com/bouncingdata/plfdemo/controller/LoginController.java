package com.bouncingdata.plfdemo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bouncingdata.plfdemo.datastore.pojo.dto.ActionResult;
import com.bouncingdata.plfdemo.datastore.pojo.dto.RegisterResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.service.DatastoreService;
import com.bouncingdata.plfdemo.util.Utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Controller
public class LoginController implements AuthenticationFailureHandler{
  
  private Logger logger = LoggerFactory.getLogger(LoginController.class);
  
  @Autowired
  private DatastoreService datastoreService;
        
  @RequestMapping(value={"/new", "/test", "/profile"}, method = RequestMethod.GET)
  public String newVersion(ModelMap model, Principal principal) {
    return "test";
  }
  
  @Override
  public void onAuthenticationFailure(HttpServletRequest request,
           HttpServletResponse response, AuthenticationException ae)
           throws IOException, ServletException {
	    
	  	HttpSession session = request.getSession();   
	  	UsernamePasswordAuthenticationToken user =(UsernamePasswordAuthenticationToken)ae.getAuthentication();

	  	session.removeAttribute("_tempUsr");
	  	// check active account 
	  	User is_active_user = datastoreService.findUserByUsername(user.getName());
	  	if(is_active_user!=null && !is_active_user.isEnabled()){
	  		session.setAttribute("_tempUsr", is_active_user);
	  		
	  		String url = request.getContextPath() + "/actregister";
	  		// return fail login page
	        response.sendRedirect(url);
	        return;
	  	}
	  	
	  	// init list username login fail
	  	if(session.getAttribute("count_login_ss") == null){
	  		List<String> lst = new ArrayList<String>();
	  		lst.add(user.getName());
	  		session.setAttribute("count_login_ss", lst);
	  	}
	  	else{
	  		
	  		// get list string username login fail in client computer
	  		List<String> lst = (List<String>) session.getAttribute("count_login_ss");
	  		int count = 0;
	  		String name_in_loop = user.getName();
	  		
	  		// check count name 
	  		for(int i = 0; i < lst.size(); i++){
	  			if(name_in_loop.equals(lst.get(i)))
	  				count++;
	  		}
	  		
	  		// if count username >= 2 then send warning mail to user 
	  		if(count>=2){

	  			// check email exist if it exists, send mail to notification
	  			User _user = datastoreService.findUserByUsername(name_in_loop);
	  			
	  		    if (_user != null){
	  		    	Utils.sendMailLoginFail(_user.getUsername(), _user.getEmail());
	  		    	
	  		    	// remove username is sent mail in list
	  		    	ListIterator<String> it=lst.listIterator();  
	  		    	while(it.hasNext()){
	  		    		String _itName = (String) it.next();
	  		    		if(_user.getUsername().equals(_itName)){
	  		    			it.remove();
	  		    		}
	  		    	}
	  		    }
	  		    
	  		// if count user < 2 then add fail username in list     
	  		}else{
	  			lst.add(name_in_loop);
	  		}
	  		
	  		session.setAttribute("count_login_ss", lst);
	  		
	  	}
	  	
	  	// return fail login page
        response.sendRedirect(request.getContextPath() + "/auth/failed");
  }
  
  

  @RequestMapping(value="/actregister", method = RequestMethod.GET)
  public String openActiveRegisterPage(HttpSession session) {
	  
	  if(session.getAttribute("_tempUsr")==null)
		  return "redirect:/auth/login";
	  
	  return "actregister";
  }
  
  @RequestMapping(value="/create", method = RequestMethod.GET)
  public String openCreate() {
    return "create";
  }
  
  
  @RequestMapping(value="/learn", method = RequestMethod.GET)
  public String openLearn() {
    return "learn";
  }
  
  @RequestMapping(value="/help/python", method = RequestMethod.GET)
  public String openPyHelp(ModelMap model) {
	model.addAttribute("mode", "help-python");
    return "help-python";
  }
  
  @RequestMapping(value="/help/r", method = RequestMethod.GET)
  public String openRHelp(ModelMap model) {
	  model.addAttribute("mode", "help-r");
	  return "help-r";
  }
    
  @RequestMapping(value="/auth", method=RequestMethod.GET)
  public String gologin(ModelMap model) {
    model.addAttribute("mode", "login");
    return "redirect:/auth/login";
  }
  
  @RequestMapping(value="/auth/login", method=RequestMethod.GET)
  public String login(ModelMap model) {
    model.addAttribute("mode", "login");

    return "login";
  }
  
  @RequestMapping(value="/auth/failed", method=RequestMethod.GET)
  public String failed(ModelMap model, HttpServletRequest request,HttpSession session) {
	model.addAttribute("mode", "login");
    model.addAttribute("error", "true");
    return "login";
  }
  
  @RequestMapping(value="/auth/logout", method = RequestMethod.GET)
  public String logout(ModelMap model) {
    model.addAttribute("mode", "login");
    logger.debug(Utils.Log());
    return "login";
  }
  
  @RequestMapping(value="/auth/register", method = RequestMethod.GET)
  public String getRegisterPage(ModelMap model) {
    model.addAttribute("mode", "register");
    model.addAttribute("regResult", null);
    return "login";
  }
  
  @RequestMapping(value="/auth/register", method = RequestMethod.POST)
  public String register(@RequestParam(value="username", required=true) String username, 
      @RequestParam(value="password", required=true) String password, 
      @RequestParam(value="email", required=true) String email, 
      @RequestParam(value="firstName", required=false) String firstName, 
      @RequestParam(value="lastName", required=false) String lastName, 
      ModelMap model,
      HttpServletRequest request) {
    
    RegisterResult result = new RegisterResult();
    result.setUsername(username);
    result.setEmail(email);
    model.addAttribute("mode", "register");
    
    // validate inputs
    boolean isValid = true;
    StringBuilder errMsg = new StringBuilder();
    if (username == null || username.length() < 4) {
      errMsg.append("Username must be at least 4 characters.");
      isValid = false;
    }
    
    if (password == null || password.length() < 4) {
      if (errMsg.length() > 0) errMsg.append("<br/>");
      errMsg.append("Password must be at least 4 characters.");
      isValid = false;
    }
    
    if (email == null || !Utils.validate(email)) {
      if (errMsg.length() > 0) errMsg.append("<br/>");
      errMsg.append("Your email address is invalid.");
      logger.debug("Debug" + Utils.Log());
      isValid = false;
    }
    
    if (datastoreService.findUserByUsername(username) != null) {
      if (errMsg.length() > 0) errMsg.append("<br/>");
      errMsg.append("Username already exists.");
      isValid = false;
    }
    
    if (datastoreService.findUserByEmail(email) != null) {
      if (errMsg.length() > 0) errMsg.append("<br/>");
      errMsg.append("Email address already exists.");
      isValid = false;
    }
    
    if (!isValid) {
      result.setMessage(errMsg.toString());
      result.setStatusCode(-1);     
      model.addAttribute("regResult", result);
      return "login";
    }
    
    // business logic to create account
    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    
    try {
    	 /*ObjectMapper logmapper = new ObjectMapper();
         String data = logmapper.writeValueAsString(new String[] {"5", username, email,password,firstName,lastName});		   	 
         datastoreService.logUserAction(user.getId(),UserActionLog.ActionCode.REGISTER,data);      */
       
      datastoreService.createUser(user);
      result.setStatusCode(0);
      result.setMessage("- Successfully create user " + username);
      result.setMessage("- Successfully create user " + username + ".<br/>- Information activate your account will be sent on e-mail address: " + email);
      
      //Set active link 
      String url = request.getRequestURL().toString() + "/activelink/" + username;

      // Send register mail 
      Utils.sendMailActiveUser(username, email, url);
      
      model.addAttribute("regResult", result);
      logger.debug("Debug" + Utils.Log());
    } catch (Exception e) {
      if (logger.isDebugEnabled()) logger.debug("Failed to create new user " + username, e);
      result.setStatusCode(-2);
      result.setMessage("Failed to register new user. Internal server error.");
      model.addAttribute("regResult", result);
    }
    
    return "login";
  }
  
  @RequestMapping(value="/auth/resendactmail", method = RequestMethod.POST)
  public @ResponseBody ActionResult resendActiveMail(@RequestParam(value="user-email", required=true) String email,
												      @RequestParam(value="user-name", required=true) String name,												      ModelMap model,
												      HttpServletRequest request){
	  String url = request.getRequestURL().toString();
	  url = url.replace("/resendactmail", "/register/activelink/" + name);
	  boolean isSent = Utils.sendMailActiveUser(name, email, url);
	  
	  if(isSent)	  
		  return new ActionResult(0, email);
	  logger.debug("Debug" + Utils.Log());
	  return new ActionResult(-1, "Sent mail failure!");
  }
  
  @RequestMapping(value="/auth/register/activelink/{name}", method=RequestMethod.GET)
  public String activeAccount(@PathVariable String name,
			   ModelMap model, 
			   HttpServletRequest request,
			   HttpSession session) {
	  
	User user = datastoreService.findUserByUsername(name);
		
    // validate username 
    if (user == null) 
    	return "login";
	    
    if(user.isEnabled())
    	return "login";
	
    session.removeAttribute("_tempUsr");
	datastoreService.changeActiveRegisterStatus(user.getId());
	model.addAttribute("success", "OK");
	model.addAttribute("_username", name);
	
	return "actregister";
  }
  
  @RequestMapping(value="/auth/resetpasswd/{name}/{activecode}", method=RequestMethod.GET)
  public String resetPassword(@PathVariable String name,
		  					   @PathVariable String activecode, 
		  					   ModelMap model, 
		  					   HttpServletRequest request) {
	model.addAttribute("mode", "resetPassword");
	
	User user = datastoreService.findUserByUsername(name);
	
    // validate username 
    if (user == null) 
    	return "cfpassword";
    
    if(user.getActiveCode()==null || !user.getActiveCode().equals(activecode))
    	return "cfpassword";
    
    if(user.getExpiryDate()==null || !Utils.compareExpiredDate(user.getExpiryDate()))
    	return "cfpassword";
    
    // find email to determine user
    String receiver = user.getEmail(); 
    
    // Set active code info
    String newpass = Utils.RandomString();
    
    datastoreService.resetPassword(user.getId(), newpass);
    
    boolean result = Utils.sendMailPassword(name, receiver, newpass);
    
    model.addAttribute("success", "OK");
    model.addAttribute("_username", name);
    return "cfpassword";
  }
  
  @RequestMapping(value="/auth/resetpasswd", method = RequestMethod.POST)
  public @ResponseBody ActionResult cfResetPassword(@RequestParam(value="user-email", required=true) String email,
		  										     @RequestParam(value="user-name", required=true) String name,
		  										     ModelMap model,
		  										     HttpServletRequest request) {
    
	model.addAttribute("mode", "cfresetpasswd");
    
	User user = datastoreService.findUserByUsername(name);
	
    // validate username and email 
    if (user == null) 
    	return new ActionResult(-1, "User isn't found !");
    
    else if (!user.getEmail().equals(email)) 
    	return new ActionResult(-1, "User isn't found !");
    
    // find email to determine user
    String receiver = user.getEmail();
    
    // Set active code info
    String activeCode = Utils.RandomString();
    
    // Set expired Date
    String expiredDate = Utils.getTomorowDateTime();
    
    //Set link reset password
    String url = request.getRequestURL().toString();
    String lnkReset = url + "/" + name + "/" + activeCode;
    
    datastoreService.addSttResetPassword(user.getId(), activeCode, expiredDate);
    
    // send new password to email address
    boolean result = Utils.sendMailConfirmResetPassword(name, receiver, lnkReset, expiredDate);
    
    // check mail is sent successfully 
    if(!result)
    	return new ActionResult(-1, "It has an error in sending mail. Please contact with administrator!");
    
    return new ActionResult(0, receiver);
  }
  
  @RequestMapping(value="/auth/changepasswd", method = RequestMethod.POST)
  public @ResponseBody ActionResult changePassword(@RequestParam(value="npassword", required=true) String npassword,
		  					   						ModelMap model, Principal principal) {
    
	model.addAttribute("mode", "changepasswd");
    
	User user = (User) ((Authentication) principal).getPrincipal();
	
    if (user == null) 
    	return new ActionResult(-1, "User isn't found !");
    
    datastoreService.resetPassword(user.getId(), npassword);
    
    return new ActionResult(0, "OK");
  }
}
