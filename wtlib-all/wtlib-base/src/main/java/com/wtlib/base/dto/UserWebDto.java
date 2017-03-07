package com.wtlib.base.dto;

import com.wtlib.base.pojo.BaseEntity;

public class UserWebDto extends BaseEntity {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 5623709212932161184L;

	private String loginId;

	private Integer userLevelId;

	private Integer currentCreditValue;

	private String userName;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Integer getUserLevelId() {
		return userLevelId;
	}

	public void setUserLevelId(Integer userLevelId) {
		this.userLevelId = userLevelId;
	}

	public Integer getCurrentCreditValue() {
		return currentCreditValue;
	}

	public void setCurrentCreditValue(Integer currentCreditValue) {
		this.currentCreditValue = currentCreditValue;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
