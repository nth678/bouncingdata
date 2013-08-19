<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
  com.bouncingdata.Main.loadCss(ctx + "/resources/css/bouncingdata/upload.css", "upload");
  var schema = [];
  <c:forEach items='${varUp.schema}' var='column'>
    schema.push({ 'name': '${column.name}', 'typeName': '${column.typeName}' });
  </c:forEach>
  var ticket = '${varUp.ticket}';
  if (!com.bouncingdata.Upload) {
    $.getScript(ctx + "/resources/js/bouncingdata/upload.js", function() {
      console.debug("upload.js async. loaded!");
      com.bouncingdata.Upload.initSchema(ticket, schema);
    });  
  } else {
    com.bouncingdata.Upload.initSchema(ticket, schema);
  }
</script>

<div id="main-content" class="upload-container">
  <div class="top-bar">
    <div class="left-buttons">
      <button class="close-button" id="upload-close">Discard</button>
    </div>
    <div class="schema-nav-panel">
      <a class="schema-nav" id="schema-back" href="<c:url value="/dataset/bupload" />">Back</a>
      <a class="schema-nav" id="schema-submit" href="javascript:void(0)">Submit</a>
      <div style="text-align: right;">
        <input type="checkbox" id="dataset-ispublic" checked="checked" /> &nbsp;<label for="dataset-ispublic">Public</label>
      </div>
    </div>
    <div class="progress upload-progress">
      <div class="progress-step upload-step upload-file">File</div>
      <div class="progress-step upload-step upload-schema progress-current">Schema & Description</div>
    </div>
    <div class="clear"></div>
  </div>
  <div class="clear-bar"></div>
  <div class="schema-container center-content-container">
    <div class="schema-panel-wrapper panel-wrapper">
      <div class="guideline-wrapper">
        <div class="guideline">Recommended</div>
      </div>
      <h3>1. Schema</h3>
      <div class="schema-panel schema-tabs ui-tabs" id="schema-panel">
        <ul>
          <li><a href="#schema-tab-view">View</a></li>
          <li><a href="#schema-tab-schema">Schema</a></li>
          <li><a href="#schema-tab-reference">Add Reference Doc</a></li>
        </ul>
        <div class="clear"></div>
        <div id="schema-tab-view" class="ui-tabs-hide">
          <div class="data-preview-wrapper">
            <span><strong>Note: </strong>This preview just show maximum first 100 rows from dataset.</span>
            <table id="data-preview"></table>
            <c:choose>
              <c:when test="${not empty varUp.data }">
                <script>
                  var data = ${varUp.data};
                  var $table = $('#data-preview');
                  com.bouncingdata.Utils.renderDatatable(data, $table,  { "sScrollX": "840px", "sScrollY": "400px", "bPaginate": false, "bFilter": false } );
                </script>
              </c:when>
              <c:otherwise>
                <!-- script>
                $(function() {
                  console.debug("Load datatable by Ajax...");
                  var guid = '${guid}';
                  var columns = ${columns};
                  var $table = $('#data-table');
                  com.bouncingdata.Workbench.loadDatatableByAjax(guid, columns, $table);               
                });
                </script-->
                <span>Unable to load data preview</span>  
              </c:otherwise>
            </c:choose>
          </div>
        </div>
        <div id="schema-tab-schema" class="ui-tabs-hide">
          <div class="schema-table-wrapper">
            <table id="schema-table" ticket="${varUp.ticket }">  
              <thead>
                <tr>
                  <th width="30%"><strong>Column Name</strong></th>
                  <th><strong>Data Type</strong></th>
                  <th width="55%"><strong>Description</strong></th>
                </tr>
              </thead>
              <tbody></tbody>           
            </table>
          </div>
          <br/>
          <button id="reset-schema">Reset schema</button>
        </div>
        <div id="schema-tab-reference" class="ui-tabs-hide">
          <form class="reference-form">
            <label for="web-ref">Web</label>
            <input type="text" name="web-ref" id="web-ref" />
            <h3>Or</h3>
            <label for="file-ref">Local</label>
            <input type="file" name="file-ref" id="file-ref" />
          </form>
        </div>         
      </div>  
    </div>
    <div class="description-panel-wrapper panel-wrapper">
      <div class="guideline-wrapper">
        <div class="guideline">Required</div>
      </div>
      <h3>2. Description</h3>
      <div class="description-panel">
        <div class="dataset-info">
          <label for="name">Dataset Name</label>
          <div class="input-wrapper"><input type="text" name="name" id="name" /></div>
          <br/>
          <label for="source">Source</label>
          <div class="input-wrapper"><input type="text" name="source" id="source"></div>
          <br/>
          <label for="description">Description</label>
          <textarea name="description" id="description" style="width: 100%;"></textarea>
        </div>
         
    <br>
<label for="source">Tag</label>
<div class="input-wrapper"><input type="text" name="tag" id="tag" style="width: 100%;"></div>

    </div>
  </div>
</div>

