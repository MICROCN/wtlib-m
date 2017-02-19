package com.wtlib.base.pojo;

import java.util.Date;

/**
 * 单个图书实体
 * 
 * @author zongzi
 * @date 2017年1月21日 下午6:05:01
 */
public class BookSingle extends BaseEntity {
	/**
	 */
	private static final long serialVersionUID = 8557673828122550240L;

	private Integer id;

	private Integer bookBaseId;

	private String bookHash;

	private Integer currentOwner;

	private Integer lastLender;

	private Date lastLendTime;

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

	public String getBookHash() {
		return bookHash;
	}

	public void setBookHash(String bookHash) {
		this.bookHash = bookHash == null ? null : bookHash.trim();
	}

	public Integer getCurrentOwner() {
		return currentOwner;
	}

	public void setCurrentOwner(Integer currentOwner) {
		this.currentOwner = currentOwner;
	}

	public Integer getLastLender() {
		return lastLender;
	}

	public void setLastLender(Integer lastLender) {
		this.lastLender = lastLender;
	}

	public Date getLastLendTime() {
		return lastLendTime;
	}

	public void setLastLendTime(Date lastLendTime) {
		this.lastLendTime = lastLendTime;
	}

}