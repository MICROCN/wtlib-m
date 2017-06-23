package com.wtlib.base.pojo;

import java.util.Date;

public class BookBaseLabelInfo {
    private Integer id;

    private Integer bookBaseId;

    private Integer labelInfoId;
    
    public BookBaseLabelInfo(){}
    
	public BookBaseLabelInfo(Integer bookBaseId, Integer labelInfoId) {
		this.bookBaseId = bookBaseId;
		this.labelInfoId = labelInfoId;
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
    
	@Override
	public String toString() {
		return "BookBaseLabelInfo [id=" + id + ", bookBaseId=" + bookBaseId
				+ ", labelInfoId=" + labelInfoId + "]";
	}
    
    
    
    
}