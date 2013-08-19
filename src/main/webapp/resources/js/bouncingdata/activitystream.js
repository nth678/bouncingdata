function ActivityStream() {

}

ActivityStream.prototype.init = function() {
  var me = this;
  this.streamEnded = false;
  this.streamLoading = false;
  me.$feedTemplate = $('#feed-item-template').template();
  var main = com.bouncingdata.Main;

  //com.bouncingdata.Nav.setSelected('page', 'stream');
  /*$('#page>.main-container>.main-navigation ul.main-nav-links li:first-child a').addClass('nav-selected');*/

  $(function() {
	
	$('.right-content #most-popular-content-tabs').tabs();
    
    $('.right-content #staff-pick-content-tabs').tabs();
    
    // remove all ajax loads
    /*$('#stream .event').each(function() {
      com.bouncingdata.ActivityStream.loadAnalysisByAjax($(this));
    });*/

    $('.more-feed').click(function() {
      var $lastEvent = $('#stream .event:last');
      if ($lastEvent.length > 0 && !me.streamEnded && !me.streamLoading) {
        me.loadMore();
      }
    });

    if (!main.jsLoader["stream"]) {
      $(window).scroll(function() {
        var $stream = $('#stream');
        var itemNumber = $('.stream-item', $stream).length;
        // automatically load more item when scrolling down to page bottom if the stream is not empty 
        // and the number of item in stream less than 100
        if ($stream.length > 0 && itemNumber < 100 
            && ($(window).scrollTop() + 80 >= $(document).height() - $(window).height())) {
          if (!me.streamEnded && !me.streamLoading) {
            var $lastEvent = $('#stream .event:last');
            if ($lastEvent.length > 0) {
//              me.loadMore($lastEvent.attr('aid'));
            	me.loadMore();
            }
          }
        }
      });
    }

    // important! to avoid duplicate events on window object
    main.jsLoader["stream"] = true;
  });
}

/**
 * Loads more recent activity stream.
 * @param lastId the last (oldest) activity id currently in activity stream.
 */
ActivityStream.prototype.loadMore = function() {
  var me = this;
  var pageId = $('.more-feed').attr('pageid');
  var fm = $('.more-feed').attr('fm');
  var tp = $('.more-feed').attr('tp');
  
  $('#stream .feed-loading').show();
  this.streamLoading = true;
  $.ajax({
    url: ctx + '/a/more/' + pageId + '/' + fm + '/' + tp,
    success: function(result) {
      me.streamLoading = false;
      console.debug("More " + result.length + " feeds loaded!");
      // appending
      if (result.length > 0) {
        me.appendFeeds(result, pageId);
      } else {
        $('#stream .feed-loading').hide();
        $('#stream .more-feed').hide();
        me.streamEnded = true;
      }
    },
    error: function(result) {
      console.debug("Failed to fetch more feed.");
      console.debug(result);
      me.streamLoading = false;
      $('#stream .feed-loading').hide();
    }
    
  });
}

