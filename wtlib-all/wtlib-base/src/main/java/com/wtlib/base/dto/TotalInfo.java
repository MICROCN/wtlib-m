package com.wtlib.base.dto;


import com.wtlib.base.pojo.BaseEntity;
import com.wtlib.base.pojo.BookBase;
import com.wtlib.base.pojo.BookBaseSupport;

public class TotalInfo extends BaseEntity{
	
	private BookBase book;
	
	private BookBaseSupport support;
	
	public BookBase getBook() {
		return book;
	}

	public void setBook(BookBase book) {
		this.book = book;
	}

	public BookBaseSupport getSupport() {
		return support;
	}

	public void setSupport(BookBaseSupport support) {
		this.support = support;
	}

	
	
}
