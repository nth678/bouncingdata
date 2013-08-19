<div id="help-content" class="help-content">
  <h3>Python APIs References</h3>
  <div class="help-main-content">
    <div>
      <h3>Datastore</h3>
      <p>
        BouncingData provides a full, scalable SQL-supported datastore which users can store, load and query data from their analysis.
      </p>
      <p>
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
          <p><var>data</var> list of Python dict object.</p>
          <p><var>dataset</var> dataset name. </p>
          <p><var>description</var> dataset description (optional).</p>
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
            <var>dataset</var> dataset name to load.
          </p>    
        </dd>
        <dt>datastore.<strong>query</strong>(query)</dt>
        <dd>
          <p>
            Run a SQL query on Bouncingdata datastore.
          </p>
          <p>Returns ...</p>
          <p>
            <var>query</var> the SQL query string.
          </p>
        </dd>
        <dt>datastore.<strong>attach</strong>(data, attachment_name[, description=""])</dt>
        <dd>
          <p>
            Attach data as an attachment of current analysis. The attachments will appear in 'Data' tab of current analysis.
          </p>
          <p>
            <var>data</var> Data to attach, it can be a list of dict objects or ...
          </p>
        </dd>
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
          <p><var>visualization_name</var> visualization name.</p>
          <p><var>visualization_content</var> visualization content (in general, html code).</p>
          <p><var>description</var> visualization description (optional).</p>
        </dd>
      </dl>
    </div>
  </div>
</div>