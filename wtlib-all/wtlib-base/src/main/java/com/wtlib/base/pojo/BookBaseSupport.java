package com.wtlib.base.pojo;

import java.io.Serializable;

/**
 * ClassName: BookBaseSupport
 * @Description: 图书拓展类
 * @author zongzi
 * @date 2017年2月15日 下午12:00:21
 */
public class BookBaseSupport extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -8752769831900770143L;

	private Integer id;

	private Integer bookId;

	private String isReservateAble;

	private String isBorrowAble;

	private Integer currentLeftBookNumber;

	private Integer currentReservateNumber;

	private Integer singleBookNumber;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public String getIsReservateAble() {
		return isReservateAble;
	}

	public void setIsReservateAble(String isReservateAble) {
		this.isReservateAble = isReservateAble == null ? null : isReservateAble
				.trim();
	}

	public String getIsBorrowAble() {
		return isBorrowAble;
	}

	public void setIsBorrowAble(String isBorrowAble) {
		this.isBorrowAble = isBorrowAble == null ? null : isBorrowAble.trim();
	}

	public Integer getCurrentLeftBookNumber() {
		return currentLeftBookNumber;
	}

	public void setCurrentLeftBookNumber(Integer currentLeftBookNumber) {
		this.currentLeftBookNumber = currentLeftBookNumber;
	}

	public Integer getCurrentReservateNumber() {
		return currentReservateNumber;
	}

	public void setCurrentReservateNumber(Integer currentReservateNumber) {
		this.currentReservateNumber = currentReservateNumber;
	}

	public Integer getSingleBookNumber() {
		return singleBookNumber;
	}

	public void setSingleBookNumber(Integer singleBookNumber) {
		this.singleBookNumber = singleBookNumber;
	}
}