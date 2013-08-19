<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<style>
    #change-pw-form label, #change-pw-form input { display:block; }
    #change-pw-form input.text { margin-bottom:12px; width:95%; padding: .4em; }
    #change-pw-form fieldset { padding:0; border:0; margin-top:25px; }
    .validateTips-password { border: 1px solid transparent; padding: 0.3em; }
    .ui-dialog .ui-state-error { padding: .3em; }
</style>
 <script>
 $(function() {
	 //vinhpq : preview webpage onmouseover event 
	 $('.popover').on({
		    mousemove: function(e) {
		        $(this).next('a').css({
		            top: e.pageY - 100,
		            left: e.pageX + 10
		        });
		    },
		    mouseenter: function() {
		        var lnk = $(this).attr('href');

		        var big = $('<iframe />', {'class': 'big_img', src: lnk,scrolling: 'no'});
		        $(this).after(big);
		    },
		    mouseleave: function() {
		        $('.big_img').remove();
		    }
		});
	 //End
	 
	    var npass = $( "#change-p-input" ),
	      cpass = $( "#confirm-p-input" ),
	      allFields = $( [] ).add(npass).add(cpass),
	      tips = $( ".validateTips-password" );
	 
	    function updateTips( t ) {
	      tips
	        .text( t )
	        .addClass( "ui-state-highlight" );
	      setTimeout(function() {
	        tips.removeClass( "ui-state-highlight", 1500 );
	      }, 500 );
	    }
	 
	    function checkLength( o, n, min, max ) {
	      if ( o.val().length > max || o.val().length < min ) {
	        o.addClass( "ui-state-error" );
	        updateTips( "Length of " + n + " must be between " +
	          min + " and " + max + "." );
	        return false;
	      } else {
	        return true;
	      }
	    }
	    
	    function checkValue( np, cp ) {
	      if ( np.val()!=cp.val() ) {
	    	np.addClass( "ui-state-error" );
	        updateTips( "The passwords do not match." );
	        return false;
	      } else {
	        return true;
	      }
	    }
	 
	    $( "#dialog-form-change-password" ).dialog({
	      autoOpen: false,
	      height: 300,
	      width: 350,
	      modal: true,
	      buttons: {
	        "Change": function() {
	          var bValid = true;
	          allFields.removeClass( "ui-state-error" );
	 
	          bValid = bValid && checkLength( npass, "new password", 4, 80 );
	          bValid = bValid && checkLength( cpass, "confirm password", 4, 80 );
	          bValid = bValid && checkValue( npass, cpass );
	          
	          if ( bValid ) {
	        	  $("#progress-p-change").show();
	        	  $.ajax({
						type : "post",
						url : '<c:url value="/auth/changepasswd"/>' ,
						data : {
							"npassword" : npass.val(),
						},
						success : function(res) {
							$( "#dialog-form-change-password" ).dialog( "close" );
							$("#progress-p-change").hide();
							
							if (res['code'] < 0) {
								window.alert("Failed to change password.\nError: " + res['message']);
						        return;
							}else{
								$( "#dialog-message-c-password" ).dialog("open");
							}
						}
					});
	          }
	        },
	        Cancel: function() {
	          $( this ).dialog( "close" );
	        }
	      },
	      close: function() {
	        allFields.val( "" ).removeClass( "ui-state-error" );
	      }
	    });
	 
	    $( "#change-password" ).click(function() {
	        $( "#dialog-form-change-password" ).dialog( "open" );
	    });
	    
	    $("#dialog-message-c-password").dialog({
		   	  autoOpen: false,
		      modal: true,
		      buttons: {
		        Ok: function() {
		          $( this ).dialog( "close" );
		        }
		      }
	    });
	  });
 </script>


