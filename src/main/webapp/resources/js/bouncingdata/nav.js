function Nav() {
  this.selected = null;
}

Nav.prototype.init = function() {
  var $mainNav = $('#page>.main-container>.main-navigation');
  
  // binds Ajax actions, history
//  this.bindAjaxActions();
  
  // manage 'Create' sub-menu
  var $createPopup = $('.nav-hidden .nav-create-popup', $mainNav);
  var $createLink = $('a#nav-create-link', $mainNav);
  $createLink.mouseenter(function() {
    clearTimeout($(this).data('create-popup-hide-timeout'));
    var createPopupShowTimeout = setTimeout(function() {
      $createPopup.addClass("nav-create-popup-visible").show("slide");
    }, 450);
    $(this).data('create-popup-show-timeout', createPopupShowTimeout);
  }).mouseleave(function() {
    clearTimeout($(this).data('create-popup-show-timeout'));
    var createPopupHideTimeout = setTimeout(function() {
      $createPopup.hide("slide").removeClass('nav-create-popup-visible');
    }, 350);
    $(this).data('create-popup-hide-timeout', createPopupHideTimeout);

  });

  $createPopup.mouseleave(function() {
    $(this).hide("slide").removeClass('nav-create-popup-visible'); //.removeClass('nav-create-popup-visible');
  }).mouseenter(function() {
    clearTimeout($createLink.data('create-popup-hide-timeout'));
  });

  // click to the 'Create' link from navigation
  $createLink.click(function() {
    com.bouncingdata.Main.$newDialog.open();
    clearTimeout($(this).data('create-popup-show-timeout'));
    $createPopup.hide("slide").removeClass('nav-create-popup-visible');
    return false;
  });

  // the sub-menu of 'Create'
  $('.nav-create-popup .nav-create-viz, .nav-create-popup .nav-create-scraper', $mainNav).click(function() {
    if($(this).hasClass('nav-create-scraper')) {
      com.bouncingdata.Main.$newDialog.open("scraper");
    }  else {
      com.bouncingdata.Main.$newDialog.open("viz");
    }
    $createPopup.hide("slide").removeClass('nav-create-popup-visible');
  });

  $('.nav-create-popup .nav-upload-data', $mainNav).click(function() {
    com.bouncingdata.Main.$uploadDataDialog.dialog("open");
  });
}

/**
 * Declares various ajax actions for the navigation, hidden nav. item, manage history
 */
Nav.prototype.bindAjaxActions = function() {
  var $mainNav = $('#page>.main-container>.main-navigation');
  $('.nav-item.nav-page', $mainNav).each(function() {
    var $form = $('form', $(this));
    var $link = $('a.nav-item-link', $(this));

    $link.bind('click', function(e) {
      com.bouncingdata.Main.toggleAjaxLoading(true);
      if (!e.originalEvent["isBackAction"]) {
        window.history.pushState({linkId: $(this).attr('id'), type:'page'}, $('.nav-item-text', $(this)).text(), $form.attr('action'));
      }
    });

    // load page
    Spring.addDecoration(new Spring.AjaxEventDecoration({
      elementId: $link.attr('id'),
      formId: $form.attr('id'),
      event: "onclick",
      params: {fragments: "main-content"}
    }));

  });

  // register ajax loading handler
  Spring.addDecoration(new Spring.AjaxEventDecoration({
    elementId: 'hiddenLinkForAjax',
    event: "onclick",
    params: {fragments: "main-content"}
  }));

  $('#hiddenLinkForAjax').click(function(e) {
    com.bouncingdata.Main.toggleAjaxLoading(true);

    // only push state to history if this event does not originate from the 'Back' button
    if (!e.originalEvent["isBackAction"]) {
      window.history.pushState({linkId: $(this).prop('href')}, null, $(this).prop('href'));
    }
    return false;
  });

  // hide the ajax loading status when received response
  dojo.connect(Spring.RemotingHandler.prototype, 'handleResponse', null, function(res) {
    com.bouncingdata.Main.toggleAjaxLoading(false);
  });

  // manage history
  window.onpopstate = function(e) {
    if(e.state) {
      var popped = ('state' in window.history && window.history.state !== null);
      var linkId = e.state.linkId;
      var type = e.state.type;
      console.debug('Pop state: linkId = ' + linkId + ', type = ' + type);
      var event;
      if (type == 'page') {
        var $link = $('a#' + linkId);
        if (!linkId || $link.length < 1) {
          location.reload();
          return;
        }
        // create a native event on nav. link
        if (document.createEvent) {
          event = document.createEvent("HTMLEvents");
          event.initEvent("click", false, true);
          event["isBackAction"] = true;
          $link[0].dispatchEvent(event);
        } else { // IE
          event = document.createEventObject();
          event.eventType = "click";
          event["isBackAction"] = true;
          $link[0].fireEvent("on" + event.eventType, event);
        }
      } else {
        com.bouncingdata.Nav.fireAjaxLoad(linkId, true);
      }

    } else {
      // TODO: have problem with Chrome, which always fire onpopstate when loading page
      //location.reload();
      console.debug("Onpopstate fired with no state.");
    }
  }
}

