<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<script>
	var dataset = {
      guid: '${dataset.guid}',
      user: '${dataset.user.username}',
      name: '${dataset.name}'
	};
	com.bouncingdata.Dataset.init(dataset);
</script>
<script>
$(function() {
	
	$( "#dels-ds" ).click(function() {
		$("#item-del-name").html("'<b style=\"color: royalblue;\">${dataset.name}</b>'");
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
      	  var iguid = "${dataset.guid}";
      	  var iname = "${dataset.name}";
      	  
      	  //pvdels : page view delete stream 
      	  var url = '<c:url value="/dataset/delds"/>';
      	  
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
<style>
	#q{
		width: 98%; padding: 0 0 0 5px;border: 0 none; height: 28px; outline: none; font-size: 12px;color: #9a9a9a;
	}
	#divQuery{
		border-color: rgb(77, 144, 254);float: left;width: 58%; border: 1px solid #DDD; height: 28px; padding: 0; display: inline-block; background-color: #FFFFFF;
	}
	#psearchleft {
		float: left;width: 93%;
	}
	#psearchright {
		float: left;width: 7%;height: 100%;
	}
	#imgsearchquery {
		margin-left: 7px;margin-top: 6px;cursor: pointer;width: 14px;height: 16px;
	}
	#embedded-link-text-ds{
		float: left;
		border: 1px solid #CCCCCC;
		resize: vertical;
		min-height: 80px;
		width: 520px;
		padding: 4px;
		font-family: monospace;
		color: #333;
		border-radius: 2px;
	}
</style>
<div id="main-content" class="datapage-container">
  <div class="data-info right-content"> 
    <div class="dataset-summary summary">
      <div class="author-summary">       
        <a class="author-avatar" href="javascript:void(0);"><img src="<c:url value="/resources/images/no-avatar.png" />" /></a>       
        <p class="author-name"><a href="javascript:void(0);"><strong>${dataset.user.username }</strong></a></p>
        <p class="published-date">Published on ${dataset.shortCreateAt }</p>
        <div class="clear"></div>
      </div>
      <p><strong>Reference: </strong><a href="#"></a></p>
      <p><strong>Dataset Collection: </strong><a href="#"></a></p>
      <p><strong>Source: </strong><a href="#"></a></p>
      <p><strong>License: </strong><a href="#">X</a></p>
      <p><strong>Last updated: </strong>${dataset.shortLastUpdate }</p>
      <p><strong>View count: </strong>${pageView }</p>
    </div>
    <div class="tag-set">
    <p><strong>Tags:</strong></p>
      <div class="tag-list">
      <c:if test="${not empty dataset.tags }">
        <c:forEach items="${dataset.tags }" var="tag">
          <div class="tag-element-outer">
            <a class="tag-element" href="<c:url value="/tag/${tag.tag }" />">${tag.tag }</a>
            <c:if test="${isOwner }">
              <span class="tag-remove" title="Remove tag from this dataset">x</span>
            </c:if>
          </div>
        </c:forEach>  
      </c:if>
      
      </div>&nbsp;
      <c:if test="${isOwner }">
          <div class="add-tag-popup" >        
          <input type="text" id="add-tag-input" style="width:69%"/>
          <input type="button" value="Add" id="add-tag-button" />
        </div>
      </c:if>
      
    </div>
    <div class="dataset-related-info related-info">
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
  <div class="center-content">
    <div class="center-content-wrapper">
      <div class="dataset-header header">


		<c:choose>
<c:when test="${isOwner }">
	<h2 class="tc_pageheader editableName" id="detailsheader">${dataset.name}</h2>
