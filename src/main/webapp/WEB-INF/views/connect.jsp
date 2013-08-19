<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script type="text/javascript">
	$(function() {
	  com.bouncingdata.Main.loadCss(ctx + "/resources/css/bouncingdata/connect.css", "connect");

	  if (!com.bouncingdata.Connect) {
	    $.getScript(ctx + "/resources/js/bouncingdata/connect.js", function() {
	    	console.debug("connect.js async. loaded!");
	    	com.bouncingdata.Connect.init();
	    });  
	  } else {
	    com.bouncingdata.Connect.init();
	  }
	});
</script>

<div id="main-content" class="main-content connect-page">
  <div class="right-content"></div>
  <div class="center-content">
    <div class="center-content-wrapper">
      <div class="connect-page-tabs ui-tabs">
        <ul>
          <li><a href="#find-friends">Find friends</a></li>
          <li><a href="#connections">Your connections</a></li>
        </ul>
        <div class="find-friends ui-tabs-hide" id="find-friends">
          <form class="search-form">
            <label for="friend-name">Your friend name</label>
            <input id="friend-name" type="text" maxlength="100" />
            <input type="submit" value="Find" class="find-friends-submit" />
          </form>
          <h3>Results</h3>
          <div class="find-friends-result" id="find-friends-result">
            <span>No result here.</span>
          </div>
        </div>
        <div class="connections ui-tabs-hide" id="connections">
          <div class="followings">
            <h3>You are following</h3>
            <div class="following-list" id="following-list">
              <span>You haven't followed anyone.</span>
            </div>
          </div>
          <div class="followers">
            <h3>Your followers</h3>
            <div class="follower-list" id="follower-list">
              <span>You have 0 follower.</span>
            </div>
          </div>
          <div class="clear"></div>
        </div>
      </div>
    </div>
  </div>
</div>