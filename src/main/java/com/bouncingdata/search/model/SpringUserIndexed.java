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
@Table(name = "spring_users")
@Indexed
public class SpringUserIndexed {
	@DocumentId
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "last_name")
	private String lastName;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "first_name")
	private String firstName;
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "email")
	private String email;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "password")
	private String password;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
	@Column(name = "user_name")
	private String userName;

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
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setLastName(String action) {
		this.lastName = action;
	}

	/**
	 * @return the message
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setFirstName(String message) {
		this.firstName = message;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
