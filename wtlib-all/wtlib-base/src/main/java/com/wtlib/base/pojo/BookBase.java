package com.wtlib.base.pojo;

import java.math.BigDecimal;

/**
 * 基本图书实体 用来描述抽象的图书 包括了一个种类的图书
 * 
 * @author zongzi
 * @date 2017年1月21日 下午6:03:40
 */
public class BookBase extends BaseEntity {

	private static final long serialVersionUID = 9168395443616353176L;

	private Integer id;

	private String bookTitle;

	private String bookCoverUrl;

	private String bookDesc;

	private String bookWriter;

	private String bookPublisher;

	private Integer bookPageNum;

	private BigDecimal bookPrice;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle == null ? null : bookTitle.trim();
	}

	public String getBookCoverUrl() {
		return bookCoverUrl;
	}

	public void setBookCoverUrl(String bookCoverUrl) {
		this.bookCoverUrl = bookCoverUrl == null ? null : bookCoverUrl.trim();
	}

	public String getBookDesc() {
		return bookDesc;
	}

	public void setBookDesc(String bookDesc) {
		this.bookDesc = bookDesc == null ? null : bookDesc.trim();
	}

	public String getBookWriter() {
		return bookWriter;
	}

	public void setBookWriter(String bookWriter) {
		this.bookWriter = bookWriter == null ? null : bookWriter.trim();
	}

	public String getBookPublisher() {
		return bookPublisher;
	}

	public void setBookPublisher(String bookPublisher) {
		this.bookPublisher = bookPublisher == null ? null : bookPublisher
				.trim();
	}

	public Integer getBookPageNum() {
		return bookPageNum;
	}

	public void setBookPageNum(Integer bookPageNum) {
		this.bookPageNum = bookPageNum;
	}

	public BigDecimal getBookPrice() {
		return bookPrice;
	}

	public void setBookPrice(BigDecimal bookPrice) {
		this.bookPrice = bookPrice;
	}

}