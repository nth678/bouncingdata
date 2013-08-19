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
      		<font style="font-size: 12px;">Hi <b>${_username}</b>, a new password is sent to your email. Check your mail, please !</font>
      	</c:when>
      	<c:otherwise>
			<font style="font-size: 16px;font-weight: bold;">Active code is expired !</font>      	
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