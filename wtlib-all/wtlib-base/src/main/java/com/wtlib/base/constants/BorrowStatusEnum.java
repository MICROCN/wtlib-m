package com.wtlib.base.constants;

public enum BorrowStatusEnum {
	BORROW("001","借阅状态"),OVER_TIME("002","超时未还"),GIVE_BACK("003","已归还"),TICK_LABEL("004","已评价");
	
	String code;
	String value;
	BorrowStatusEnum(String code,String value){
		this.code = code;
		this.value = value;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
