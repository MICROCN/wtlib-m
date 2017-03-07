package com.wtlib.base.pojo;

/**
 * 信用记录实体
 * 
 * @author zongzi
 * @date 2017年1月21日 下午5:57:36
 */
public class CreditRecord extends BaseEntity {
	private static final long serialVersionUID = -4328967727538309100L;

	private Integer id;

	private Integer userId;

	private Integer creditInfoId;

	private String creditIsPlus;

	private String creditName;

	private Integer creditBeforeValue;

	private Integer creditValue;

	private Integer creditAfterValue;

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

	public Integer getCreditInfoId() {
		return creditInfoId;
	}

	public void setCreditInfoId(Integer creditInfoId) {
		this.creditInfoId = creditInfoId;
	}

	

	public String getCreditIsPlus() {
		return creditIsPlus;
	}

	public void setCreditIsPlus(String creditIsPlus) {
		this.creditIsPlus = creditIsPlus;
	}

	public String getCreditName() {
		return creditName;
	}

	public void setCreditName(String creditName) {
		this.creditName = creditName == null ? null : creditName.trim();
	}

	public Integer getCreditBeforeValue() {
		return creditBeforeValue;
	}

	public void setCreditBeforeValue(Integer creditBeforeValue) {
		this.creditBeforeValue = creditBeforeValue;
	}

	public Integer getCreditValue() {
		return creditValue;
	}

	public void setCreditValue(Integer creditValue) {
		this.creditValue = creditValue;
	}

	public Integer getCreditAfterValue() {
		return creditAfterValue;
	}

	public void setCreditAfterValue(Integer creditAfterValue) {
		this.creditAfterValue = creditAfterValue;
	}

}