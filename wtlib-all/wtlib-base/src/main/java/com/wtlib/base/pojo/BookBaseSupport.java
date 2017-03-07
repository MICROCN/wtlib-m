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

	private Integer bookBaseId;

	private String isReservateAble;

	private String isBorrowAble;

	private Integer currentLeftBookNumber;

	private Integer currentReservateNumber;

	private Integer singleBookNumber;
	
	public BookBaseSupport(){}
	
	

	public BookBaseSupport(Integer bookBaseId, String isReservateAble, String isBorrowAble,
			Integer currentLeftBookNumber, int currentReservateNumber, Integer singleBookNumber) {
		this.bookBaseId = bookBaseId;
		this.isReservateAble = isReservateAble;
		this.isBorrowAble = isBorrowAble;
		this.currentLeftBookNumber = currentLeftBookNumber;
		this.currentReservateNumber = currentReservateNumber;
		this.singleBookNumber = singleBookNumber;
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



	@Override
	public String toString() {
		return "BookBaseSupport [id=" + id + ", bookBaseId=" + bookBaseId
				+ ", isReservateAble=" + isReservateAble + ", isBorrowAble="
				+ isBorrowAble + ", currentLeftBookNumber="
				+ currentLeftBookNumber + ", currentReservateNumber="
				+ currentReservateNumber + ", singleBookNumber="
				+ singleBookNumber + "]";
	}
	
}