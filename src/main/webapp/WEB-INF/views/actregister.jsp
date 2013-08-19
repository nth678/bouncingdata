<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Bouncing Data</title>
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bouncingdata/login.css" />" />
  <link type="text/css" href="<c:url value="/resources/css/jquery-ui/smoothness/jquery-ui-1.8.20.custom.css" />" rel="stylesheet" />
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-1.7.2.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-ui-1.8.20.custom.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/bouncingdata/login.js" />"></script>
</head>
<body>
  <div class="page" id="page">
    <div class="top-content">
      <div class="logo"></div>
      <div class="title">
      <c:choose>
      	<c:when test="${not empty success}">
      		<font style="font-size: 12px;">Hi <b>${_username}</b>, Your account is activated !</font>
      	</c:when>
      	<c:otherwise>
      		<script>
			  $(function() {
				  $("#activelink").click(function() {
					  debugger;  
					  $( "#dialog-message" ).dialog( "open" );
					  $( "#dv_sucess").hide();
					  $( "#dv_failure").hide();
					  $( "#progress-stt").show();
					  
					  $.ajax({
							type : "post",
							url : '<c:url value="/auth/resendactmail"/>' ,
							data : {
								"user-email": '${_tempUsr.email}',
								"user-name" : '${_tempUsr.username}'
							},
							success : function(res) {
								$( "#progress-stt").hide();
								
								if (res['code'] < 0) {
									$( "#dv_failure").show();
							        return;
								}else{
									$("#email-text").html(res['message']);
									$( "#dv_sucess").show();
								}
							}
						});
				  });
				  
				    $("#dialog-message").dialog({
					   	  autoOpen: false,
					      modal: true,
					      buttons: {
					        Ok: function() {
				        	  $("#email-text").html("");
				        	  $( "#dv_sucess").hide();
							  $( "#dv_failure").hide();
							  $( "#progress-stt").show();
					          $( this ).dialog( "close" );
					        }
					      }
					    });
			  });
			  </script>
			<font style="font-size: 12px;">Hi <b>${_tempUsr.username}</b>, your account has not been activated. Please check your e-mail and click the activation link inside. <br/><br/>
		    	Click <a id="activelink" href="#">here</a> to resend active mail.
		    </font>
		    
		    <div id="dialog-message" title="Resent mail">
			  
			  	<div id="progress-stt" name="progress-stt" style="display: none;">
			  		<img src="<c:url value="/resources/images/ajax-loader.gif"/>" height="25" width="25"> Sending ...
			  	</div>
			  
			    <div id="dv_sucess" name="dv_sucess" style="display:none;">
				  <p>
				    <span class="ui-icon ui-icon-circle-check" style="float: left; margin: 0 7px 50px 0;"></span>
				    A active mail is sent successfully to your email. 
				  </p>
				  <p>
				    Check email <u><b id="email-text">...</b></u> to active your account.
				  </p>
			    </div>
			    
			    <div id="dv_failure" name="dv_failure" style="display:none;">
				  <p>
				    <span class="ui-icon ui-icon-circle-check" style="float: left; margin: 0 7px 50px 0;"></span>
				    The active mail can't be sent to your email. 
				  </p>
			    </div>
			</div>
      	</c:otherwise>
      </c:choose>
      </div>
      <div class="top-horizontal-rule"></div>
    </div>
    
    <div class="footer">
      <div class="footer-content">
        <a href="#" class="footer-link">Term of Service</a>
        <a href="#" class="footer-link">Privacy Policy</a>
        <a href="#" class="footer-link">Help</a>
        <div class="footer-right">
          <span>Copyright &copy;2012 bouncingdata.com. All rights reserved.</span>
        </div>
      </div>
    </div>
  </div>  
</body>
</html>