</c:when>
<c:otherwise>
    <div class="dataset-title main-title"><h2>${dataset.name}</h2></div>
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
        <div class="action-links dataset-action-links" style="margin-top: 4px;">
          <h3 class="dataset-score score">${dataset.score}</h3>&nbsp;
          <a href="javascript:void(0);" class="action vote-up dataset-vote-up">Vote up</a>&nbsp;&nbsp;
          <a href="javascript:void(0);" class="action vote-down dataset-vote-down">Vote down</a>&nbsp;&nbsp;
          <c:if test="${isOwner}">
          	<a id="dels-ds" href="javascript:void(0);" >Delete</a>&nbsp;&nbsp;
          </c:if>
          <%-- <c:if test="${${dataset.public}"> --%>
          	<a href="javascript:void(0)" class="action dataset-action dataset-embed-button" id="dataset-embed-button">Embed</a>&nbsp;&nbsp;
          <%-- </c:if> --%>
          <a href="<c:url value="/dataset/dl/csv/${dataset.guid}"/>" class="action dataset-action">Download CSV</a>&nbsp;&nbsp;
          <a href="<c:url value="/dataset/dl/json/${dataset.guid}"/>" class="action dataset-action">Download JSON</a>	
        </div>
        
        <div class="embedded-link" id="embedded-link-ds" style="display: none;">
          <textarea id="embedded-link-text-ds" spellcheck='false'></textarea>
        </div>
        <div class="clear"></div>
      </div>
      <div class="header-rule"></div>
      <div class="dataset-content data-tab-container ui-tabs" id="dataset-content">
        <form id="search-query" method="post" action="<c:url value="/dataset/squery"/>">
	        <div id="divQuery">
	          <c:choose>
	          	<c:when test="${not empty squery_datapage}">
		          <div id="psearchleft">
					<input type="text" id="q" name="q" value="${squery_datapage}" onblur="if(value=='') value = 'Search query ...'" onfocus="if(value=='Search query ...') value = ''" title="Using MySql syntax">
				  </div>
				  <div id="psearchright">
					<a href="<c:url value="/dataset/view/${dataset.guid}"/>">
						<img id="imgsearchquery" src="<c:url value="/resources/images/delete-icon.gif"/>" title="Delete query">
					</a>
				  </div>
				</c:when>
				<c:otherwise>
					<input type="text" id="q" name="q" value="Search query ..." onblur="if(value=='') value = 'Search query ...'" onfocus="if(value=='Search query ...') value = ''" title="Using MySql syntax">
				</c:otherwise>
			  </c:choose>
	          <input type="hidden" id="oq" name="oq" value="${dataset.guid}"/>
			</div>
        </form>	
        
        <ul>
          <li><a href="#data">View</a></li>
          <li><a href="#schema">Schema</a></li>
          <li><a href="#ref-doc">Reference Doc</a></li>
        </ul>
        <div class="clear"></div>
        <div id="data" class="ui-tabs-hide">
          <table class="data-table" id="data-table">
          </table>
          <c:choose>
            <c:when test="${not empty data }">
              <script>
                $(function() {
                  var data = ${data};
                  var $table = $('#data-table');
                  com.bouncingdata.Utils.renderDatatable(data, $table, { "sScrollX": "735px", "sScrollY": "500px", "bPaginate": false, "bFilter": false});  
                });
                                   
              </script>
            </c:when>
            <c:otherwise>
              <script>
              $(function() {
                console.debug("Load datatable by Ajax...");
                var guid = '${guid}';
                var columns = ${columns};
                var $table = $('#data-table');
                com.bouncingdata.Workbench.loadDatatableByAjax(guid, columns, $table);               
              });
              </script>  
            </c:otherwise>
          </c:choose>
        </div>
        <div id="schema" class="ui-tabs-hide">
          <!-- pre class="brush: sql" style="white-space: pre-wrap; word-wrap: break-word;">${dataset.schema }</pre-->
          <div class="schema-table-wrapper">
            <table id="schema-table" ticket="${ticket }">  
              <thead>
                <tr>
                  <th width="30%"><strong>Column Name</strong></th>
                  <th><strong>Data Type</strong></th>
                  <th width="55%"><strong>Description</strong></th>
                </tr>
              </thead>
              <tbody>
              <c:forEach items='${schema}' var='column'>
                <tr>
                  <td><span class="column-name">${column.name}</span></td>
                  <td><span class="column-type">${column.typeName }</span></td>
                  <td><span class="column-description"></span></td>
                </tr>
              </c:forEach>
              
              </tbody>           
            </table>
          </div>
        </div>
        <div id="ref-doc" class="ui-tabs-hide">
          <c:choose>
            <c:when test="${not empty dataset.refDocuments }">
              <c:set var="ctUrl">${pageContext.request.requestURL}</c:set>
              <c:set var="baseURL" value="${fn:replace(ctUrl, pageContext.request.requestURI, pageContext.request.contextPath)}" />
              <c:forEach items="${dataset.refDocuments }" var="ref">
                <c:if test="${ref.type == 'url' }">
                  <p>
                    <a>${ref.url }</a>
                  </p>
                </c:if>
                <c:if test="${ref.type == 'pdf' }">
                  <div>
                    <h4>${ref.name }</h4>
                    <p>
                      <iframe src="http://docs.google.com/viewer?url=${baseURL }/public/ref/${dataset.guid }?ref=${ref.guid }&embedded=true" style="width: 100%; height: 460px;" frameborder="0"></iframe>
                    </p>
                  </div>
                </c:if>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <span>No reference document.</span>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
      <div class="clear"></div>
      <div class="description dataset-description">
        <h3 style="margin: 0 0 10px; cursor: pointer">Description</h3>
        <c:choose>
          <c:when test="${not empty dataset.description }">
            <span>${dataset.description }</span>
          </c:when>
          <c:otherwise>
            <span>No description</span>
          </c:otherwise>
        </c:choose>
      </div>
      <!-- vinhpq : popup delete item -->
		<div id="dialog-confirm-delete" title="Delete item?" >
		  <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>The <font id="item-del-name"></font> item will be deleted and cannot be recovered. Are you sure?</p>
		  <div id="progress-del-img" style="display:none">
			<img src="<c:url value="/resources/images/wait.gif" />" style="vertical-align: middle;height: 18px;width: 18px; margin-right: 3px;"> waiting...
		  </div>
		</div>
      <!-- <div class="comments-container">
        <h3 class="comments-title">
          <a href="javascript:void(0);" onclick="$('#comment-form').toggle('slow');">Comment</a>
        </h3>
        <div class="comment-form" id="comment-form">
          <form>
            <fieldset>
              <p>
                <textarea rows="5" id="message"></textarea>
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
      </div> -->
    </div>
  </div>
</div>