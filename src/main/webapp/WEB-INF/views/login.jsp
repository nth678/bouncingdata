<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Bouncing Data | User Login</title>
  <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bouncingdata/login.css" />" />
  <link type="text/css" href="<c:url value="/resources/css/jquery-ui/smoothness/jquery-ui-1.8.20.custom.css" />" rel="stylesheet" />
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-1.7.2.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/jquery/jquery-ui-1.8.20.custom.min.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/bouncingdata/login.js" />"></script>
  <style>
    .error-block, .message-block-error {
      color: #ff0000;
      border: 0 none;
      padding: 8px;
    }
    .message-block {
      color: blue;
      border: 0 none;
      padding: 8px;  
    }
    label, input { display:block; }
    input.text { margin-bottom:12px; width:95%; padding: .4em; }
    fieldset { padding:0; border:0; margin-top:25px; }
    .validateTips { border: 1px solid transparent; padding: 0.3em; }
    .ui-dialog .ui-state-error { padding: .3em; }
    
  </style>
    <script>
	  $(function() {
	    var name = $( "#name" ),
	      email = $( "#email" ),
	      allFields = $( [] ).add( name ).add( email ),
	      tips = $( ".validateTips" );
	 
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
	 
	    function checkRegexp( o, regexp, n ) {
	      if ( !( regexp.test( o.val() ) ) ) {
	        o.addClass( "ui-state-error" );
	        updateTips( n );
	        return false;
	      } else {
	        return true;
	      }
	    }
	 
	    $( "#dialog-form" ).dialog({
	      autoOpen: false,
	      height: 300,
	      width: 350,
	      modal: true,
	      buttons: {
	        "Get password": function() {
	          var bValid = true;
	          allFields.removeClass( "ui-state-error" );
	 
	          bValid = bValid && checkLength( name, "username", 3, 16 );
	          bValid = bValid && checkLength( email, "email", 6, 80 );
	 
	          bValid = bValid && checkRegexp( name, /^[a-z]([0-9a-z_])+$/i, "Username may consist of a-z, 0-9, underscores, begin with a letter." );
	          // From jquery.validate.js (by joern), contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
	          bValid = bValid && checkRegexp( email, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "eg. exam@gmail.com" );
	 
	          if ( bValid ) {
	        	  $("#progress-stt").show();
	        	  $.ajax({
						type : "post",
						url : '<c:url value="/auth/resetpasswd"/>' ,
						data : {
							"user-email" : email.val(),
							"user-name" : name.val()
						},
						success : function(res) {
							$( "#dialog-form" ).dialog( "close" );
							$("#progress-stt").hide();
							
							if (res['code'] < 0) {
								window.alert("Failed to reset password.\nError: " + res['message']);
						        return;
							}else{
								$("#email-text").html(res['message']);
								$( "#dialog-message" ).dialog( "open" );
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
	 
	    $( "#forgot-user-password" ).click(function() {
	        $( "#dialog-form" ).dialog( "open" );
	    });
	    
	    $("#dialog-message").dialog({
	   	  autoOpen: false,
	      modal: true,
	      buttons: {
	        Ok: function() {
        	  $("#email-text").html("");
	          $( this ).dialog( "close" );
	        }
	      }
	    });
	  });
	  
	 
  </script>
</head>
<body onload='document.f.j_username.focus();'>
  <div class="page" id="page">
    <div class="top-content">
      <div class="logo">
      
      </div>
      <div class="title">
        <h1>Bouncing Data</h1>
      </div>
      <div class="top-horizontal-rule"></div>
    </div>
    <div class="login-main-container">
      <div class="login-tabs" id="login-tabs">
        <ul>
          <li><a href="#login">Login</a></li>
          <li><a href="#signup">Sign up</a></li>
        </ul>
        <div id="login">
          <h4 style="margin-bottom: 2px;">Login to your account</h4>
          <div class="message">
            <c:if test="${not empty error}">
              <div class="error-block" id="error-msg">
                Your login attempt was not successful, try again.
              </div>
            </c:if>
          </div>
          <div class="login-form">
            <form name='f' action="<c:url value='/auth/security_check' />" method='post'> 
                <label>Username</label>
                <input class="input-field" type='text' name='j_username' id='username' maxlength="40"></input>
                <label>Password</label>
                <input class="input-field" type='password' name='j_password' id='password' maxlength="100"></input>
                <div class="login-actions">
                  <input type="submit" name="submit" value="Login" onclick="if ($('#username').val().length <= 0 || $('#password').val().length <= 0) return false; else return true;" />
                  <a href="#" id="forgot-user-password">Forgot your password?</a>
                </div>            
              <div class="clear"></div>
            </form>
          </div>
        </div>
        <div id="signup">
          <h4>Register new account</h4>       
          <input type="hidden" value="${mode}" id="mode"/>
          <div class="message">
            <c:if test="${not empty regResult}">
              <div class="message-block" id="register-msg">
                <span>${regResult.message}</span>
                <c:if test="${regResult.statusCode < 0}">
                  <script type="text/javascript">
                  $(function() {
                    $('#reg-username').val('${regResult.username}');
                  	$('#reg-email').val('${regResult.email}');
                  	$('#register-msg').removeClass('message-block');
                  	$('#register-msg').addClass('message-block-error');
                  });
                  </script>
                </c:if>
              </div>
            </c:if>
          </div>
          <div class="register-form">
            <form action="<c:url value='/auth/register' />" method="post"> 
              <div>
                <label for='register-username'>Username</label>
                <input class="input-field" type='text' name='username' id='reg-username' maxlength="40"></input>
              </div>
              <div>  
                <label for='reg-password'>Password</label>
                <input class="input-field" type='password' name='password' id='reg-password' maxlength="100"></input>
              </div>
              <div>  
                <label for='reg-password2'>Re-type Password</label>
                <input class="input-field" type='password' name='password2' id='reg-password2' maxlength="100"></input>
              </div>
              <div>
                <label for='reg-email'>Email</label>
                <input class="input-field" type='text' name='email' id='reg-email' maxlength="100"></input>
              </div>
              
              <div class="reg-actions">
                <input type="submit" name="submit" value="Submit" onclick="if ($('#reg-username').val().length <= 0 || $('#reg-password').val().length <= 0 || $('#reg-email').val().length <= 0) return false; else return true;" />
                <input type="reset" name="reset" value="Reset"></input>
              </div>            
              <div class="clear"></div>
            </form>
          </div>
        </div>
    <!-- vinhpq  
        <div id="forgot-password" style="display: none;">
          <h4>Reset your password</h4>
          <div class="message">
          
          </div>
          <div class="forgot-password-form">
            <form action="" method="post">
              <div>
                <label for='user-email'>Please enter your registered email:</label>
                <input class="input-field" type='text' name='user-email' id='user-email' maxlength="100"></input>
              </div>
            </form>
          </div>
        </div>
    -->
      </div>
    </div>
    <div class="footer">
      <div class="footer-content">
        <a href="#" class="footer-link">Term of Service</a>
        <a href="#" class="footer-link">Privacy Policy</a>
        <a href="#" class="footer-link">Help</a>
        <div class="footer-right">
          <span>Copyright &copy;2012 bouncingdata.com. All rights reserved.</span>
        </div>
      </div>
    </div>
  </div>  
  
  	<div id="dialog-form" title="Reset your password">
	  <p class="validateTips">All form fields are required.</p>
	  <form>
	  <fieldset>
	    <label for="name">Name</label>
	    <input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" />
	    <label for="email">Email</label>
	    <input type="text" name="email" id="email" value="" class="text ui-widget-content ui-corner-all" />	    
	  	
	  	<div id="progress-stt" name="progress-stt" style="display: none;">
	  		<img src="<c:url value="/resources/images/ajax-loader.gif"/>" height="25" width="25">
	  	</div>
	  	 
	  </fieldset>
	  </form>
	</div>
	<div id="dialog-message" title="Reset your password">
	  <p>
	    <span class="ui-icon ui-icon-circle-check" style="float: left; margin: 0 7px 50px 0;"></span>
	    A confirm mail is sent successfully to your email. 
	  </p>
	  <p>
	    Check email <u><b id="email-text">...</b></u> to get new password.
	  </p>
	</div>
</body>
</html>