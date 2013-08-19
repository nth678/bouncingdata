<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
	$(function() {
	  $('#help-page-nav').tabs();
	});

</script>
<div class="main-content help-page" id="main-content">
  <h3>API References</h3>
  <div class="main-tabs" id="help-page-nav">
    <ul>
      <li><a href="#python-apis">Python</a></li>
      <li><a href="#r-apis">R</a></li>
    </ul>
    <div id="python-apis">
      <div>
        <h3>Datastore</h3>
        <p>
          BouncingData provides a full, scalable SQL-supported datastore which users can store, load and query data from their analysis.
          <br />
          This section describes the Python APIs to work with BouncingData datastore. 
        </p>
        <p>Import Datastore module in Python</p>
        <div class="code-outer">
          <code>import datastore</code>
        </div>
       
        <dl>
          <dt>datastore.<strong>store</strong>(data, dataset[, description=""])</dt>
          <dd>
            <p>Store data to a dataset</p>
            <p>
              <param>data</param> list of Python dict object.
              <param>dataset</param> dataset name.
              <param>description</param> dataset description (optional).
            </p>
          </dd>
          <dt>datastore.<strong>load</strong>(dataset)</dt>
          <dd>
            <p>
              Load data from given dataset.
            </p>
            <p>
              Returns ...
            </p>
            <p>
              <param>dataset</param> dataset name to load.
            </p>    
          </dd>
          <dt>datastore.<strong>query</strong>(query)</dt>
          <dd>
            <p>
              Run a SQL query on Bouncingdata datastore.
            </p>
            <p>Returns ...</p>
            <p>
              <param>query</param> the SQL query string.
            </p>
          </dd>
          <dt>datastore.<strong>attach</strong>(data, attachment_name[, description=""])</dt>
          <dd>
            <p>
              Attach data as an attachment of current analysis. The attachments will appear in 'Data' tab of current analysis.</dd>
            </p>
            <p>
              <param>data</param> Data to attach, it can be a list of dict objects or ...
            </p>
          <dt>datastore.<strong>attach_dataset</strong>(dataset)</dt>
          <dd>
            <p>
              Attach given dataset to current analysis.
            </p>
            <p>
              
            </p>  
          </dd>
        </dl>
        
      </div>
      <div>
        <h3>Visualization</h3>
        <p>Import Visualization module in Python</p>
        <div class="code-outer">
          <code>import visualization</code>
        </div>
        <dl>
          <dt>visualization.<strong>save_html</strong>(visualization_name, visualization_content[, description=""])</dt>
          <dd>
            <p>Save a html content as new visualization.</p>
            <p>
              <param>visualization_name</param> visualization name.
              <param>visualization_content</param> visualization content (in general, html code).
              <param>description</param> visualization description (optional).
            </p>
          </dd>
        </dl>
      </div>
    </div>
    <div id="r-apis">
      
    </div>
  </div>
</div>