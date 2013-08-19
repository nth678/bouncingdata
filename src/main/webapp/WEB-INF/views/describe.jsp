<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
  com.bouncingdata.Main.loadCss(ctx + "/resources/css/bouncingdata/editor.css", "editor");

  var anls = {
    guid: '${anls.guid}',
    name: '${anls.name}',
    code: '${anlsCode}'
  };
  
  // supports async. load js but we really should pre-load workbench.js from the layout.jsp
  if (!com.bouncingdata.Editor) {
    $.getScript(ctx + "/resources/js/bouncingdata/editor.js", function() {
      console.debug("editor.js async. loaded!");
      com.bouncingdata.Editor.initDescribe(anls);
    });  
  } else {
    com.bouncingdata.Editor.initDescribe(anls);
  }
</script>

<div id="main-content" class="editor-container">  
  <div class="top-bar">
    <div class="left-buttons">
      <button class="editor-button" id="editor-clone">Clone</button>
      <a href="<c:url value="/anls/${anls.guid}" />" class="editor-nav" id="editor-cancel">Cancel</a>
    </div>
    <div class="editor-nav-panel">
      <a class="editor-nav" id="describe-back" href="<c:url value="/editor/anls/${anls.guid }/size" />">Back</a>
      <a class="editor-nav" id="describe-publish" href="javascript:void(0);">Publish</a>
      <div style="text-align: right;">
        <input type="checkbox" id="describe-ispublic" checked="checked" /> &nbsp;<label for="describe-ispublic">Public</label>
      </div>
    </div>
    <div class="editor-progress progress">
      <div class="progress-step editor-step">Code</div>
      <div class="progress-step editor-step">Size</div>
      <div class="progress-step editor-step progress-current">Describe</div>
    </div>
    <div class="clear"></div>
  </div>
  <div class="clear-bar"></div>
  
  <div class="editor-describe-container center-content-container">
    <div class="info-area">
      <h3 class="title">${anls.name }</h3>
      <div class="author">
        <img src="<c:url value="/resources/images/no-avatar.png" />" class="author-avatar" />
        <p class="author-name"><strong>${anls.user.username }</strong></p>
      </div>
      <div class="editor-status">
        <span>Last updated: 4 minutes ago</span>
        <div class="saving-status">
          <img class="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loader.gif" />" style="opacity: 0;"  />
          <span class="status-message" style="color: Green; font-style: italic;"></span>
        </div>
      </div>
    </div>
    <div class="clear"></div>
    <div class="detail-area">
      <form id="detail-form" class="detail-form" action="">
        <fieldset>
          <label for="name"><strong>Analysis name</strong></label>
          <input type="text" name="name" id="name" value="${anls.name }"><br>
          <label for="description"><strong>Description</strong></label>
          <textarea name="description" id="description">${anls.description }</textarea><br>
          <label for="tag-set"><strong>Tags: </strong></label>
          <div class="tag-set" id="tag-set">
            <div class="tag-list">
              <c:if test="${not empty anls.tags }">
                <c:forEach items="${anls.tags }" var="tag">
                  <div class="tag-element-outer">
                    <a class="tag-element" href="<c:url value="/tag/${tag.tag }" />" target="_blank">${tag.tag }</a>
                    <span class="tag-remove" title="Remove tag from this analysis">x</span>
                  </div>
                </c:forEach>  
              </c:if>
            </div>&nbsp;             
            <div class="add-tag-popup">            
          <input type="text" id="add-tag-input" style="width:69%"/>
          <input type="button" value="Add" id="add-tag-button" />
        </div>
           
          </div>
          <br/>
        </fieldset>
      </form>
    </div>
  </div>
</div>