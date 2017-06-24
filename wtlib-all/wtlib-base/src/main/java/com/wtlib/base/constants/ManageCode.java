package com.wtlib.base.constants;

import org.apache.commons.lang.StringUtils;

public enum ManageCode {
	CONTROLLER("1000", "控制台"), ADD_BOOK("1001", "添加图书"),BOOK_LIST("1002", "图书列表") ,
	BORROW_RECORD("1003", "归还借阅预定记录"),BOOK_LOSS("1004","图书损失记录"),LABEL_RECORD("1005","图书标签记录")
	,ADD_USER("1006","添加用户"),USER_LIST("1007","用户列表"),CREDIT_SETTINGS("1008","信用值设置"),
	CREDIT_RECORD("1009","信用值记录"),ADVICE("1010","意见反馈");
	
	private String code;

	private final String value;

	ManageCode(String code, String v) {
		this.code = code;
		value = v;
	}

	public String value() {
		return value;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	public static DataStatusEnum fromCode(String code) {
		for (DataStatusEnum c : DataStatusEnum.values()) {
			if (StringUtils.equals(c.getCode(), code)) {
				return c;
			}
		}
		throw new IllegalArgumentException(code + "");
	}

	public static void main(String[] args) {
		System.out.println(DataStatusEnum.NORMAL_USED.getCode());
	}
}
