package com.wtlib.base.constants;

public enum CreditEnum {
	
	successReturn(1,"还书成功"),overtime(2,"创建标签成功"),createLabel(3,"创建标签成功")
	,tickLabel(4,"打标签成功"),damageBook(5,"图书破损1页"),loseBook(6,"图书丢失");

	private final Integer id;

	private final String name;
	
	private CreditEnum(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	
	
}
