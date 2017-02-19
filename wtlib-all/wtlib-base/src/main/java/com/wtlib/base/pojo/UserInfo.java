package com.wtlib.base.pojo;

/**
 * 用户附加信息实体
 * 
 * @author zongzi
 * @date 2017年1月21日 下午6:02:04
 */
public class UserInfo extends BaseEntity {
	private static final long serialVersionUID = 8929791798236546341L;

	private Integer id;

	private Integer userId;

	private Integer currentCreditLevel;

	private Integer currentCreditValue;
	
	private String username;

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

	public Integer getCurrentCreditLevel() {
		return currentCreditLevel;
	}

	public void setCurrentCreditLevel(Integer currentCreditLevel) {
		this.currentCreditLevel = currentCreditLevel;
	}

	
}