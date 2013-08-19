package com.bouncingdata.search.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 * The persistent class for the analyses database table.
 * 
 */
@Entity
@Table(name = "analyses")
@Indexed
public class AnalyseIndexed {
	@DocumentId
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "createSource")
	private String createSource;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "description")
	private String description;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "name")
	private String name;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "tags")
	private String tags;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "type")
	private String type;

	public AnalyseIndexed() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreateSource() {
		return this.createSource;
	}

	public void setCreateSource(String createSource) {
		this.createSource = createSource;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTags() {
		return this.tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}