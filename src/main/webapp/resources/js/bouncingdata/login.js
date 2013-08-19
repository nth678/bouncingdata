function initLogin() {
  $(function() {
    $('#login-tabs').tabs();
    $('input:button').button();
    $('input:submit').button();
    $('input:reset').button();
    
    var mode = $("#mode").val();
    if (mode == "register") {
      $('#login-tabs').tabs('select', 1);
    } else {
      $('#login-tabs').tabs('select', 0);
    }
    
    $('#login-tabs').bind('tabsselect', function(event, ui) {
//      debugger;
//    	if (ui.index == 1) {
//        if ($('#register-msg')) {
//          $('#register-msg').hide();
//        } else if ($('#error-msg')) {
//          $('#error-msg').hide();
//        }
//      }
      
    });
    
    $('#login-tabs').bind('tabsshow', function(event, ui) {    	
      if (ui.index == 1) {    	  
        $('#reg-username').focus();
      } else {
        $('#username').focus();
      }
    });
  });
}

/**
 * Binds real-time validate to fields
 */
function bindValidations() {
  
}

/**
 * Final validation before post data to server
 */
function postValidate() {
  
}

initLogin();