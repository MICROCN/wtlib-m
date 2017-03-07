package com.wtlib.base.pojo;

import java.util.Date;

public class BookBaseLabelInfo {
    private Integer id;

    private Integer bookBaseId;

    private Integer labelInfoId;

    private Integer creator;

    private Date createTime;

    private Integer reviser;

    private Date updateTime;

    private String dataStatus;
    

    public BookBaseLabelInfo(){}
    
    public BookBaseLabelInfo(Integer id, Integer creator) {
    	this.id = id;
    	this.creator = creator;
    }

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookBaseId() {
        return bookBaseId;
    }

    public void setBookBaseId(Integer bookBaseId) {
        this.bookBaseId = bookBaseId;
    }

    public Integer getLabelInfoId() {
        return labelInfoId;
    }

    public void setLabelInfoId(Integer labelInfoId) {
        this.labelInfoId = labelInfoId;
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
        this.dataStatus = dataStatus == null ? null : dataStatus.trim();
    }

	@Override
	public String toString() {
		return "BookBaseLabelInfo [id=" + id + ", bookBaseId=" + bookBaseId
				+ ", labelInfoId=" + labelInfoId + ", creator=" + creator
				+ ", createTime=" + createTime + ", reviser=" + reviser
				+ ", updateTime=" + updateTime + ", dataStatus=" + dataStatus
				+ "]";
	}
    
    
}