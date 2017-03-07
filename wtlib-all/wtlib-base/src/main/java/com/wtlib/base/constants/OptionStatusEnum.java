package com.wtlib.base.constants;

public enum OptionStatusEnum {
	OPTION_FALSE("0", "选择假"), OPENT_TRUE("1", "选择真"), OPTION_NULL(null, "不选择");

	private String code;
	private final String value;

	OptionStatusEnum(String code, String v) {
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

	public static OptionStatusEnum fromCode(String code) {
		for (OptionStatusEnum c : OptionStatusEnum.values()) {
			if (c.getCode().equals(code)) {
				return c;
			}
		}
		throw new IllegalArgumentException(code + "");
	}

	public static void main(String[] args) {
		System.out.println(OptionStatusEnum.OPENT_TRUE.getCode());
	}
}
