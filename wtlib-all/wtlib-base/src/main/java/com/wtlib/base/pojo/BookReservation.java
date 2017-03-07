package com.wtlib.base.pojo;

/**
 * 图书预约实体
 * 
 * @author zongzi
 * @date 2017年1月21日 下午5:54:06
 */
public class BookReservation extends BaseEntity {
	private static final long serialVersionUID = -8455033077397948605L;

	private Integer id;

	private Integer userId;

	private Integer bookId;

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

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

}