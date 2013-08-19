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

@Entity
@Table(name = "scrapers")
@Indexed
public class ScraperIndexed {
	@DocumentId
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "description")	
	private String description;
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "name")	
	private String name;	
	
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "createSource")
	private String createSource;
	
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "type")
	private String type;
	
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "language")
	private String language;

	
	
	/**
	 * @return the title
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param title the title to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the message
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param message the message to set
	 */
	public void setName(String message) {
		this.name = message;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the createSource
	 */
	public String getCreateSource() {
		return createSource;
	}
	/**
	 * @param createSource the createSource to set
	 */
	public void setCreateSource(String createSource) {
		this.createSource = createSource;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

}
