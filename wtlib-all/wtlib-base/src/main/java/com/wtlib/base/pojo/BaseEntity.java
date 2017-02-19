package com.wtlib.base.pojo;

import java.util.Date;

import com.wtlib.base.pojo.request.Page;

public class BaseEntity extends Page {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -8212788099710575029L;

	private Integer creator;

	private Date createTime;

	private Integer reviser;

	private Date updateTime;

	private String dataStatus;

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getReviser() {
		return reviser;
	}

	public void setReviser(Integer reviser) {
		this.reviser = reviser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

}
