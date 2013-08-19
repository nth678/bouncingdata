package com.bouncingdata.plfdemo.datastore.pojo.dto;



public class ExecutionResult {
  private String executionId;
  private String output;
  //private Map<String, VisualizationDetail> visualizations;
  //private Map<String, DatasetDetail> datasets;
  private int visCount;
  private int datasetCount;
  private int statusCode;
  private String message;
  
  public ExecutionResult(String executionId, String output, int visCount, int datasetCount, int statusCode, String msg) {
    this.executionId = executionId;
    this.output = output;
    //this.visualizations = visualizations;
    this.statusCode = statusCode;
    this.message = msg;
    //this.datasets = datasets;
    this.visCount = visCount;
    this.datasetCount = datasetCount;
  }
  
  public String getOutput() {
    return output;
  }
  public int getStatusCode() {
    return statusCode;
  }
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }

  public int getVisCount() {
    return visCount;
  }

  public void setVisCount(int visCount) {
    this.visCount = visCount;
  }

  public int getDatasetCount() {
    return datasetCount;
  }

  public void setDatasetCount(int datasetCount) {
    this.datasetCount = datasetCount;
  }

  public String getExecutionId() {
    return executionId;
  }

  public void setExecutionId(String executionId) {
    this.executionId = executionId;
  }

  /*public Map<String, VisualizationDetail> getVisualizations() {
    return visualizations;
  }

  public void setVisualizations(Map<String, VisualizationDetail> visualizations) {
    this.visualizations = visualizations;
  }

  public Map<String, DatasetDetail> getDatasets() {
    return datasets;
  }

  public void setDatasets(Map<String, DatasetDetail> datasets) {
    this.datasets = datasets;
  }*/
  
}
