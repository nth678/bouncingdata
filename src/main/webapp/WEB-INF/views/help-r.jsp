<div id="help-content">
  <h3>R APIs Reference</h3>
  <div class="help-main-content">
    <div>
      <h3>Datastore</h3>
      <p>
        BouncingData provides a full, scalable SQL-supported datastore which users can store, load and query data from their analysis.
      </p>
      <p>
        This section describes the R APIs to work with BouncingData datastore. 
      </p>
      <p>Import Datastore module in R</p>
      <div class="code-outer">
        <code>library(datastore)</code>
      </div>
     
      <dl>
        <dt>datastore::<strong>load</strong>(dataset)</dt>
        <dd>
          <p>Load data from given dataset.</p>
          <p><var>dataset</var> dataset name. </p>
          <p>Returns dataframe object</p>
        </dd>
        <dt>datastore::<strong>query</strong>(query)</dt>
        <dd>
          <p>
            Run a SQL query against Bouncingdata datastore.
          </p>
          <p>Returns dataframe object</p>
          <p>
            <var>query</var> the SQL query string.
          </p>
        </dd>
        <dt>datastore::<strong>attach</strong>(attachment_name, dataframe)</dt>
        <dd>
          <p>
            Attach data as an attachment of current analysis. The attachments will appear in 'Data' tab of current analysis.
          </p>
          <p>
            <var>attachment_name</var> Name of attachment.
          </p>
          <p>
            <var>dataframe</var> R Dataframe to attach.
          </p>
        </dd>
      </dl>      
    </div>
  </div>
</div>