<div class="header-content">
  <div class="header-login" style="display: none;">
    <sec:authorize access="isAuthenticated()">
      <div>Welcome back <span style="font-weight: bold;"> 
          <sec:authentication property="principal.username" />
        </span>
        <a style="color: blue;" href="<c:url value="/auth/j_spring_security_logout" />">Logout</a>
      </div>
    </sec:authorize>
  </div>
  
  <h2 class="header-logo">
    <a href="<c:url value="/"/>">Bouncing Data</a>
  </h2>
  <div class="top-page-panel">
    <div class="search-container">
      <form id="search-form" class="search-form" method="get">
        <div class="search-input-wrapper">
          <input type="text" class="search-input" id="query" name="query" />
        </div>
        <input type="hidden" name="criteria" value="global" id="criteria" />
        <!-- button type="submit" id="search-submit">Search</button-->
      </form>
    </div>
    <div class="header-buttons">
      <ul class="guide-button-container">
        <li><a href="<c:url value="/learn"/>" class="guide-button learn-button">Learn</a></li>
        <li><a href="<c:url value="/help/python"/>" class="guide-button help-button">Help</a></li>
        <li><a class="guide-button me-button">Me</a></li>
      </ul>
      <div class="guide-button-submenu">
        <div class="header-hidden-menu me-menu">
          <ul>
            <li><a id="change-password" name="change-password" class="header-submenu-item" href="#">Change password</a></li>
            <li><a class="header-submenu-item" href="<c:url value="/auth/security_logout" />">Logout</a></li>
          </ul>
        </div>
      </div>
      <div class="create-button-container">
        <div id="create-button" class="create-button">
          <a id="create-button-link" class="create-button-link" href="javascript:void(0);">Create</a>
        </div>
        <div class="create-submenu header-hidden-menu" style="display: none;">
          <ul>
            <li><a id="create-analysis" class="create-sub-item header-submenu-item" href="javascript:void(0);"><span class="sub-item-icon"></span>Analysis</a></li>
            <li><a id="create-dataset" class="create-sub-item header-submenu-item" href="<c:url value="/dataset/upload" />"><span class="sub-item-icon"></span>Dataset</a></li>
            <li><a id="create-scraper" class="create-sub-item header-submenu-item" href="javascript:void(0);"><span class="sub-item-icon"></span>Scraper</a></li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- vinhpq : popup change password -->
<div id="dialog-form-change-password" title="Change password" style="display: none;">
  <p class="validateTips-password">All form fields are required.</p>
  <form id="change-pw-form">
  <fieldset>
    <label for="name">New password:</label>
    <input type="password" name="change-p-input" id="change-p-input" class="text ui-widget-content ui-corner-all" />
    <label for="email">Confirm new password:</label>
    <input type="password" name="confirm-p-input" id="confirm-p-input" value="" class="text ui-widget-content ui-corner-all" />	    
  	<div id="progress-p-change" name="progress-p-change" style="display: none;">
  		<img src="<c:url value="/resources/images/ajax-loader.gif"/>" height="25" width="25">
  	</div>
  </fieldset>
  </form>
</div>
<div id="dialog-message-c-password" title="Change password complete" style="display: none;">
  <p>
    <span class="ui-icon ui-icon-circle-check" style="float: left; margin: 0 7px 50px 0;"></span>
    Change password successfully !  
  </p>
</div>
	
