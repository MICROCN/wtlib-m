package com.wtlib.base.constants;



/**
 * @Description: TODO
 * @author zongzi
 * @date 2017年1月22日 下午3:10:29
 */
public enum DataStatusEnum {
	DELETED("000", "逻辑删除"), NORMAL_USED("001", "正常可用"),EXCEPTION_DATA_REMARK("100","不同结果标识");
	private String code;

	private final String value;

	
	
	DataStatusEnum(String code, String v) {
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
			if (c.getCode().equals(code)) {
				return c;
			}
		}
		throw new IllegalArgumentException(code + "");
	}
	
	public static void main(String[] args) {
		System.out.println(DataStatusEnum.NORMAL_USED.getCode());
	}
}
