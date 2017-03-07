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

	private Integer userLevelId;

	private Integer currentCreditValue;

	private String userName;

	public UserInfo(){}
	
	public UserInfo(Integer userId, String userName) {
		this.userId = userId;
		this.userName = userName;
		userLevelId = 1;
		currentCreditValue = 0;
	}

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserLevelId() {
		return userLevelId;
	}

	public void setUserLevelId(Integer userLevelId) {
		this.userLevelId = userLevelId;
	}

}