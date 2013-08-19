function Editor() {
  
}

/**
 * Initializes the editor, also step 1 of analysis creation process
 */
Editor.prototype.init = function(anls, feature) {
  var me = this;
  me.anls = anls;
  var executeAction = 'execute';
  $(function() {    
    com.bouncingdata.Main.toggleLeftNav();
    var $executeButton = $('#editor-execute');
    $executeButton.button();
    
    me.jqConsole = $('.editor-container .execution-logs .console').jqconsole('Welcome to our console\n', Utils.getConsoleCaret('python'));
    me.startPrompt(me.jqConsole, 'python');
    
    if (feature == 'edit') {
      $('span',$executeButton).text('Next');
      executeAction = 'next';
      
      me.jqConsole.Write(lastOutput, 'jqconsole-output');
      me.startPrompt(me.jqConsole, 'python');
    }
    
    //initialize ace editor
    var editorDom = $('.editor-container .code-editor')[0];
    me.editor = ace.edit(editorDom);
    me.editor.getSession().setMode('ace/mode/python');
    
    me.editor.getSession().getDocument().setValue(anls.code);
    
    me.editor.on('change', function(e) {
      if (feature == 'edit') {
        $('span',$executeButton).text('Execute');
        executeAction = 'execute';
      }
    }); 
    
    me.$message = $(".editor-status .status-message");
    me.$loading = $(".editor-status .ajax-loading");
    
    $('button#editor-execute').click(function() {
     debugger;
     var type = 'analysis';
     if (executeAction == 'execute') {
        me.execute(type);
      } else if (executeAction == 'next') {
        // just go to next step
        window.location.href = ctx + '/editor/anls/' + me.anls.guid + '/size';
      } else {
        // do nothing
        console.debug('Unknown action!');
      }     
    });
    
    $('button#speditor-execute').click(function() {
        debugger; 
        var type = 'scraper';
        if (executeAction == 'execute') {
           me.execute(type);
        } else {
           // do nothing
           console.debug('Unknown action!');
         }     
    });
    
    $('#execution-logs .clear-console').click(function() {
      me.jqconsole.Reset();
      me.startPrompt(jqconsole, 'python');
    });
    
    $('button#editor-clone').click(function() {
      me.clone(anls);
      return false;
    });
    
  });
}

/**
 * Initializes the logging console prompt
 */
Editor.prototype.startPrompt = function(jqconsole, language) {
  var me = this;
  jqconsole.Prompt(true, function(input) {
    $.ajax({
      url: ctx + "/shell/execute",
      type: "get",
      data: {
        code: input,
        language: language
      },
      success: function(result) {
        jqconsole.Write(result + '\n', 'jqconsole-output');
        me.startPrompt(jqconsole, language);
      },
      error: function(result) {
        console.info(result);
        me.startPrompt(jqconsole, language);
      }
    });
  });
}

/**
 * Sizing step (step 2) in analysis creation process
 */
Editor.prototype.initSize = function(anls, dbDetail) {
  var me = this;
  me.anls = anls;
  this.loadedData = false;
  $(function() {
    com.bouncingdata.Main.toggleLeftNav();
    var $tabs = $('#size-tabs').tabs();
    $('.editor-nav').button();
    
    // set code
    $('#size-code .code-block pre').text(anls["code"]);
    SyntaxHighlighter.highlight();
    
    var $dashboard = $('#viz-dashboard', $tabs);
    me.loadDashboard(dbDetail.visualizations, dbDetail.dashboard, $dashboard, anls);
    
    // load data
    $('#size-tabs').bind('tabsselect', function(event, ui) {
      // select data tab
      if (ui.index == 2 && me.loadedData == false) {
        var $dataPanel = $('#size-data');
        var dsguids = '';
        $('.anls-dataset', $dataPanel).each(function() {
          dsguids += $(this).attr('dsguid') + ',';
        });
        dsguids = dsguids.substring(0, dsguids.length - 1);
        if (dsguids.length > 0) {
          com.bouncingdata.Utils.setOverlay($dataPanel, true);
          $.ajax({
            url: ctx + '/dataset/m/' + dsguids,
            type: 'get',
            dataType: 'json',
            success: function(result) {
              com.bouncingdata.Utils.setOverlay($dataPanel, false);
              $('.anls-dataset', $dataPanel).each(function() {
                var dsguid = $(this).attr('dsguid');
                var $table = $('table', $(this));
                var data = result[dsguid].data;
                if (data) {
                  com.bouncingdata.Utils.renderDatatable($.parseJSON(data), $table, { "sScrollY": "400px", "bPaginate": false, "bFilter": false });
                } else if (result[dsguid].size > 0) {
                  console.debug("Load datatable by Ajax...");
                  var columns = result[dsguid].columns;
                  com.bouncingdata.Workbench.loadDatatableByAjax(dsguid, columns, $table);
                }
              });
              me.loadedData = true;
            },
            error: function(result) {
              com.bouncingdata.Utils.setOverlay($dataPanel, false);
              console.debug('Failed to load datasets.');
              console.debug(result);
              $dataPanel.text('Failed to load datasets.');
            }
          });
        }
      }
    });
    
    
    $('button#editor-clone').click(function() {
      me.clone(anls);
      return false;
    });
    
  });
}

