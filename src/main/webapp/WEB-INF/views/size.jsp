<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
  com.bouncingdata.Main.loadCss(ctx + "/resources/css/bouncingdata/editor.css", "editor");

  var anls = {
    guid: '${anls.guid}',
    username: '${anls.user.username}',
    code: '${anlsCode}'
  };
  var dbDetail = $.parseJSON('${dashboardDetail}');
  
  // supports async. load js but we really should pre-load workbench.js from the layout.jsp
  if (!com.bouncingdata.Editor) {
    $.getScript(ctx + "/resources/js/bouncingdata/editor.js", function() {
      console.debug("editor.js async. loaded!");
      com.bouncingdata.Editor.initSize(anls, dbDetail);
    });  
  } else {
    com.bouncingdata.Editor.initSize(anls, dbDetail);
  }
</script>
<style>

.space-item {
	padding-left: 20px;
	padding-right: 20px;
}

</style>

<div id="main-content" class="editor-container">
  <div class="top-bar">
    <div class="left-buttons">
      <button class="editor-button" id="editor-clone">Clone</button>
      <a href="<c:url value="/anls/${anls.guid}" />" class="editor-nav" id="editor-cancel">Cancel</a>
    </div>
    <div class="editor-nav-panel">
      <c:choose>
        <c:when test="${not empty execId }">
          <a href="<c:url value="/editor/anls/${anls.guid }/edit?feature=edit&execid=${execId }" />" class="editor-nav" id="size-back">Back</a>
        </c:when>
        <c:otherwise>
          <a href="<c:url value="/editor/anls/${anls.guid }/edit?feature=edit" />" class="editor-nav" id="size-back">Back</a>
        </c:otherwise>
      </c:choose>
      <a href="<c:url value="/editor/anls/${anls.guid }/describe" />" class="editor-nav" id="size-next">Next</a>
    </div>
    <div class="editor-progress progress">
      <div class="progress-step editor-step editor-code-link">Code</div>
      <div class="progress-step editor-step editor-size-link progress-current">Size</div>
      <div class="progress-step editor-step editor-describe-link">Describe</div>
    </div>
  </div>
  <div class="clear-bar"></div>
  <div class="editor-size-container center-content-container">
    <div class="info-area">
      <h3 class="title">${anls.name }</h3>
      <div class="author">
        <img src="<c:url value="/resources/images/no-avatar.png" />" class="author-avatar" />
        <p class="author-name"><strong>${anls.user.username }</strong></p>
      </div>
      <div class="editor-status">
        <span>Last updated: 4 minutes ago</span>
      </div>
    </div>
    <div class="clear"></div>
    <div class="editor-size-area">
      <div class="size-tabs ui-tabs" id="size-tabs">
        <ul>
          <li><a href="#size-viz"  style="padding-left: 20px;padding-right: 20px;">Viz</a></li>
          <li><a href="#size-data" style="padding-left: 20px;padding-right: 20px;">Data</a></li>
          <li><a href="#size-code" style="padding-left: 20px;padding-right: 20px;">Code</a></li>
        </ul>
        <div class="clear"></div>
        <div class="size-viz ui-tabs-hide" id="size-viz">
          <div class="viz-wrapper">
            <div class="dashboard-ruler">
              <div class="dashboard-ruler-left ruler"></div>
              <div class="dashboard-ruler-top ruler"></div>
              <div class="dashboard-ruler-right ruler"></div>
              <div class="dashboard-ruler-bottom ruler"></div>
              <div class="snap-line-left snap-line"></div>
              <div class="snap-line-top snap-line"></div>
              <div class="snap-line-right snap-line"></div>
              <div class="snap-line-bottom snap-line"></div>
            </div>
            <div class="viz-dashboard" id="viz-dashboard">
              <div id="dashboard-wrapper" class="dashboard-wrapper"></div>
            </div>
          </div>
        </div>
        <div class="size-code ui-tabs-hide" id="size-code">
          <div class="code-block">
            <pre class="brush: py"></pre>
          </div>
        </div>
        <div class="size-data ui-tabs-hide" id="size-data">
          <c:if test="${empty datasetList and empty attachments }">
            <span>No data related to this analysis.</span>
          </c:if>
          <c:if test="${not empty datasetList}">
            <c:forEach items="${datasetList }" var="entry">
              <div class="anls-dataset" style="margin: 1em 0 2.5em 0;"
                dsguid="${entry.key }">
                <div class="dataset-item-title">
                  <strong> <a
                    href="<c:url value="/dataset/view/${entry.key }" />">${entry.value
                      }</a> &nbsp;
                  </strong> <a
                    href="<c:url value="/dataset/dl/csv/${entry.key }" />"
                    style="color: blue; text-decoration: none;">Download
                    CSV</a>&nbsp;&nbsp; <a
                    href="<c:url value="/dataset/dl/json/${entry.key }" />"
                    style="color: blue; text-decoration: none;">Download
                    JSON</a>
                </div>
                <table dsguid="${entry.key }" class="dataset-table"></table>
              </div>
            </c:forEach>
          </c:if>
          <c:if test="${not empty attachments }">
            <script>
            	var $dataContainer = $('#size-data');
            </script>
            <c:forEach items="${attachments }" var="attachment">
              <script>
                  $(function() {
                    var $attachment = $('<div class="anls-attachment" style="margin: 1em 0 2.5em 0;"><div class="dataset-item-title"><strong>'
                    + '<a href="">${attachment.name}</a></strong>&nbsp;<a href="<c:url value="/dataset/att/csv/${anls.guid}/${attachment.name}" />" style="color: blue; text-decoration: none;">Download CSV</a>'
                    + '&nbsp;&nbsp;<a href="<c:url value="/dataset/att/json/${anls.guid}/${attachment.name}" />" style="color: blue; text-decoration: none;">Download JSON</a></div>' 
                    + '<table class="attachment-table"></table></div>');
                    $attachment.appendTo($dataContainer);
                    var $table = $('table', $attachment);
                    var data = '${attachment.data}';
                    com.bouncingdata.Workbench.renderDatatable($.parseJSON(data), $table);
                  });
                </script>
            </c:forEach>
            <script>
            	
            </script>
          </c:if>

        </div>
      </div>
    </div>
  </div>
</div>