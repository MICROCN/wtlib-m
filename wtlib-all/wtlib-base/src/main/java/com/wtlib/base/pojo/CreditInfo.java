package com.wtlib.base.pojo;

/**
 * 信用描述实体
 * 
 * @author zongzi
 * @date 2017年1月21日 下午5:56:44
 */
public class CreditInfo extends BaseEntity {

	private static final long serialVersionUID = 7858840203780280153L;

	private Integer id;

	private String creditName;

	private Integer miniUserLevel;

	private String isPlus;

	private Integer creditValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreditName() {
		return creditName;
	}

	public void setCreditName(String creditName) {
		this.creditName = creditName == null ? null : creditName.trim();
	}

	public Integer getMiniUserLevel() {
		return miniUserLevel;
	}

	public void setMiniUserLevel(Integer miniUserLevel) {
		this.miniUserLevel = miniUserLevel;
	}

	public String getIsPlus() {
		return isPlus;
	}

	public void setIsPlus(String isPlus) {
		this.isPlus = isPlus == null ? null : isPlus.trim();
	}

	public Integer getCreditValue() {
		return creditValue;
	}

	public void setCreditValue(Integer creditValue) {
		this.creditValue = creditValue;
	}

}