ActivityStream.prototype.appendFeeds = function(feedList, pageId) {
  var $stream = $('#stream');
  var htmlToAdd = [];
  var idsToAdd = [];
  
  var pv = '<img src=\"'+ ctx + '/resources/images/icon-private.png\" class=\"privImage\" title=\"Private\">';
  var pb = '<img src=\"'+ ctx + '/resources/images/icon-public.png\" class=\"publImage\" title=\"Public\">';
  var delImg = '<img src=\"'+ ctx + '/resources/images/trash.png\" style=\"width: 13px;\" title=\"delete\">';
  
  for (index in feedList) {
    var feed = feedList[index];
    var $feed = $.tmpl(this.$feedTemplate, {
      /*id: feed.id,
      action: feed.action,
      guid: feed.object.guid,
      username: feed.user.username,
      description: feed.object.description,
      name: feed.object.name,
      time: new Date(feed.time),
      score: feed.object.score,
      thumbnail: feed.object.thumbnail,
      commentCount: feed.object.commentCount*/
      
      id: feed.id,
      guid: feed.guid,
      username: feed.username,
      description: feed.description,
      name: feed.name,
      score: feed.score,
//      pbicon: (pageId=='streambyself' ? (feed.flag==true ? pb:'') : ''),
//      pvicon: (pageId=='streambyself' ? (feed.flag==false? pv:'') : ''),
      cmturl : (feed.classType=='Analysis' ? ctx + '/anls/' +feed.guid : '#'),
      url : (feed.classType=='Analysis' ? ctx + '/anls/' +feed.guid : ctx + '/dataset/view/' +feed.guid),
      thumbnail: ((feed.thumbnail !=null || feed.thumbnail != '') ? ctx + '/thumbnails/' + feed.thumbnail + '.jpg' : ctx + '/thumbnails/no-image.jpg'),
      commentCount: feed.commentCount
    });
    
    if (feed.score > 0) {
      $('.event-score', $feed).addClass('event-score-positive');
      $('.event-score', $feed).text('+' + $('.event-score', $feed).text());
    } else if (feed.score < 0) {
      $('.event-score', $feed).addClass('event-score-negative');
    }
    
    if(pageId == 'streambyself'){
    	feed.flag==true? $('#pbicon', $feed).append(pb) : '';
    	feed.flag==false? $('#pvicon', $feed).append(pv) : '';
    	$('#delicon', $feed).append(delImg);
    }
    
    if (feed.tags) {
      for (idx in feed.tags) {
        var tag = feed.tags[idx];
        $('.info .tag-list', $feed).append('<div class="tag-element-outer"><a class="tag-element" href="' + ctx + '/tag/' + tag.tag + '">' + tag.tag + '</a></div>'); 
      }
    }
    
    if(feed.description){
    	$('.description', $feed).append(feed.description);
    }
    
    htmlToAdd[index] = $feed.outerHtml();
    idsToAdd[index] = feed.id;
  }
  
  $('.feed-loading', $stream).hide();
  $('.stream-footer',$stream).before(htmlToAdd.join(''));
  
  /*for (index in idsToAdd) {
    var aid = idsToAdd[index];
    var $event = $('#stream .event[aid="' + aid + '"]');
    this.loadAnalysisByAjax($event);
  }*/
}

ActivityStream.prototype.loadAnalysisByAjax = function($feed) {
  var $title = $('.title a', $feed);
  var $thumb = $('.thumbnail a', $feed);
  var $comment = $('.event-footer a.comments-link', $feed)
  var name = $title.text();
  var href = $title.prop('href');
  var main = com.bouncingdata.Main;
  
  $title.click(function(e) {
    main.toggleAjaxLoading(true);
    window.history.pushState({linkId: href, type: 'anls'}, name, href);
    e.preventDefault();
  });
  
  $thumb.click(function(e) {
    main.toggleAjaxLoading(true);
    window.history.pushState({linkId: href, type: 'anls'}, name, href);
    e.preventDefault();
  });
  
  $comment.click(function(e) {
    main.toggleAjaxLoading(true);
    window.history.pushState({linkId: href, type: 'anls'}, name, href);
    e.preventDefault();
  });
  
  Spring.addDecoration(new Spring.AjaxEventDecoration({
    elementId: $title.prop('id'),
    event: "onclick",
    params: {fragments: "main-content"}
  }));
  
  Spring.addDecoration(new Spring.AjaxEventDecoration({
    elementId: $thumb.prop('id'),
    event: "onclick",
    params: {fragments: "main-content"}
  }));
  
  Spring.addDecoration(new Spring.AjaxEventDecoration({
    elementId: $comment.prop('id'),
    event: "onclick",
    params: {fragments: "main-content"}
  }));
}


com.bouncingdata.ActivityStream = new ActivityStream();