/**
 * Last step in analysis creation process. In this step, user can add information and publish analysis
 */
Editor.prototype.initDescribe = function(anls) {
  var me = this;
  $(function() {
    com.bouncingdata.Main.toggleLeftNav();
    $('.editor-nav').button();
    
    if (anls.published) {
      $('#describe-ispublic').prop('checked', 'checked');
    }
    
    tinyMCE.init({
      mode : "textareas",
      theme : "advanced",
      plugins : "codeElement,latex,autolink,lists,pagebreak,style,layer,save,advhr,advimage,advlink,iespell,inlinepopups,insertdatetime,preview,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,advlist,visualblocks",

      // Theme options
      theme_advanced_buttons1 : "codeElement,latex,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,bullist,numlist,|,link,unlink,code,|,forecolor,backcolor,|,formatselect,fontselect,fontsizeselect,|,preview",     
      theme_advanced_toolbar_location : "bottom",
      theme_advanced_toolbar_align : "center",
      theme_advanced_statusbar_location : "bottom",
      theme_advanced_resizing : true

    });
    
    $('a.add-tag-link').click(function() {
      $(this).next().show().addClass('active');
      return false;
    });
    
    $('div.add-tag-popup').click(function() {
      return false;
    });
    
    $(document).click(function() {
      var $addTagPopup = $('div.add-tag-popup');
      if ($addTagPopup.hasClass('active')) {
        $addTagPopup.removeClass('active');
        $addTagPopup.hide();
      }
    });
    
    $(document).on('keydown', '.add-tag-popup #add-tag-input', function(e) {
    	var keyCode = e.keyCode || e.which;
    	if ( keyCode == '13') {
    		$('.add-tag-popup #add-tag-button').click();
    	}
    });
    
    
    $('.add-tag-popup #add-tag-button').click(function() {
	      var tag = $('#add-tag-input').val();
	      if (!tag) return false;
	      $.ajax({
	        url: ctx + '/tag/addtag',
	        type: 'post',
	        data: {
	          'guid': anls.guid,
	          'tag': tag,
	          'type': 'analysis'
	        },
	        success: function(res) {
	    	console.debug(res);
	    	var result = res['message'];
			 if(result=='') return;
			 var tags= result.split(",");	
	         for (var i=0;i<tags.length;i++){        	 
	          var $newTag = $('<div class="tag-element-outer"><a class="tag-element" href="' + ctx + '/tag/' + tags[i] + '">' + tags[i] + '</a><span class="tag-remove" title="Remove tag from this datasetd">x</span></div>');
	          $('.tag-set .tag-list').append($newTag);
	          $('.tag-remove', $newTag).click(function() {
	              var self = this;
//	              if (anls.user != com.bouncingdata.Main.username) return;
	              var tag = $(this).prev().text();
	              $.ajax({
	                url: ctx + '/tag/removetag',
	                type: 'post',
	                data: {
	                  'guid': anls.guid,
	                  'tag': tag,
	                  'type': 'analysis'
	                },
	                success: function(res) {
	                  if (res['code'] < 0) {
	                    console.debug(res);
	                    return;
	                  }
	                  $(self).parent().remove();
	                },
	                error: function(res) {
	                  console.debug(res);
	                }
	              });
	            });
	          
	        }
	         
	        //delete value in input 
	        $('#add-tag-input').val('');
	        },
	        error: function(res) {
	          console.debug(res);
	        }
	      });
    });
    
    
    $('.tag-element-outer .tag-remove').click(function() {
      var self = this;
      var tag = $(this).prev().text();
      $.ajax({
        url: ctx + '/tag/removetag',
        type: 'post',
        data: {
          'guid': anls.guid,
          'tag': tag,
          'type': 'analysis'
        },
        success: function(res) {
          if (res['code'] < 0) {
            console.debug(res);
            return;
          }
          $(self).parent().remove();
        },
        error: function(res) {
          console.debug(res);
        }
      });
    });
    
        
    $('#describe-publish').click(function() {
      var name = $('#detail-form #name').val();
      var description = tinymce.editors[0].getContent(); //$('#detail-form #description').val();
      var isPublic = $('#describe-ispublic').prop('checked');
      $('.saving-status .ajax-loading').css('opacity', 1);
      $('.saving-status .status-message').text('Saving..').css('color', 'green');
      $.ajax({
        url: ctx + '/editor/anls/' + anls.guid + '/describe',
        type: 'post',
        data: {
          'name': name,
          'description': description,
          'isPublic': isPublic
        },
        success: function(res) {
          $('.saving-status .ajax-loading').css('opacity', 0);
          $('.saving-status .status-message').text('Saved').css('color', 'green');
          window.location.href = ctx + '/anls/' + anls.guid;
        },
        error: function(res) {
          console.debug(res);
          $('.saving-status .ajax-loading').css('opacity', 0);
          $('.saving-status .status-message').text('Failed').css('color', 'red');
        }
      });
      return false;
    });
    
    $('button#editor-clone').click(function() {
      me.clone(anls);
      return false;
    });
  }); 
}


