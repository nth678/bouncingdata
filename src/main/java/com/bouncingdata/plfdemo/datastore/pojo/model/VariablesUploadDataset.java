package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bouncingdata.plfdemo.util.dataparsing.DatasetColumn;

public class VariablesUploadDataset {

	private MultipartFile file;
	private String filename;
	private String fileUrl;
	private String firstRowAsHeader;
	private String delimiter;
	private String data;
	private List<DatasetColumn> schema;
	private String ticket;
	
	public VariablesUploadDataset() {
		
	}
	
	public VariablesUploadDataset(MultipartFile file,
								   String filename,
								   String fileUrl,
								   String firstRowAsHeader,
								   String delimiter,
								   String data,
								   List<DatasetColumn> schema,
								   String ticket){
		this.file = file;
		this.filename = filename;
		this.fileUrl = fileUrl;
		this.firstRowAsHeader = firstRowAsHeader;
		this.delimiter = delimiter;
		this.data = data;
		this.schema = schema;
		this.ticket = ticket;
	}
	
	public String getFilename() {
		return filename;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFirstRowAsHeader() {
		return firstRowAsHeader;
	}

	public void setFirstRowAsHeader(String firstRowAsHeader) {
		this.firstRowAsHeader = firstRowAsHeader;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public List<DatasetColumn> getSchema() {
		return schema;
	}

	public void setSchema(List<DatasetColumn> schema) {
		this.schema = schema;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
}
