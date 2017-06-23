package com.wtlib.base.vo;

import java.util.List;

import com.wtlib.base.pojo.LabelInfo;

public class LabelPack {
	
	LabelInfo info;
	
	Integer infoId;
	
	
	public LabelInfo getInfo() {
		return info;
	}
	public void setInfo(LabelInfo info) {
		this.info = info;
	}
	public Integer getInfoId() {
		return infoId;
	}
	public void setInfoId(Integer infoId) {
		this.infoId = infoId;
	}
	
	@Override
	public String toString() {
		return "LabelPack [info=" + info + ", infoId=" + infoId + "]";
	}

	
}
