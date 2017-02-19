package com.wtlib.base.pojo;

/**
 * 借阅回馈实体
 * 
 * @author zongzi
 * @date 2017年1月21日 下午5:59:39
 */
public class FeedBack extends BaseEntity {
	private static final long serialVersionUID = 6543108093033209276L;

	private Integer id;

	private Integer userId;

	private String desc;

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc == null ? null : desc.trim();
	}

}