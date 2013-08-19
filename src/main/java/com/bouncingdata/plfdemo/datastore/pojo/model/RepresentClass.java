package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Comparator;
import java.util.Date;
import java.util.Set;

public class RepresentClass implements Comparator<RepresentClass> {

	private int id;
	private int score;
	private Set<Tag> tags;
	private String thumbnail;
	private Date createAt;
	private int commentCount;
	private String description;
	private String guid;
	private String username;
	private String name;
	private String classType;
	private boolean flag;

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Override
	public int compare(RepresentClass o1, RepresentClass o2) {

		if (o1.getCreateAt().compareTo(o2.getCreateAt()) > 0)
			return -1;
		else if (o1.getCreateAt().compareTo(o2.getCreateAt()) < 0)
			return +1;
		else
			return 0;
	}
}
