<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
  com.bouncingdata.Main.loadCss(ctx + "/resources/css/bouncingdata/browser.css", "browser");
  com.bouncingdata.Main.loadCss(ctx + "/resources/css/bouncingdata/workbench.css", "workbench");

  if (!com.bouncingdata.Browser) {
    $.getScript(ctx + "/resources/js/bouncingdata/browser.js", function() {
      console.debug("browser.js async. loaded!");
      com.bouncingdata.Browser.init();
    });
  } else {
    com.bouncingdata.Browser.init();
  }

  // supports async. load js but we really should pre-load workbench.js from the layout.jsp
  if (!com.bouncingdata.Workbench) {
    $.getScript(ctx + "/resources/js/bouncingdata/workbench.js", function() {
    	console.debug("workbench.js async. loaded!");
    	com.bouncingdata.Workbench.init();
    });  
  } else {
    com.bouncingdata.Workbench.init();
  }
</script>

<div id="main-content" class="workbench-container">
  <div class="workbench-browser right-content" id="workbench-browser">
    <div class="search-container">
      <div id="search-form" class="search-form">
        <input type="text" class="search-input" id="query" name="query" placeholder="Search global stuff..." />
        <select class="search-criteria">
          <option value="global">All</option>
          <option value="mystuff">My stuff</option>
        </select>
      </div>
      <div class="clear"></div>
    </div>
    <div class="browser-container">
      <div class="browser-tabs ui-tabs" id="browser-tabs">
        <ul>
          <li><a href="#browser-mystuff">My Stuff</a></li>
        </ul>
        <div id="browser-mystuff" class="browser-mystuff">
          <div class="browser-tab-content">
            <div class="dataset-list-panel browser-tab-panel">
            <h4>Datasets</h4>
            <div id="dataset-list"></div>
            </div>
            <div class="browser-separator"></div>
            <div class="analysis-list-panel browser-tab-panel">
              <h4>Analyses</h4>
              <div id="analysis-list"></div>
            </div>
            <div class="browser-separator"></div>
            <div class="scraper-list-panel browser-tab-panel">
              <h4>Scrapers</h4>
              <div id="scraper-list"></div>
            </div>
            <div class="show-all"><a id="show-all-button" href="javascript:void(0)">Back</a></div>
          </div>
          <div class="clear"></div>
          <div class="browser-buttons">
            <a href="javascript:void(0);" class="refresh-button" title="Reload stuffs">Reload</a>
          </div>
        </div>      
          
      </div>
    </div>
  </div>
  <div class="workbench-ide">  
    <div class="center-content-wrapper">
      <div class="top-rule"></div>
      <div class="workbench-toolbar">
        <span id="app-actions-toolbar" class="app-actions ui-widget-header ui-corner-all">
          <button class="app-action" id="new-app">New</button>
          <button class="app-action" id="copy-app">Clone</button>
          <button class="app-action" id="save-app">Save</button>
          <button class="app-action" id="run-app">Run</button>
          <button class="app-action" id="publish-app">Publish</button>
          <button class="app-action" id="view-app">View</button>
          <button class="app-action" id="upload-data">Upload</button>
        </span>
      </div>
      <!-- workbench main tabs layout -->
      <div class="workbench-main-tabs ui-tabs" id="workbench-main-tabs">
        <ul class="workbench-main-tabs-bar">
        </ul>
      </div> 
      
      <div class="popup save-app-dialog" id="save-app-dialog" title="Save your application">
        <form>
          <fieldset>
            <label>Analysis name</label>
            <input type="text" class="app-name" maxlength="40" /><br>
            <label>Language</label>
            <select class="app-language">
              <option value="python">Python</option>
              <option value="r">R</option>
            </select> <br>
            <label>Description</label>
            <textarea rows="3" class="app-description" style="resize: none;"></textarea><br>
            <label>Privacy</label>
            <span class="app-privacy">
              <span>Public&nbsp;</span><input value="public" class="app-privacy-public" type="radio" name="app-privacy" /> &nbsp;&nbsp;
              <span>Private&nbsp;</span><input value="private" class="app-privacy-private" type="radio" name="app-privacy" />  
            </span><br>     
            <label>Tags</label>
            <input type="text" class="app-tags" title="Separate tags by comma"/><br>
          </fieldset>
        </form>
      </div>
      <!--div class="popup publish-dialog" id="publish-dialog" title="Publish your analysis">
        <form>
          <fieldset>
            <label for="anls-name">Analysis</label>
            <span id="anls-name"></span><br>
            <label for="publish-msg">Message</label>
            <textarea id="publish-msg" rows="3"></textarea>
          </fieldset>
        </form>
      </div-->
    </div> 
  </div>
</div>