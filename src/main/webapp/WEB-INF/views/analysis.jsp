<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script>
  //com.bouncingdata.Main.loadCss(ctx + "/resources/css/bouncingdata/analysis.css", "analysis");
  var anls = {
    guid: '${anls.guid}',
    user: '${anls.user.username}',
    language: '${anls.language}',
    code: '${anlsCode}'
  };
  var dbDetail = $.parseJSON('${dashboardDetail}');
  if (!com.bouncingdata.Analysis) {
    $.getScript(ctx + "/resources/js/bouncingdata/analysis.js", function() {
      console.debug("analysis.js async. loaded!");
      com.bouncingdata.Analysis.init(anls, dbDetail);
    });
  } else {
    com.bouncingdata.Analysis.init(anls, dbDetail);
  }
</script>
<script>
$(function() {
	
	$( "#del-anls" ).click(function() {
		$("#item-del-name").html("'<b style=\"color: royalblue;\">${anls.name}</b>'");
        $( "#dialog-confirm-delete" ).dialog( "open" );
    });
	
	$( "#dialog-confirm-delete" ).dialog({
	  autoOpen: false,
	  resizable: false,
	  height:'auto',
	  minHeight: 140,
      modal: true,
      buttons: {
        "Delete": function() {
      	  $("#progress-del-img").show();
      	  var iguid = "${anls.guid}";
      	  var iname = "${anls.name}";
      	  
      	  //pvdels : page view delete stream 
      	  var url = '<c:url value="/anls/delanls"/>';
      	  debugger;
      	  
      	  //process delete here 
      	  $.ajax({
				type : "post",
				url :   url,
				data : {
					"iguid" : iguid,
					"iname" : iname
				},
				success : function(res) {
					$( "#dialog-confirm-delete" ).dialog( "close" );
					$("#progress-del-img").hide();
					
					if (res['code'] < 0) {
						window.alert("Failed to delete!");
				        return;
					}else{
						window.location='<c:url value="/stream/${pageId}/${fm}/${tp}"/>';
					}
				}
			});
        },
        Cancel: function() {
          $( this ).dialog( "close" );
        }
      }
    });
});
</script>
<div id="main-content" class="analysis-container">
  <div class="analysis-info right-content">
    <div class="anls-summary summary">
      <div class="author-summary">       
        <a class="author-avatar" href="javascript:void(0);"><img src="<c:url value="/resources/images/no-avatar.png" />" /></a>       
        <p class="author-name"><a href="javascript:void(0);"><strong>${anls.user.username }</strong></a></p>
        <p class="published-date">Published on ${anls.shortCreateAt }</p>
        <div class="clear"></div>
      </div>
      <p><strong>Reference: </strong></p>
      <p><strong>Referenced by: </strong></p>
      <p><strong>Dataset used: </strong></p>
      <p><strong>Last updated: </strong>${anls.shortLastUpdate }</p>
      <p><strong>View count: </strong>${pageView }</p>
    </div>
    
    <div class="tag-set">
     <p><strong>Tags:</strong></p>
      <div class="tag-list">
      <c:if test="${not empty anls.tags }">
        <c:forEach items="${anls.tags }" var="tag">
          <div class="tag-element-outer">
            <a class="tag-element" href="<c:url value="/tag/${tag.tag }" />">${tag.tag }</a>
            <c:if test="${isOwner }">
              <span class="tag-remove" title="Remove tag from this analysis">x</span>
            </c:if>
          </div>
        </c:forEach>  
      </c:if>
      
      </div>&nbsp;
      <c:if test="${isOwner }">      
          <div class="add-tag-popup">         
          <input type="text" id="add-tag-input" style="width:69%"/>
          <input type="button" value="Add" id="add-tag-button" />
        </div>
        
      </c:if>
    </div>
    <div class="anls-related-info related-info">
      <p><strong>Related:</strong></p>
      <div class="related-tabs ui-tabs" id="related-tabs">
        <ul>
          <li><a href="#related-dataset">Dataset</a></li>
          <li><a href="#related-author">Author</a></li>
          <li><a href="#related-voters">Voters</a></li>
        </ul>
        <div id="related-dataset" class="ui-tabs-hide"></div>
        <div id="related-author" class="ui-tabs-hide"></div>
        <div id="related-voters" class="ui-tabs-hide"></div>
      </div>
      
    </div>
  </div>
    
  <div class="analysis-main center-content">
    <div class="center-content-wrapper">
      <div class="anls-header header">

