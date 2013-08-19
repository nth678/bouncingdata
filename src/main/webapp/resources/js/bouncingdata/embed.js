function view(vizList, dashboardPos, $container , anls) {
  var count = 0, defaultSize = 380;
  $container.empty();
  if (!dashboardPos) {
    // the vizList will be positioned automatically
    for (v in vizList) {
      var viz = vizList[v];
      viz.name = v;    
      this.addViz(0, 0, 600, 600*3/4, viz, $container, false, anls);
      count++;
    }
  }  else {
    for (v in vizList) {
      var viz = vizList[v];
      viz.name = v;
      var pos = dashboardPos[viz.guid];
      if (pos) {
        this.addViz(pos.x, pos.y, pos.w, pos.h, viz, $container, false);
      }
      else this.addViz(0, 0, 600, 600*3/4, viz, $container, false);
      count++;
    }
  }
}

function addViz(x, y, w, h, viz, $container, editMode) {
	  if (!viz || !viz.source) return;

	  var me = this;
	  var type = viz.type.toLowerCase();
	  var src = viz.source;
	  
	  var $vizContainer = $('<div class="viz-container"></div>');
	  $vizContainer.attr('guid', viz.guid).attr('n', viz.name);
	  $vizContainer.css('width', w + 'px').css('height', h + 'px');  
	  //var $vizHandle = $('<div class="viz-handle"><span class="permalink viz-permalink"><a href="" target="_blank">permalink</a></span></div>');
	  //var $vizHandle = $('<div class="viz-handle"></div>');
	  //$vizContainer.append($vizHandle);
	  
	  var $inner;
	  switch(type) {
	  case "html":
	    $inner = $('<iframe style="width:' + (w-10) + 'px; height:' + (h-15) + 'px;"></iframe>');
	    $('a', $vizHandle).attr('href', src);
	    $inner.load().appendTo($vizContainer);
	    
	    $inner.attr('src', ctx + '/' + src);
	      
	    if ($container.height() < (y + h)) {
	      $container.css('height', (y + h + 20) + "px");
	      if (editMode) me.updateRuler($container);
	    }
	    break;
	  case "png":
	    $inner = $('<img src="data:image/png;base64,' + src + '" />');
	    //$('a', $vizHandle).attr('href', 'data:image/png;base64,' + src);
	    $inner.bind('load', function() {
	      /*w = w || ($(this).width() + 10);
	      h = h || ($(this).height() + 15);*/
	      // adjust viz. container size
	      $(this).css('width', w + 'px').css('height', h + 'px');
	      if ($container.height() < (y + h)) {
	        $container.css('height', (y + h + 20) + "px");
	        if (editMode) me.updateRuler($container);
	      }
	    }).appendTo($vizContainer);
	    break;
	  default: 
	    console.debug("Unknown visualization type!");
	    return;
	  }
	  
	  $vizContainer.css('position', 'absolute')
	    .css('top', y + 'px')
	    .css('left', x + 'px')
	  
	  if (editMode) {
	    $vizContainer.css('z-index', this.zCounter[$container.attr('tabid')]++);
	    
	    $('#dashboard-wrapper', $container).append($vizContainer);
	    
	    // add info tooltip
	    var $info = $('<div class="viz-dimension-info"></div>');
	    $info.css('position', 'absolute').css('top', 0).css('left', 0)
	      .css('border', '1px solid #555555').css('background-color', 'yellow').css('z-index', 1000);
	    $info.css('padding', '2px').css('opacity', 0.8).css('font-size', '10px');
	    
	    $vizContainer.append($info);
	    $info.hide();
	    
	    var _x = $container.offset().left;
	    var _y = $container.offset().top;
	    var _w = $container.width(); // should be fixed
	    var _h = $container.height();
	    
	    /*$vizContainer.draggable({ 
	      //containment: [_x, _y, _x + _w - w, 140000],
	      containment: '#dashboard-wrapper', 
	      handle: '.viz-handle', 
	      iframeFix: true, 
	      grid: [10, 10] 
	    });*/
	    
	    $vizContainer.resizable({
	      containment: 'parent',
	      //containment: '#dashboard-wrapper-' + $container.attr('tabid'),
	      //containment: [_x, _y, _x + w, 140000],
	      grid: 10,
	      handles: "s",
	      //aspectRatio: type=="png"
	      aspectRatio: false
	    });
	      
	    $vizContainer.bind('click', function(event, ui) {
	      $(this).css('z-index', me.zCounter[$container.attr('tabid')]++);
	    })
	    .bind('dragstart', function(event, ui) {
	      $(this).css('z-index', me.zCounter[$container.attr('tabid')]++);
	      $info.show();
	    })
	    .bind('resizestart', function(event, ui) {
	      $(this).css('z-index', me.zCounter[$container.attr('tabid')]++);
	      var $currentViz = this;
	      $info.show();
	      
	      // iframe fix for resizing
	      $("iframe", $container).each(function() {
	        var $frame = $(this);
	        var $resizeIframeFix = $('<div class="resizable-iframe-fix" style="position: absolute; opacity: 0.001; z-index: 1000;"></div>');
	        if ($frame.is($currentViz.children)) {
	          $resizeIframeFix.addClass("resizable-iframe-fix-inner");
	        }
	        $resizeIframeFix.css('background', 'none repeat scroll left top #FFFFFF')
	          .css('top', $frame.offset().top)
	          .css('left', $frame.offset().left)
	          .css('width', $frame.width())
	          .css('height', $frame.height());
	        $("body").append($resizeIframeFix);
	      });
	      
	    });
	    
	    /*$vizContainer.bind('drag', function(event, ui) {
	      me.showSnapLines($(this), $container, false);
	      
	      if (ui.position.top + $(this).height() + 15 >= $container.height()) {
	        $container.css('height', ($container.height() + 15) + "px");
	        me.updateRuler($container);
	      }
	      $info.text('left: ' + Math.round(ui.position.left) + ', top: ' + Math.round(ui.position.top));
	      
	    })
	    
	    .bind('dragstop', function(event, ui) {  
	      me.hideSnapLines($container);
	      $info.hide();
	      
	      if (ui.position.top + $(this).height() >= $container.height()) {
	        $container.css('heigth', (ui.position.top + $(this).height() + 10) + "px");
	        me.updateRuler($container);
	      }
	      // post back
	      me.postback($container, "rearrange");
	    })*/
	    
	    $vizContainer.bind('resize', function(event, ui) {
	      me.showSnapLines($(this), $container, true);
	      
	      if (ui.position.top + $(this).height() + 15 >= $container.height()) {
	        $container.css('height', ($container.height() + 15) + "px");
	        me.updateRuler($container);
	      }
	      
	      var cw = $(this).width(), ch = $(this).height(); 
	      $inner.css('height', ch + "px").css('width', cw + "px");
	      //$vizContainer.draggable("option", "containment", [_x, _y, _x + _w - cw, 140000]);
	      
	      // update inner iframe-fix position
	      if ($inner[0].tagName.toLowerCase() == "iframe") {
	        var $innerIframeFix = $(".resizable-iframe-fix.resizable-iframe-fix-inner");
	        $innerIframeFix.css('top', $inner.offset().top)
	          .css('left', $inner.offset().left)
	          .css('width', $inner.width())
	          .css('height', $inner.height());
	      }
	      
	      $info.text('width: ' + Math.round($(this).width()) + ', height: ' + Math.round($(this).height()));
	    })
	    
	    .bind('resizestop', function(event, ui) {
	      me.hideSnapLines($container);
	      $info.hide();
	      var cw = $(this).width(), ch = $(this).height(); 
	      
	      //if (ui.position.top + ch >= $container.height()) {
	        $container.css('height', (ui.position.top + ch + 20) + "px");
	        me.updateRuler($container);
	      /*} else {
	        $container.css('heigth', (ui.position.top + ch + 10) + "px");
	        me.updateRuler($container);
	      }*/
	      
	      //remove resizable-iframe-fix
	      $('.resizable-iframe-fix').remove();
	      
	      // post back
	      me.postback($container, "rearrange");
	      
	      // request re-plot viz.
	      if (type == "png") {
	        var anlsGuid = $container.attr('guid');
	        var vizGuid = $(this).attr('guid');
	        if (!anlsGuid || !vizGuid) return;
	        console.debug("Request for replay plot..")
	        $.ajax({
	          url: ctx + "/visualize/replot/" + anlsGuid + "/" + vizGuid + "/png",
	          type: "get",
	          data: {
	            "w": cw,
	            "h": ch
	          },
	          success: function(res) {
	            // reload plot
	            if (res) {       
	              $inner.attr('src', 'data:image/png;base64,' + res);
	              $inner.bind('load', function() {
	                $(this).css('height', ch + "px").css('width', cw + "px");
	                $vizContainer.css('width', cw + 'px').css('height', ch + 'px');
	              });
	              console.debug("Successfully replay plot.");
	            } else {
	              console.debug("Failed to replay plot");
	            }
	            
	          }, 
	          error: function(res) {
	            console.debug("Failed to replot viz.");
	            console.debug(res);
	          }
	        });
	      }
	    });
	    
	    /*$vizContainer.hover(function() {
	      //$vizHandle.addClass('viz-handle-hover');
	      $(this).addClass('viz-container-hover');
	    },
	    function() {
	      //$vizHandle.removeClass('viz-handle-hover');
	      $(this).removeClass('viz-container-hover');
	    });*/
	  } else $container.append($vizContainer);

	}

