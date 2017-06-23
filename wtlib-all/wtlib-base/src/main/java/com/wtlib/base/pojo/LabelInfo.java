package com.wtlib.base.pojo;

import java.util.Date;

public class LabelInfo {
    private Integer id;

    private Integer userId;

    private String value;

    private Integer creator;

    private Date createTime;

    private String dataStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

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

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus == null ? null : dataStatus.trim();
    }

	@Override
	public String toString() {
		return "LabelInfo [id=" + id + ", userId=" + userId + ", value="
				+ value + ", creator=" + creator + ", createTime=" + createTime
				+ ", dataStatus=" + dataStatus + "]";
	}
    
    
}