<c:choose>
<c:when test="${isOwner }">
	<h2 class="tc_pageheader editableName" id="detailsheader">${anls.name}</h2>
</c:when>
<c:otherwise>
    <div class="anls-title main-title"><h2>${anls.name}</h2></div>
</c:otherwise>
</c:choose>





        <div class="share-panel" style="float: right; width: 140px;">
          <!-- AddThis Button BEGIN -->
          <div class="addthis_toolbox addthis_default_style ">
          <a class="addthis_button_preferred_1"></a>
          <a class="addthis_button_preferred_2"></a>
          <a class="addthis_button_preferred_3"></a>
          <a class="addthis_button_preferred_4"></a>
          <a class="addthis_button_compact"></a>
          <a class="addthis_counter addthis_bubble_style"></a>
          </div>
          <script type="text/javascript">
            var addthis_config = addthis_config||{};
            addthis_config.data_track_addressbar = false;
            //var addthis_config = {"data_track_addressbar":true};
          </script>
          <script type="text/javascript" src="//s7.addthis.com/js/300/addthis_widget.js#pubid=ra-512cd44d6cd449d2"></script>
          <!-- AddThis Button END -->
        </div>
        <div class="anls-action-links action-links">
          <h3 class="score anls-score">${anls.score}</h3>&nbsp;
          <a href="javascript:void(0)" class="action anls-action anls-vote-up">Vote up</a>&nbsp;&nbsp;
          <a href="javascript:void(0)" class="action anls-action anls-vote-down">Vote down</a>&nbsp;&nbsp;
          <a href="<c:url value="/anls/clone/processing/${anls.guid }" />" target="_blank" class="action anls-action anls-clone">Clone</a>&nbsp;&nbsp;
          <c:if test="${anls.published}">
          	<a href="javascript:void(0)" class="action anls-action anls-embed-button" id="anls-embed-button">Embed</a>&nbsp;&nbsp;
          </c:if>
          <c:if test="${isOwner }">
            <a href="<c:url value="/editor/anls/${anls.guid }/size" />" class="action anls-action" title="Edit this analysis">Edit</a>&nbsp;&nbsp;
            <a id="del-anls" href="javascript:void(0)" title="Delete this analysis">Delete</a>&nbsp;&nbsp;
          </c:if>
          <a href="javascript:void(0);" class="action anls-action anls-download">Download</a>
          <div class="action-hidden-menu anls-action-hidden-menu" style="display: none;">
            <div>Download analysis</div>
            <div>Download dataset</div>
            <div>Download script</div>
          </div>
          
        </div>
        <div class="embedded-link" id="embedded-link" style="display: none;">
          <textarea id="embedded-link-text" spellcheck='false' style="float: left;height: 80px;"></textarea>
          <!-- a class="embedded-link-hidden" href="<c:url value="/anls/embed/" />${anls.guid}" style="display: none;">embedded</a-->
          <!-- <div class="embedded-options" style="float: left; margin-left: 15px;">     
            <div>  
              <strong>Include tab</strong><br />
              <input id="include-viz" type="checkbox" checked />Viz<br />
              <input id="include-code" type="checkbox"/>Code<br />
              <input id="include-data" type="checkbox"/>Data<br />
            </div><br />
            <div>
              <strong>Width</strong><br />
              <input id="embedded-width" type="text" value="800" />&nbsp; pixels<br/><br/>
              <strong>Height</strong><br />
              <input id="embedded-height" type="text" value="600" />&nbsp; pixels<br/>
              <input type="checkbox" id="embedded-border" />&nbsp; Border?
            </div>
          </div> -->
        </div>
        <div class="clear"></div>
      </div>
      <div class="anls-header-rule"></div>
      <div class="anls-content anls-tab-container ui-tabs" id="anls-content">
        <ul class="anls-tabs">
          <li class="anls-tab"><a href="#anls-dashboard" style="padding-left: 20px;padding-right: 20px;">Viz</a></li>
          <li class="anls-tab"><a href="#anls-code" style="padding-left: 18px;padding-right: 18px;">Code</a></li>
          <li class="anls-tab"><a href="#anls-data" style="padding-left: 20px;padding-right: 20px;">Data</a></li>
        </ul>
        <div class="clear"></div>
        <div class="anls-tabs-content-wrapper">
          <div class="anls-dashboard ui-tabs-hide" id="anls-dashboard"></div>
          <div class="anls-code ui-tabs-hide" id="anls-code">
            <div class="code-block" id="code-block">
              <pre class="brush: py"></pre>
            </div>
            <div class="raw-source-link" style="float: right;">
              <a target="_blank" href="view-source:<c:url value="/public/source/${anls.guid }" />" style=" color: #999; text-decoration: none; font-size: 11px;">Full view</a>
            </div>
            <div class="clear"></div>
          </div>
          <div class="anls-data ui-tabs-hide" id="anls-data">
            <c:if test="${empty datasetList and empty attachments }">
              <span>No dataset</span>
            </c:if>
            <c:if test="${not empty datasetList}">
              <c:forEach items="${datasetList }" var="entry">
                <div class="anls-dataset" style="margin: 1em 0 2.5em 0;" dsguid="${entry.key }">
                  <div class="dataset-item-title">
                    <strong>
                      <a href="<c:url value="/dataset/view/${entry.key }" />">${entry.value }</a>
                      &nbsp;
                    </strong>
                    <a href="<c:url value="/dataset/dl/csv/${entry.key }" />" style="color: blue; text-decoration: none;">Download CSV</a>&nbsp;&nbsp;
                    <a href="<c:url value="/dataset/dl/json/${entry.key }" />" style="color: blue; text-decoration: none;">Download JSON</a>
                  </div>
                  <table dsguid="${entry.key }" class="dataset-table"></table>
                </div>
              </c:forEach>
            </c:if>
            <c:if test="${not empty attachments }">
              <c:forEach items="${attachments }" var="attachment">
                <script>
                	$(function() {
                	  var $attachment = $('<div class="anls-attachment" style="margin: 1em 0 2.5em 0;"><div class="dataset-item-title"><strong>'
                	  + '<a href="">${attachment.name}</a></strong>&nbsp;<a href="<c:url value="/dataset/att/csv/${anls.guid}/${attachment.name}" />" style="color: blue; text-decoration: none;">Download CSV</a>'
                	  + '&nbsp;&nbsp;<a href="<c:url value="/dataset/att/json/${anls.guid}/${attachment.name}" />" style="color: blue; text-decoration: none;">Download JSON</a></div>' 
                	  + '<table class="attachment-table"></table></div>');
                	  $attachment.appendTo($('#anls-data'));
                	  var $table = $('table', $attachment);
                	  var data = ${attachment.data};
                	  com.bouncingdata.Utils.renderDatatable(data, $table, { "sScrollX": "735px", "sScrollY": "500px", "bPaginate": false, "bFilter": false });
                	});
              	</script>
              </c:forEach>  
            </c:if>
          </div>
        </div>
      </div>
      <div class="clear"></div>
      <div class="description anls-description" style="width: 100%;height: 100%;">
        <h3 style="margin: 0 0 10px; cursor: pointer">Description</h3>
        <c:choose>
          <c:when test="${empty anls.description }">No description</c:when>
          <c:otherwise><span>${anls.description }</span></c:otherwise>
        </c:choose>
      </div>
        
      <div class="comments-container">
        <h3 class="comments-title"><a href="javascript:void(0);" onclick="$('#comment-form').toggle('slow');">Comment</a></h3>
        <div class="comment-form" id="comment-form">
          <form>
            <fieldset>
            <p>
              <textarea rows="5" id="message" spellcheck='false'></textarea>
            </p>  
            <p>
              <input type="button" class="comment-submit" id="comment-submit" value="Post comment">
            </p>              
            </fieldset>
          </form>
        </div>
        <div class="clear"></div>
        <label id="comments"></label>
        <div class="comments">
          <h3 class="comments-count">Comments</h3>
          <ul id="comment-list" class="comment-list">            
          </ul>
        </div>
      </div>
    </div>
    
  </div>
  <!-- vinhpq : popup delete item -->
  <div id="dialog-confirm-delete" title="Delete item?" >
	  <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>The <font id="item-del-name"></font> item will be deleted and cannot be recovered. Are you sure?</p>
	  <div id="progress-del-img" style="display:none">
		<img src="<c:url value="/resources/images/wait.gif" />" style="vertical-align: middle;height: 18px;width: 18px; margin-right: 3px;"> waiting...
	  </div>
  </div>
</div>