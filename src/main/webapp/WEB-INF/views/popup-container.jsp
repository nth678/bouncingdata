<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="popup new-anls-popup" id="new-anls-popup" title="Create new analysis">
  <form id="new-anls-form" class="new-script-form" action="">
    <fieldset>
      <div>
        <label for="anls-name">Name</label> 
        <input type="text" id="anls-name" name="name" maxlength="100" />
      </div>
      <div>
        <label for="anls-language">Language</label> 
        <select name="language" id="anls-language">
          <option value="r">R</option>
          <option value="python">Python</option>
        </select>
      </div>
      <!-- div>
        <label>Privacy</label> 
        <span> 
          <span>Public&nbsp;</span>
          <input type="radio" name="anls-privacy" value="public" id="anls-privacy-public" checked="checked" /> 
          <span>Private&nbsp;</span>
          <input type="radio" name="anls-privacy" value="private" id="anls-privacy-private" />
        </span>
      </div-->
    </fieldset>
  </form>
</div>

<div class="popup publish-dialog" id="publish-dialog" title="Publish your visualization">
  <div class="preview-pane">
    <!--Preview visualization/data-->
  </div>
  <form class="publish-form">
    <fieldset>
      <div>
        <label>Visualization</label>
        <span><strong class="title"></strong></span>
      </div>
      <div>
        <label>Your message</label>
        <textarea class="publish-message"></textarea>
      </div>
    </fieldset>
  </form>

</div>

