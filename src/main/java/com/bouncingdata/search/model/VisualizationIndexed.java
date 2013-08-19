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
@Table(name = "visualizations")
@Indexed
public class VisualizationIndexed {
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
	@Column(name = "type")
	private String type;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the action
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setDescription(String action) {
		this.description = action;
	}

	/**
	 * @return the message
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setName(String message) {
		this.name = message;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
