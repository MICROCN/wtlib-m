package com.wtlib.base.constants;

/**
 * @Description: 数据撞他枚举类
 * @author zongzi
 * @date 2017年1月22日 下午3:10:29
 */
public enum CodeEnum {
	SUCCESS(10000, "成功"), ERR_PARAM(10001, "参数错误"), FAIL(10002, "系统异常"), ERR_CONNECTION(
			10003, "数据库连接异常"), FATAL_ERR(10004, "严重错误");

	private int code;

	private final String value;

	CodeEnum(int code, String v) {
		this.code = code;
		value = v;
	}

	public String value() {
		return value;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	public static CodeEnum fromCode(int code) {
		for (CodeEnum c : CodeEnum.values()) {
			if (c.getCode() == code) {
				return c;
			}
		}
		throw new IllegalArgumentException(code + "");
	}

	public static void main(String[] args) {
		System.out.println(CodeEnum.SUCCESS.getCode());
	}
}
