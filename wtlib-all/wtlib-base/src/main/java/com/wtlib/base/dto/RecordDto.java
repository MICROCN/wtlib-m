package com.wtlib.base.dto;

import java.util.List;

import com.wtlib.base.pojo.BorrowRecord;

public class RecordDto {
	
	Integer id;
	
	List<BorrowRecord> list;
	
	String bookTitle;

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<BorrowRecord> getList() {
		return list;
	}

	public void setList(List<BorrowRecord> list) {
		this.list = list;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	@Override
	public String toString() {
		return "RecordDto [id=" + id + ", list=" + list + ", bookTitle="
				+ bookTitle + "]";
	}
	
	
}
