package com.wtlib.base.dto;

import java.util.ArrayList;
import java.util.List;

import com.wtlib.base.pojo.BaseEntity;
import com.wtlib.base.pojo.BookBase;
import com.wtlib.base.pojo.BookBaseSupport;
import com.wtlib.base.pojo.LabelInfo;

public class SupportWebDto extends BaseEntity{
	
	private BookBase book;
	
	private List<LabelInfo> labelList;
	
	private BookBaseSupport support;
	
	private List<UserWebDto> userDto;

	public BookBase getBook() {
		return book;
	}

	public void setBook(BookBase book) {
		this.book = book;
	}

	
	public List<LabelInfo> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<LabelInfo> labelList) {
		this.labelList = labelList;
	}

	
	public BookBaseSupport getSupport() {
		return support;
	}

	public void setSupport(BookBaseSupport support) {
		this.support = support;
	}

	public List<UserWebDto> getUserDto() {
		return userDto;
	}

	public void setUserDto(List<UserWebDto> userDto) {
		this.userDto = userDto;
	}

	@Override
	public String toString() {
		return "SupportWebDto [book=" + book + ", labelList=" + labelList
				+ ", support=" + support + ", userDto=" + userDto + "]";
	}
	
	
}
