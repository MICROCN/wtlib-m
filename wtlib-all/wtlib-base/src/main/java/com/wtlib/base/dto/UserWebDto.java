package com.wtlib.base.dto;

import com.wtlib.base.pojo.BaseEntity;

public class UserWebDto extends BaseEntity{

	private String loginId;
	
	private String currentCreditLevel;

	private Integer currentCreditValue;
	
	private String username;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getCurrentCreditLevel() {
		return currentCreditLevel;
	}

	public void setCurrentCreditLevel(String currentCreditLevel) {
		this.currentCreditLevel = currentCreditLevel;
	}

	public Integer getCurrentCreditValue() {
		return currentCreditValue;
	}

	public void setCurrentCreditValue(Integer currentCreditValue) {
		this.currentCreditValue = currentCreditValue;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	
}
