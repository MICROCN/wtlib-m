package com.wtlib.base.pojo;

/**
 * 用户实体
 * 
 * @author zongzi
 * @date 2017年1月21日 下午6:00:18
 */
public class User extends BaseEntity {
	private static final long serialVersionUID = -9157449820278402502L;

	private Integer id;

	private String loginId;

	private String password;

	public User(){
		
	}
	
	public User(String loginId, String password) {
		this.loginId= loginId;
		this.password= password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId == null ? null : loginId.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", loginId=" + loginId + ", password="
				+ password + "]";
	}
	
	
}