function loadDatasetByAjax(dataPanelId) {
  var $dataPanel = $(dataPanelId);
  if ($dataPanel.length < 1) {
    $('bcdata-embedded-wrapper').html('<span>UI error. Cannot render data panel</span>');
    return;
  }
  var dsguids = '';
  $('.anls-dataset', $dataPanel).each(function() {
    dsguids += $(this).attr('dsguid') + ',';
  });
  dsguids = dsguids.substring(0, dsguids.length - 1);
  if (dsguids.length > 0) {
    setOverlay($dataPanel, true);
    $.ajax({
      url: ctx + '/public/data/m/' + dsguids,
      type: 'get',
      dataType: 'json',
      success: function(result) {
        setOverlay($dataPanel, false);
        $('.anls-dataset', $dataPanel).each(function() {
          var dsguid = $(this).attr('dsguid');
          var $table = $('table', $(this));
          var data = result[dsguid].data;
          if (data) {
            renderDatatable($.parseJSON(data), $table);
          } else if (result[dsguid].size > 0) {
            console.debug("Load datatable by Ajax...");
            var columns = result[dsguid].columns;
            var aoColumns = [];
            for (idx in columns) {
              aoColumns.push({ "mDataProp": columns[idx], "sTitle": columns[idx] });
            }
            var datatable = $table.dataTable({
              "bServerSide": true,
              "bProcessing": true,
              "sAjaxSource": ctx + "/public/data/ajax/" + dsguid,
              "aoColumns": aoColumns,
//              "bJQueryUI": true,
              "sPaginationType": "full_numbers"
            });
            
            var keys = new KeyTable( {
              "table": $table[0],
              "datatable": datatable
            });
          }
        });
      },
      error: function(result) {
        setOverlay($dataPanel, false);
        console.debug('Failed to load datasets.');
        console.debug(result);
        $dataPanel.text('Failed to load datasets.');
      }
    });
  }
}

