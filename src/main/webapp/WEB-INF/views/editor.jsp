<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
  com.bouncingdata.Main.loadCss(ctx + "/resources/css/bouncingdata/editor.css", "editor");

  var anls = {
    guid: '${anls.guid}',
    code: '${anlsCode}',
    name: '${anls.name}'
  }
  
  var feature = '${feature}';
  var lastOutput = '${lastOutput}';
  // supports async. load js but we really should pre-load workbench.js from the layout.jsp
  if (!com.bouncingdata.Editor) {
    $.getScript(ctx + "/resources/js/bouncingdata/editor.js", function() {
      console.debug("editor.js async. loaded!");
      com.bouncingdata.Editor.init(anls, feature);
    });  
  } else {
    com.bouncingdata.Editor.init(anls, feature);
  }
</script>

<div id="main-content" class="editor-container">
  <div class="top-bar">
    <div class="left-buttons">
      <button id="editor-clone">Clone</button>
      <a href="<c:url value="/anls/${anls.guid}" />" class="editor-nav" id="editor-cancel">Cancel</a>
    </div>
    <div class="editor-nav-panel">
      <button class="editor-nav" id="editor-execute">Execute</button>
    </div>
    <div class="editor-progress progress">
      <div class="progress-step editor-step progress-current">Code</div>
      <div class="progress-step editor-step">Size</div>
      <div class="progress-step editor-step">Describe</div>
    </div>
  </div>
  <div class="clear-bar"></div>
  <div class="editor-container center-content-container">
    <div class="info-area">
      <h3 class="title">${anls.name }</h3>
      <div class="author">
        <img src="<c:url value="/resources/images/no-avatar.png" />" class="author-avatar" />
        <p class="author-name"><strong>${anls.user.username }</strong></p>
      </div>
      <div class="editor-status">
        <div class="running-status">
          <img class="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loader.gif" />" style="opacity: 0;"  />
          <span class="status-message" style="color: Green; font-style: italic;"></span>
        </div>
        <div class="saving-status">Automatically saved at 4:25 pm</div>
      </div>
    </div>
    <div class="clear"></div>
    <div class="editor-area">
      <div class="code-editor-wrapper">
        <div>
          <div id="code-editor" class="code-editor"></div>
          <div id="code-hidden"><pre>${anlsCode }</pre></div>
        </div>
      </div>
      <div class="execution-logs-wrapper">
        <div id="execution-logs" class="execution-logs">
          <div class="console-actions">
            <input class="clear-console" type="button" value="Clear console" />
          </div>
          <div class="console prompt" style="display: block;"></div>
        </div>
      </div>
    </div>
  </div>
</div>