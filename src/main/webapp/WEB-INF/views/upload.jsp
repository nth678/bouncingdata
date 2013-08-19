<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
  com.bouncingdata.Main.loadCss(ctx + "/resources/css/bouncingdata/upload.css", "upload");

  if (!com.bouncingdata.Upload) {
    $.getScript(ctx + "/resources/js/bouncingdata/upload.js", function() {
      console.debug("upload.js async. loaded!");
      com.bouncingdata.Upload.init();
    });  
  } else {
    com.bouncingdata.Upload.init();
  }
</script>

<script>
$(function() {
	$('#show-pn-uploaded').click(function() {
		$("#upload-submit").hide();
		$("#upload-next").show();
		$("#pn-file-upload").hide();
		$("#pn-file-uploaded").show();
	});
	
	$('#show-pn-upload').click(function() {
		$("#upload-next").hide();
		$("#upload-submit").show();
		$("#pn-file-upload").show();
		$("#pn-file-uploaded").hide();
	});
});
</script>

<div id="main-content" class="upload-container">
  <div class="top-bar">
    <div class="left-buttons">
      <button class="close-button" id="upload-close">Close</button>
    </div>
    <div class="upload-nav-panel">
      	<a class="upload-nav" id="upload-next" href="<c:url value="/dataset/fschema"/>" ${ not empty varUp ? "style='display:block;'" : "style='display:none;'"}> Next </a>
      	<a class="upload-nav" id="upload-submit" href="javascript:void(0);" ${ not empty varUp ? "style='display:none;'" : "style='display:black;'"}>Submit</a>
    </div>
    <div class="progress upload-progress">
      <div class="progress-step upload-step upload-file progress-current">File</div>
      <div class="progress-step upload-step upload-schema">Schema & Description</div>
    </div>
  </div>
  <div class="clear-bar"></div>
  <div class="upload-container center-content-container">
    <c:if test="${not empty errorMsg}">
      <div class="error-block" id="error-msg">
        <strong style="color: red; ">${errorMsg}</strong>
      </div>
    </c:if>
    <div class="upload-form-wrapper">
      <form id="upload-form" method="post" action="<c:url value="/dataset/upload/schema" />" enctype="multipart/form-data">
        <div>
          <img alt="Uploading" src="<c:url value="/resources/images/loader32.gif" />" class="upload-in-progress" style="display: none;" />&nbsp;
          <span class="upload-status"></span>
        </div>
        <div class="file-section panel-wrapper">
          <div class="guideline-wrapper">
            <div class="guideline">Required</div>
          </div>
          <h3>1. File Location</h3>
          <span>We support the following types: csv, xls, xlsx, txt, rdata</span>
          <div class="file-inner">
            <label for="file-url">Web Address</label>
            <input type="text" name="fileUrl" id="file-url" />
            <div style="font-size: 15px; margin: 5px 0; font-weight: bold;">Or</div>         
           		<div id="pn-file-upload" ${ empty varUp ? "style='display:block;'" : "style='display:none;'"}>
		           	<label for="file">Local File</label>
		           	<input type="file" id="file" name="file" style="margin-right: 5px;"/>
					<c:if test="${not empty varUp.filename}">           	
						<a id="show-pn-uploaded" href="javascript:void(0);" style="font-size: 10px;color: blue;text-decoration:underline;">Cancel</a>
					</c:if>
           		</div>	
	            <div id="pn-file-uploaded" ${ not empty varUp ? "style='display:block;'" : "style='display:none;'"}>
	            	<label for="file">Local File</label>
					<label style="font-size: 14px;width: 50%;">${varUp.filename}</label>
					<a id="show-pn-upload" href="javascript:void(0);" style="font-size: 10px;color: blue;text-decoration:underline;">delete</a>
	            </div>
          </div>
        </div>
        <div class="options-section panel-wrapper">
          <div class="guideline-wrapper">
            <div class="guideline">Optional</div>
          </div>
          <h3>2. Loading Options</h3>
          <div class="options-inner">
            <label for="first-row-as-header">First row is header</label>
            <input type="checkbox" id="first-row-as-header" ${ (empty varUp || varUp.firstRowAsHeader eq 'on') ? "checked='checked'" : ''} name="firstRowAsHeader" /><br/><br/>
            <label for="delimiter">Delimiter</label>
            <select id="delimiter" name="delimiter">
              <option value="comma" ${varUp.delimiter eq 'comma' ? 'selected' :''}>Comma</option>
              <option value="tab" ${varUp.delimiter eq 'tab' ? 'selected' :''}>Tab</option>
              <option value="period" ${varUp.delimiter eq 'period' ? 'selected' :''}>Period</option>
            </select>
          </div>
        </div>  
      </form>
    </div>
  </div>

</div>