Editor.prototype.loadDashboard = function(visuals, dashboard, $dashboard, anls) {
  $dashboard.attr('guid', anls['guid']).attr('tabid', 1); 
  com.bouncingdata.Dashboard.load(visuals, dashboard, $dashboard, anls.username==com.bouncingdata.Main.username);
}

Editor.prototype.execute = function(type,callback) {
  debugger;
  var me = this;
  var code = me.editor.getSession().getDocument().getValue();
  if (!code) {
    console.debug('No code to execute!');
    return;
  }
  
  me.setStatus("running");
  
  $.ajax({
    url: ctx + "/app/e/" + me.anls.guid,
    type: 'post',
    data: {
      code: code,
      language: me.anls.language,
      type: type
    },
    success: function(result) {
      if (result['statusCode'] == 0) {
        me.setStatus("finished-running");
        me.jqConsole.Write(result['output'], 'jqconsole-output');
        me.startPrompt(me.jqConsole, 'python');
        
          // reload datasets & viz.
        if (type == 'analysis') {
          var visCount = result['visCount'];
          var datasetCount = result['datasetCount'];
          var execId = result['executionId'];
          console.debug(result);
          
          // only redirect to size page if the visualization dashboard is not empty
          if (visCount > 0) {
            window.location = ctx + '/editor/anls/' + me.anls.guid + '/size?execid=' + execId;
          }
        } else if (type == 'scraper') {
          var datasets = result['datasets'];
          var $dsContainer = $('#' + tabId + '-data', $tab);
          //for (name in datasets) {
          //  var data = datasets[name];
          //  me.renderDataset(name, data, $dsContainer);
          //}
          
          //me.renderDatasets(datasets, $dsContainer);
        }
        
        if (callback) callback();
        
      } else {
        console.debug(result);
        me.setStatus("error");
        me.jqConsole.Write(result['output'], 'jqconsole-output');
      }
    },
    error: function(msg) {
      console.debug(msg);
      me.setStatus("error");
    }
  });
  
}

/**
 * Clone current analysis, the new analysis will be opened in a new tab
 */
Editor.prototype.clone = function(anls) {
  $.ajax({
    url: ctx + '/anls/clone/' + anls.guid,
    success: function(res) {
      if (res == "error") {
        alert("Error occured when trying clone this analysis. Please try again.")
        return;
      }
      
      window.open(ctx + '/editor/anls/' + res + '/size', '_blank');

    },
    error: function(res) {
      alert("Error occured when trying clone this analysis. Please try again.");
      console.debug(res);
    }
  });
}

Editor.prototype.setStatus = function(status) {
  if (status) {
    switch(status) {
    case "running":
      this.$message.text("Running...").css("color", "green");
      this.$loading.css("opacity", 1);
      break;
    case "finished-running":
      this.$message.text("Finished running.").css("color", "green");;
      this.$loading.css("opacity", 0);
      break;
    case "error":
      this.$loading.css("opacity", 0);
      this.$message.text("Error").css("color", "red");
      break;
    default:
      this.$message.text("");
      this.$loading.css("opacity", 0);
    }
  } else {
    this.$message.text("");
    this.$loading.css("opacity", 0);
  }
}

Editor.prototype.initProgressLinks = function() {
  var me = this;
  var $code = $('.top-bar .editor-progress a.editor-code-link');
  var $size = $('.top-bar .editor-progress a.editor-size-link');
  var $describe = $('.top-bar .editor-progress a.editor-describe-link');
  
  $code.click(function() {
    com.bouncingdata.Nav.fireAjaxLoad(ctx + '/editor/anls/' + me.anls.guid + '/edit', false); 
    return false;
  });
  
  $size.click(function() {
    com.bouncingdata.Nav.fireAjaxLoad(ctx + '/editor/anls/' + me.anls.guid + '/size', false); 
    return false;
  });
  
  $describe.click(function() {
    com.bouncingdata.Nav.fireAjaxLoad(ctx + '/editor/anls/' + me.anls.guid + '/describe', false); 
    return false;
  });
}

Editor.prototype.newAnalysis = function(name, language, isPublic) {
  var data = {
    appname : name,
    language : language,
    description : '',
    code : '',
    isPublic : isPublic,
    tags : '',
    type : 'analysis'
  };
  
  var me = this;
  
  $.ajax({
    url: ctx + "/main/createapp",
    data: data,
    type: "post",
    success: function(anls) {
      window.location = ctx + '/editor/anls/' + anls.guid + '/edit';
    },
    error: function(res) {
      console.debug(res)
    }
  });
}

com.bouncingdata.Editor = new Editor();
