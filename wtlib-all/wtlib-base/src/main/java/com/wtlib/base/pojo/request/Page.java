package com.wtlib.base.pojo.request;

import java.io.Serializable;

/**
 * @Description: 请求分页参数基类
 * @author dapengniao
 * @date 2016年7月27日 下午2:03:38
 */
public class Page implements Serializable {
	private static final long serialVersionUID = -4527112189386545207L;
	// 当前页
	private Integer pageIndex;
	// 每页条数
	private Integer pageSize;
	// 开始记录
	@SuppressWarnings("unused")
	private Integer pageStart;

	// null或者0分页；1：不分页
	private Integer all;
	private String startDate;
	private String endDate;
	// 排序字段
	private String sortField;
	// desc or asc
	private String orderType;

	public Integer getPageIndex() {
		if (pageIndex == null || pageIndex == 0) {
			return 1;
		}
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		if (pageSize == null) {
			return 10;
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getAll() {
		return all;
	}

	public void setAll(Integer all) {
		this.all = all;
	}

	public Integer getPageStart() {
		if (pageIndex == null || pageIndex == 0) {
			return 0;
		}
		return (pageIndex - 1) * pageSize;
	}

	public void setPageStart(Integer pageStart) {
		this.pageStart = pageStart;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

}
