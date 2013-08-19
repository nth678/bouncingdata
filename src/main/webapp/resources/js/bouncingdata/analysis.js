function Analysis() {
  
}


Analysis.prototype.init = function(anls, dbDetail) {
  var guid = anls.guid;
  //$('#anls-content').easytabs();
  var me = this;
  this.loadedData = false;
  this.votingCache = {};

  this.$commentTemplate = $('#comment-template').template();
  this.$commentEditor = $('#comment-editor-template').template();

  com.bouncingdata.Nav.setSelected('anls', anls.guid);

  $(function() {
    $('#anls-content').tabs();
    $('#related-tabs').tabs();
    
    com.bouncingdata.Dashboard.view(dbDetail.visualizations, dbDetail.dashboard, $('#main-content #anls-dashboard'));

    $('#anls-content').bind('tabsselect', function(event, ui) {
      // select data tab
      if (ui.index == 2 && me.loadedData == false) {
        var $dataPanel = $('#anls-data');
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
                  com.bouncingdata.Utils.renderDatatable($.parseJSON(data), $table,  { "sScrollY": "400px", "bPaginate": false, "bFilter": false });
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


$(document).on('click', '#detailsheader', function() {
    $(this).replaceWith("<div id='dv-edit-title' class='div-change-title'><input class='input-title' type='text' id='edit' value='" + $(this).text() + "' /></div>");
});

$(document).on('keydown', '#edit', function(e) {
	var keyCode = e.keyCode || e.which; 
	if ( keyCode == '13' || keyCode == '9') {
		var newTitle = this.value;
	    if (newTitle == '') {
	        alert("Cant be blank!");
	        return false;
	    }
	    else {
	    		$.ajax({
	    			  url: ctx + '/anls/changetitle',
	     			 type: 'post',
	     			 data: {
	     				 'guid': guid,
	     				 'newTitle': newTitle
	    		},
	            success: function(res) {
	              var result = res['code'];            
	              if (result < 0) {
	                alert("Error occured when trying change title of this analysis. Please try again.")
	              }else{
	            	  $('#dv-edit-title').replaceWith('<h2 class="tc_pageheader editableName" id="detailsheader">' + newTitle + '</h2>');
	              }
					return;     
	            },
	            error: function(res) {
	            	console.debug(res);
	            }
	          });
	    }
	}
});


    $('#comment-form #comment-submit').click(function() {
      // validate
      var message = $('#comment-form #message').val();
      if (!message) return;
      me.postComment(guid, message, -1, function() { $('#comment-form #message').val(''); });
    });

    $('.comments h3.comments-count').click(function() {
      $(this).next().toggle('slow');
    }).css('cursor', 'pointer');

    me.loadCommentList(guid);

    var $score = $('.header .score');
    var score = $score.text();
    if (score > 0) {
      $score.attr('class', 'score score-positive');
    } else {
      if (score == 0) $score.attr('class', 'score');
      else $score.attr('class', 'score score-negative');
    }

    $('.header a.anls-vote-up').click(function() {
      me.voteAnalysis(guid, 1);
      return false;
    });

    $('.header a.anls-vote-down').click(function() {
      me.voteAnalysis(guid, -1);
      return false;
    });
    
    /*$('.header a.anls-clone').click(function() {
      
      
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
          console.debug(res);
        }
      });
      
      return false;
    });*/

    // embedded
    var $embedded = $('#embedded-link');
    $('.anls-action-links a#anls-embed-button').click(function() {
      $embedded.toggle('slow');
      // still not reversed the remote ip to hostname, temporarily hard code the host
      var host = "www.bouncingdata.com";
      var embedded = '<iframe src="http://' + host + ctx + '/public/embed/' + guid + '/?tab=v&tab=c&tab=d" style="border:solid 1px #777" width="800" height="600" frameborder="0"></iframe>';
      $('#embedded-link-text', $embedded).val(embedded).click(function() {
        $(this).select();
        $(this).attr('title', 'Press CTRL-C to copy embedded code');
      });

      // reset options
//      $('#include-viz', $embedded).prop('checked', true);
//      $('#include-code', $embedded).prop('checked', false);
//      $('#include-data', $embedded).prop('checked', false);
//      $('#embedded-width', $embedded).val('800');
//      $('#embedded-height', $embedded).val('600');
//      $('#embedded-border', $embedded).prop('checked', false);
    });

    $('.embedded-options input', $embedded).change(function() {
      me.updateEmbeddedLink(guid);
    });

    var $publish = $('.anls-action-links a#anls-publish-button');
    if ($publish.length > 0) {
      $publish.click(function() {
        com.bouncingdata.Main.$publishDialog['object'] = anls;
        com.bouncingdata.Main.$publishDialog.dialog("open");
      });
    }

    $('#anls-code #code-block pre').text(anls["code"]);
    SyntaxHighlighter.highlight();
    
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
         		 'guid': guid,
         		 'tag': tag,
         		 'type': 'analysis'
      			  },
              
        success: function(res) { 
        	$('.add-tag-popup #add-tag-input').val('');        	
         	 var result = res['message'];
			 if(result=='') return;
        	 var tags= result.split(",");		

          for (var i=0;i<tags.length;i++){
				
        	  	var $newTag = $('<div class="tag-element-outer"><a class="tag-element" href="' + ctx + "/tag/" + tags[i] + '">' + tags[i] + '</a><span class="tag-remove" title="Remove tag from this analysis">x</span></div>');
 			 $('.tag-set .tag-list').append($newTag);
                   
          
          $('.tag-remove', $newTag).click(function() {
            var self = this;
            if (anls.user != com.bouncingdata.Main.username) return;
            var tag = $(this).prev().text();
            $.ajax({
              url: ctx + '/tag/removetag',
              type: 'post',
              data: {
                'guid': guid,
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
        },
        error: function(res) {
          console.debug(res);
        }
      });
    });
    
    $('.tag-element-outer .tag-remove').click(function() {
      var self = this;
      if (anls.user != com.bouncingdata.Main.username) return;
      var tag = $(this).prev().text();
      $.ajax({
        url: ctx + '/tag/removetag',
        type: 'post',
        data: {
          'guid': guid,
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
  });

}

/**
 *
 * @param guid
 * @param message
 * @param parentId
 * @param callback
 */
Analysis.prototype.postComment = function(guid, message, parentId, callback) {
  var me = this;
  if (!message) return;
  // post comment
  $.ajax({
    url: ctx + '/anls/commentpost/' + guid,
    type: 'post',
    data: {
      message: message,
      parentId: parentId
    }, 
    success: function(result) {
      if (!result) {
        console.debug("Comment post failed!");
        return;
      }
      console.debug("Comment post successfully!");
      me.addComment(guid, {
        id: result.id,
        message: message,
        parentId: parentId,
        lastUpdate: new Date(),
        user: { username: com.bouncingdata.Main.username }
      });
      
      if (callback) callback();
    }, 
    error: function(result) {
      console.debug("Comment post failed!");
    }
  });
}

/**
 * Loads the comment list for specific analysis
 * @param guid the analysis guid
 */
Analysis.prototype.loadCommentList = function(guid) {
  var me = this;
  
  // represents the hierachy, key: commentId, value: array of childId
  me.children = {};
  
  // array of root node
  me.roots = [];
  
  // maps the commentId with commentObj
  me.commentList = {};

  // callback function when click on 'reply'
  me.inlineReplyFunction = function(event) {
    //
    var $self = $(event.target);
    var $commentBody = $self.parent().parent();
    var $comment = $commentBody.parent();
    if ($commentBody.next().is('div.inline-editor')) {
      $commentBody.next().remove();
      return false;
    }
    var $inlineEditor = $.tmpl(me.$commentEditor, { rows: 3 });
    $commentBody.after($inlineEditor);
    $('input.reply-button', $inlineEditor).click(function() {
      var message = $(this).prev().val();
      if (!message) return false;
      me.postComment(guid, message, $comment.attr('nodeid'), function() {$commentBody.next().remove();});
    }).button();
    return false;
  }
  
  // fetch comment list from server
  $.ajax({
    url: ctx + '/anls/commentlist/' + guid,
    type: 'get',
    dataType: 'json',
    success: function(result) {      
      var $commentList = $('#comment-list');
      $commentList.css('background', '#fff');
      
      me.commentCount = result.length;
      
      me.updateCommentCounter();
      
      if (result.length==0) {
        return;
      }
      
      // build comment list hierachy     
      for (var i = 0; i < result.length; i++) {
        var id = result[i].id;
        var parentId = result[i].parentId;
        me.commentList[id] = result[i];   
        if (parentId > 0) {          
          if (!me.children[parentId]) me.children[parentId] = [ id ];
          else me.children[parentId].push(id);
        } else {
          me.roots.push(id);
        }   
      }
      
      // render comments
      var commentToInsert = [];
      for (var i = 0; i < me.roots.length; i++) {
        commentToInsert[i]= me.renderCommentNode(me.roots[i]);
      }
      
      $commentList.append(commentToInsert.join(''));

      $('a.comment-reply', $commentList).click(me.inlineReplyFunction);
      
      $('a.up-vote-link', $commentList).click(function() {
        var $comment = $(this).parent().parent().parent();
        me.voteComment(guid, $comment.attr('nodeid'), 1);
        return false;
      });
      
      $('a.down-vote-link', $commentList).click(function() {
        var $comment = $(this).parent().parent().parent();
        me.voteComment(guid, $comment.attr('nodeid'), -1);
        return false;
      });
      
      $('.comment-score', $commentList).each(function() {
        var $score = $(this);
        var score = $score.text();
        if (score > 0) {
          $score.attr('class', 'comment-score comment-score-positive');
        } else if (score == 0) $score.attr('class', 'comment-score');
        else $score.attr('class', 'comment-score comment-score-negative');
      });
    },
    error: function(result) {
      console.debug(result);
    }
  });
  
}

/**
 * Recursively generate the comment list html structure
 **/
Analysis.prototype.renderCommentNode = function(id) {
  var commentObj = this.commentList[id];
  if (!commentObj) return '';
  
  var $comment = $.tmpl(this.$commentTemplate, { id: id, 
    username: commentObj.user.username, 
    message: commentObj.message,
    date: new Date(commentObj.lastUpdate),
    upVote: commentObj.upVote,
    downVote: commentObj.downVote
  });
  // leaf node
  if (!this.children[id] || this.children[id].length <= 0) {    
    /** see main.js#jQuery.fn.outerHtml */ 
    return $comment.outerHtml();
  } else {    
    var $children = $comment.children('ul.children');
    // recursive
    for (childId in this.children[id]) {
      $children.append(this.renderCommentNode(this.children[id][childId]));
    }
    return $comment.outerHtml();
  }
}

Analysis.prototype.addComment = function(guid, commentObj) {
  var me = this;
  this.commentCount++;
  this.updateCommentCounter();
  var $commentList = $('#comment-list');
  var id = commentObj.id;
    
  var $comment = $.tmpl(this.$commentTemplate, { id: id, 
    username: commentObj.user.username, 
    message: commentObj.message,
    date: new Date(commentObj.lastUpdate),
    upVote: 0,
    downVote: 0
  });
  
  console.debug($comment);
  
  if (commentObj.parentId < 1) {
    $commentList.append($comment);
  } else {
    // find parent
    var $parent = $('#comment-' + commentObj.parentId , $commentList);
    if (!$parent) return false;
    $parent.children('ul.children').append($comment);
  }

  $('a.comment-reply', $comment).click(me.inlineReplyFunction);
  
  $('a.up-vote-link', $commentList).click(function() {
    var $comment = $(this).parent().parent().parent();
    me.voteComment(guid, $comment.attr('nodeid'), 1);
    return false;
  });
  
  $('a.down-vote-link', $commentList).click(function() {
    var $comment = $(this).parent().parent().parent();
    me.voteComment(guid, $comment.attr('nodeid'), -1);
    return false;
  });
}

Analysis.prototype.voteAnalysis = function(guid, vote) {
  var me = this;
  if (this.votingCache[guid] && this.votingCache[guid] * vote > 0) {
    console.debug("You have voted this analysis " + guid + "already");
    return;
  }
  
  if (!this.votingCache[guid]) this.votingCache[guid] = 0;

  $.ajax({
	  url: ctx + "/anls/vote/" + guid,
	  data: {
		  vote: vote
	  },
	  type: 'post',
	  success: function(result) {
		  if(result=='1'){
			  var $score = $('.header .score');
			  if (vote >= 0) {
				  me.votingCache[guid]++;
				  $score.text($score.text() - (-1));
			  } 
			  else {
				  me.votingCache[guid]--;
				  $score.text($score.text() - 1);
			  }
			  var score = $score.text();
			  if (score > 0) {
				  $score.attr('class', 'score score-positive');
			  } else {
				  if (score == 0) 
					  $score.attr('class', 'score');
				  else 
					  $score.attr('class', 'score score-negative');
			  }
		  }
	  },
	  error: function(result) {
		  console.debug("Failed to vote analysis " + guid);
		  console.debug(result);
	  }
  }); 
 
}

Analysis.prototype.voteComment = function(guid, commentId, vote) {
  var me = this;
  if (this.votingCache[commentId] && (this.votingCache[commentId] * vote > 0)) {
    console.debug("You have voted this comment #" + commentId + " already");
    return;
  }
  
  if (!this.votingCache[commentId]) this.votingCache[commentId] = 0;
  
  $.ajax({
    url: ctx + '/anls/commentvote/' + guid,
    data: {
      commentId: commentId,
      vote: vote
    },
    type: 'post',
    success: function(result) {
      var $commentBody = $('li.comment-item#comment-' + commentId + ' > div.comment-item-body');
      var $score = $('span.comment-score', $commentBody);
      if (vote >= 0) {
        me.votingCache[commentId]++;          
        $score.text($score.text() - (-1));
      } else {
        me.votingCache[commentId]--;
        $score.text($score.text() - 1);
      }
      var score = $score.text();
      if (score > 0) {
        $score.attr('class', 'comment-score comment-score-positive');
      } else {
        if (score == 0) $score.attr('class', 'comment-score'); 
        else $score.attr('class', 'comment-score comment-score-negative');
      }
    },
    error: function(result) {
      console.debug("Failed to vote comment #" + commentId);
      console.debug(result);
    }
  });
}

Analysis.prototype.updateCommentCounter = function() {
  $('.comments .comments-count').text(this.commentCount + " comments");
}

Analysis.prototype.reload = function() {
  
}

Analysis.prototype.updateEmbeddedLink = function(guid) {
  var tabs = [];
  var width = 0;
  var height = 0;
  if ($('.embedded-options #include-viz').prop('checked')) {
    tabs.push('v')
  }
  if ($('.embedded-options #include-code').prop('checked')) {
    tabs.push('c');
  }
  if ($('.embedded-options #include-data').prop('checked')) {
    tabs.push('d');
  }
    
  width = $('.embedded-options #embedded-width').val();
  height = $('.embedded-options #embedded-height').val();
  
  var border = $('.embedded-options #embedded-border').prop('checked');
  
  if (isNaN(width) || width < 1) {
    width = 800;
    $('.embedded-options #embedded-width').val(width);
  }
  if (isNaN(height) || height < 1) {
    height = 600;
    $('.embedded-options #embedded-height').val(height);
  }
  
  var host = "www.bouncingdata.com";
  var link = "http://" + host + ctx + "/public/embed/" + guid;
  if (tabs.length > 0) {
    for (i in tabs) {
      if (i == 0) {
        link = link + "/?tab=" + tabs[i];
      } else {
        link = link + "&tab=" + tabs[i];
      } 
    }
  }
  
  var embedded = '<iframe src="' + link + '" style="' + (border?'border:solid 1px #777':'border-width:0') + '" width="' + width + '" height="' + height + '" frameborder="0"></iframe>';
  $('.embedded-link #embedded-link-text').val(embedded);
  
}

com.bouncingdata.Analysis = new Analysis();