<jqtemplate>
  <!-- Comment templates -->
  <script id="comment-template" type="text/x-jquery-tmpl">
    <li class="comment-item" id="comment-\${id}" nodeid="\${id}">
      <div class="comment-item-body">
        <div class="comment-header">
          <div class="comment-author">\${username}</div>
        </div>
        <div class="comment-message">\${message}</div>
        <div class="comment-footer">
          <span class="comment-date">\${date}</span>&nbsp;
          <strong><span class="comment-score">\${upVote - downVote}</span>&nbsp;</strong>
          <!--span class="up-vote">\${upVote}</span>&nbsp;-->
          <a class="up-vote-link" href="#"><span class="up-vote-icon">Vote up</span></a>&nbsp;
          <!--span class="down-vote">\${downVote}</span>&nbsp;-->
          <a class="down-vote-link" href="#"><span class="down-vote-icon">Vote down</span></a>&nbsp;
          <a class="comment-reply" href="#">Reply</a>
        </div>
      </div>
      <ul class="children"></ul>
    </li>
	</script>
  <script id="comment-editor-template" type="text/x-jquery-tmpl">
    <div class="comment-editor inline-editor">
      <textarea class="reply-text" rows="\${rows}" spellcheck='false'></textarea>
      <input class="reply-button" type="button" value="Reply" />
      <div class="clear"></div>
    </div>
	</script>
  
  <!-- Dataset view templates  -->
  <script id="data-view-template" type="text/x-jquery-tmpl">
    <div class="dataset-view-container">
      <div class="dataset-view-layout" id="dataset-view-layout-\${tabId}">
        <div class="dataset-view-center" id="dataset-view-center-\${tabId}">
          <div class="dataset-view-main">
            <div class="dataset-view">
              <table class="dataset-table"></table>
            </div>
            <div class="dataset-query">
              <div class="dataset-query-editor">
                <textarea rows="5" class="code-editor query-editor" spellcheck="false"></textarea>
              </div>
              <div class="dataset-query-actions">
                <img id="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loading.gif" />" style="display:none;"  />
                <span id="ajax-message" style="color: Green; font-style: italic;"></span>
                <input class="dataset-query-execute" type="button" value="Execute" />
              </div>
            </div>
          </div>
        </div> 
        <div class="dataset-view-east" id="dataset-view-east-\${tabId}">
          <div class="dataset-view-side dataset-view-info">
            <p>
              <strong>Dataset: </strong>
              <span>\${dsName }</span>
            </p>
            <p>
              <strong>Author: </strong>
              <span>\${dsAuthor }</span>
            </p>
            <p>
              <strong>Schema: </strong>
              <span>\${dsSchema }</span>
            </p>
            <p>
              <strong>Row count: </strong>
              <span>\${dsRowCount }</span>
            </p>
            <p>
              <strong>Create date: </strong>
              <span>\${dsCreateDate }</span>
            </p>
            <p>
              <strong>Last update: </strong>
              <span>\${dsLastUpdate }</span>
            </p>
            <p>
              <strong>Tags: </strong>
              <span>\${dsTags }</span>
            </p>
          </div>
        </div>
      </div>    
    </div>
  </script>

  <script id="workbench-flow-template" type="text/x-jquery-tmpl">
    <div class="workbench-flow">
      <div class="app-info">
        <div class='app-title'><label style='font-weight: bold;'>Name: </label>\${appName}</div>
        <div class='app-language'><label style='font-weight: bold;'>Language: </label>\${appLang}</div>
      </div>
      <div class="workbench-flow-nav">
        <div class="flow-nav-item">
          <a href="javascript:void(0)" class="flow-nav-editor">Code</a>
        </div>
        <div class="flow-nav-item">
          <a href="javascript:void(0)" class="flow-nav-viz">Visualization</a>
        </div>
      </div>
      <div class="workbench-flow-content">
        <div class="workbench-editor">
          <div class="app-actions">
            <button id="app-execute-button-\${tabId}" class="app-action-button app-execute-button" title="Execute">Execute</button>
            <button id="app-view-viz-\${tabId}" class="app-action-button app-view-viz-button" title="View visualization">Visualization</button>
          </div>
          <div class="app-status">
            <img id="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loader.gif" />" style="opacity: 0;"  />
            <span id="ajax-message" style="color: Green; font-style: italic;"></span>
            <div class="saving-status">*</div>
          </div>
          <div class="code-editor-wrapper">
            <div>
              <div id="code-editor-\${tabId}" class="code-editor"></div>
            </div>
          </div>
          <div class="execution-logs-wrapper">
            <div id="execution-logs-\${tabId}" class="execution-logs">
              <div class="console prompt" style="display: block;"></div>
              <div class="console-actions">
                <input class="clear-console" type="button" value="Clear console" />
              </div>
            </div>
          </div>
        </div>
        <div class="workbench-result">
          <div class="app-actions">
            <button id="app-post-button-\${tabId}" class="app-action-button app-post-button" title="Post">Publish</button>
            <button id="app-back-button-\${tabId}" class="app-action-button app-back-button" title="Back">Back</button>
          </div>
          <div class="result-tabs output-tabs" id="\${tabId}-result-tabs">
            <ul>
              <li><a href="#\${tabId}-viz">Visualization</a></li>
              <li><a href="#\${tabId}-data">Data</a></li>
              <li><a href="#\${tabId}-code">Code</a></li>
            </ul>
            <div class="clear"></div>
            <div class="result-tab-viz ui-tabs-hide" id="\${tabId}-viz">
              <div><strong>Visualization Dashboard.</strong></div><br />
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
                  <div id="dashboard-wrapper-\${tabId}" class="dashboard-wrapper" style="width: 800px;position: absolute; visibility: hidden; height: 14000px;"></div>
                  <div class="viz-dashboard" id="viz-dashboard-\${tabId}"></div>
              </div>
            </div>
            <div class="result-tab-code ui-tabs-hide" id="\${tabId}-code">
              <div class="code-block">
                <pre class="brush: py"></pre>
              </div>
            </div>
            <div class="result-tab-data ui-tabs-hide" id="\${tabId}-data">
              <span class="no-data">No data</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </script>
  
  <script id="workbench-content-template" type="text/x-jquery-tmpl">
    <div class="workbench-ide-content">
        <div class="app-info">
          <div class='app-title'><label style='font-weight: bold;'>Application name: </label>\${appName}</div>
          <div class='app-language' id='app-language'><label style='font-weight: bold;'>Language: </label>\${appLang}</div>
          <div class='app-author'><label style='font-weight: bold;'>Author: </label>\${appAuthor}</div>
        </div>
        <div class="new-app-info">
          <strong>Language: </strong>
          <select class="language-select">
            <option value='python'>Python</option>
            <option value='r'>R</option>
          </select>
        </div>
        <div class="app-status">
          <img id="ajax-loading" width="20px" height="20px" src="<c:url value="/resources/images/ajax-loader.gif" />" style="opacity: 0;"  />
          <span id="ajax-message" style="color: Green; font-style: italic;"></span>
        </div>

        <div class="app-actions">
          <button id="app-execute-button-\${tabId}" class="app-action-button app-execute-button" title="Execute">Execute</button>
          <button id="app-post-button-\${tabId}" class="app-action-button app-post-button" title="Post">Post</button>
          <button id="app-back-to-code-button-\${tabId}" class="app-action-button app-back-to-code-button" title="Back">Back</button>
        </div>
        <div class="clear"></div>
        <div class="app-editor-container app-editor-tabs ui-tabs">
          <ul>
            <li><a href="#\${tabId}-code">Code</a></li>
            <li><a href="#\${tabId}-viz">Visualization</a></li>
            <li><a href="#\${tabId}-data">Data</a></li>
          </ul>
          
          <div class="app-code ui-tabs-hide" id="\${tabId}-code">
            <div class="app-code-editor">
              <div>
                <div id="code-editor-\${tabId}" class="code-editor"></div>
              </div>    
            </div>
            <div class="execution-logs-wrapper">
              <div id="execution-logs-\${tabId}" class="execution-logs">
                <div class="console prompt" style="display: block;"></div>
                <div class="console-actions">
                  <input class="clear-console" type="button" value="Clear console" />
                </div>
              </div>
            </div>
          </div>
          <div class="app-viz ui-tabs-hide" id="\${tabId}-viz">
            <div><strong>Visualization Dashboard.</strong></div><br />
            <div class="app-viz-dashboard">
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
              <div id="dashboard-wrapper-\${tabId}" class="dashboard-wrapper" style="width: 800px;position: absolute; visibility: hidden; height: 14000px;"></div>
              <div class="viz-dashboard" id="viz-dashboard-\${tabId}"></div>
            </div>
          </div>
          <div class="app-data ui-tabs-hide" id="\${tabId}-data">
            <span class="no-data">No data</span>
          </div>
        </div>
    </div>
  </script>    
  
  <script id="browser-item-template" type="text/x-jquery-tmpl">
    <div class="browser-item">
      <div class="browser-item-header">
        <a href="#"><span class="browser-item-title"><strong>\${title}</strong></span></a>
      </div>
      <div class="browser-item-footer">
        <a class="browser-item-footer-link expand-link" href="javascript:void(0);">Expand</a>
        <a class="browser-item-footer-link browser-item-action open-link" href="javascript:void(0)">Open</a>
      </div>
      <div class="browser-item-detail">
        <div class="browser-item-description browser-item-info">
          <strong>Description: </strong><span>\${description }</span>
        </div>
        <div class="browser-item-info application-language"><strong>Language: </strong>\${language}</div>
        <div class="browser-item-info"><strong>Author: </strong>\${author }</div>
        <div class="browser-item-info line-count"><strong>Line count: </strong>\${lineCount }</div>
        <div class="browser-item-info"><strong>Is public: </strong>\${public }</div>
        <div class="browser-item-info"><strong>Create date: </strong>\${createDate }</div>
        <div class="browser-item-info"><strong>Last update: </strong>\${lastUpdate }</div>
        <div class="browser-item-info browser-item-tags"><strong>Tags: </strong>\${tags }</div>
      </div>
  	</div>
  </script>
  
  <script id="feed-item-template" type="text/x-jquery-tmpl">  
	<div class="event stream-item \${classType}" aid=\${id }>
                <div class="event-content">
                  <a class="event-avatar-link">
                    <img class="avatar no-avatar" src="<c:url value="/resources/images/no-avatar.png" />">
                  </a>
                  <div class="thumbnail">
                    <a href="\${url}">
                      <img class="thumb-img" src="\${thumbnail}" onerror="this.src='<c:url value="/thumbnails/no-image.jpg" />'; this.onerror=null;" />
                    </a>
                  </div>
                  <p class="title">
                  	<!-- vinhpq : preview webpage on mouse over event (acitive func : adding in link class="popover") -->
                    <a id="evt-title-${id }" href="\${url}" ><strong>\${name}</strong></a>
                  </p>
                  <div class="info">
                    <span class="author">Author: <a href="#">\${username }</a></span><br/>
                    <div class="tag-list">Tags:&nbsp;</div>
                  </div>
                  <div class="description"></div>
                  <div class="clear"></div>
                  <div class="event-footer">
					<font id="pbicon"></font>
					<font id="pvicon"></font>                    
					<strong class="event-score">\${score}</strong>&nbsp;
					<a id="evt-comment-\${id }" class="comments-link" href="\${cmturl}"><strong>\${commentCount }</strong>&nbsp;comments</a>
					<font id="delicon"></font> 
                  </div>
                </div>
                <div class="clear"></div>
              </div>
  </script>
</jqtemplate>