function renderDatatable(data, $table) {
  if (!data || data.length <= 0) return;
  
  //prepare data
  var first = data[0];
  var aoColumns = [];
  for (key in first) {
    aoColumns.push({ "sTitle": key});
  }
  
  var aaData = [];
  for (index in data) {
    var item = data[index];
    var arr = [];
    for (key in first) {
      arr.push(item[key]);
    }
    aaData.push(arr);
  }
  var datatable = $table.dataTable({
    "aaData": aaData, 
    "aoColumns": aoColumns, 
//    "bJQueryUI": true,
    "sPaginationType": "full_numbers"
  });
  var keys = new KeyTable( {
    "table": $table[0],
    "datatable": datatable
  });
}

function setOverlay($panel, isActive) {
  if (isActive) {
    var $overlay = $('<div class="overlay-panel" style="position: absolute; top: 0; bottom: 0; left: 0; right: 0;"></div>');
    $overlay.css('background', 'url("' + ctx + '/resources/images/ajax-loader.gif") no-repeat 50% 10% #eee')
      .css('z-index', 10).css('background-size', '30px 30px').css('opacity', '0.8');
    if (!$panel.css('position')) {
      $panel.css('position', 'relative');
    }
    $panel.append($overlay);
  } else {
    $('div.overlay-panel', $panel).remove();
  }
}