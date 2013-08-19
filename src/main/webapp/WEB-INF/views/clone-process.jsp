<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="content-type" content="text/html;charset=utf-8" />  
  <title>Cloning analysis..</title>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-1.7.2.min.js" />"></script>
</head>
<body>
Cloning analysis ...
<script>
var ctx = '${pageContext.request.contextPath}';
var guid = '${guid}';
$.ajax({
  url: ctx + '/anls/clone/' + guid,
  success: function(res) {
    if (res == "error") {
      alert("Error occurred when trying clone this analysis. Please try again.")
      document.write("Error occurred!");
      return;
    }
    
    window.location.href = ctx + '/editor/anls/' + res + '/size';
  },
  error: function(res) {
    alert("Error occurred when trying clone this analysis. Please try again.")
    document.write("Error occurred!");
    console.debug(res);
  }
});
</script>
</body>
</html>