/**
 * Sets current selected item
 * @param type one of 'page', 'anls', 'data', 'search'
 * @param ref the reference string, it's the page name ('create', 'stream', ...) in case of type 'page',</br> 
 * guid in case of type 'anls' or 'data', query string if type is 'search'
 */
Nav.prototype.setSelected = function(type, ref) {
  var $oldSelected = $('#page>.main-container>.main-navigation div.nav-item-selected');
  if ($oldSelected) {
    $oldSelected.removeClass('nav-item-selected');
    var oldPageId = $('a.nav-item-link', $oldSelected).prop('id');
    if (oldPageId == "nav-create-link" && this.selected.type == 'page' && this.selected.ref == 'create') {
      com.bouncingdata.Workbench.dispose();
    }      
  }
  
  this.selected = {
    'type': type,
    'ref': ref
  };
  
  if (type == 'page') {
    $('#page>.main-container>.main-navigation div.nav-item#nav-' + ref).addClass('nav-item-selected');
  } else {
    $('#page>.main-container>.main-navigation div.nav-item#nav-stream').addClass('nav-item-selected');
  }
}

/**
 * Fires an Ajax load event, in fact, a click event is dispatched on the hidden link.
 * If successful, the main content will be loaded.
 * @param link the link to load
 * @param isBack is this event originated from the 'Back' button?
 */
Nav.prototype.fireAjaxLoad = function(link, isBack) {
  var $hiddenLink = $('#hiddenLinkForAjax');
  $hiddenLink.prop('href', link);
  var event;
  if (document.createEvent) {
    event = document.createEvent("HTMLEvents");
    event.initEvent("click", false, true);
    event["isBackAction"] = isBack;
    $hiddenLink[0].dispatchEvent(event);
  } else { //IE
    event = document.createEventObject();
    event.eventType = "click";
    event["isBackAction"] = isBack;
    $hiddenLink[0].fireEvent("on" + event.eventType, event);
  }
}

/**
 * Fires an event to open workbench
 */
Nav.prototype.openWorkbench = function() {
  //create a native event on workbench navigation link
  /*var $link = $('a#nav-create-link');
  if (document.createEvent) {
    event = document.createEvent("HTMLEvents");
    event.initEvent("click", false, true);
    event["isBackAction"] = false;
    $link[0].dispatchEvent(event);
  } else { // IE
    event = document.createEventObject();
    event.eventType = "click";
    event["isBackAction"] = false;
    $link[0].fireEvent("on" + event.eventType, event);
  }*/

  this.fireAjaxLoad(ctx + '/create', false);
}

/**
 * Toogles the main navigation and expand the main content area
 */
Nav.prototype.toggle = function(speed) {
  var $nav = $('#page .main-container .main-navigation');
  var $mainContainer = $('#page .main-container .main-content-container');
  if (speed) {
    $nav.toggle(speed, function() {  });
  } else {
    $nav.toggle('fast', function() {  });
    
  }
}

com.bouncingdata.Nav = new Nav();