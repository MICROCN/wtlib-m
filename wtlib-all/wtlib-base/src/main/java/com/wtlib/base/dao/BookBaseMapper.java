package com.wtlib.base.dao;

import org.apache.ibatis.annotations.Param;

import com.wtlib.base.pojo.BookBase;

/**
 * @author zongzi
 * @date 2017年1月21日 下午6:16:26
 */
public interface BookBaseMapper extends BaseDao<BookBase> {

	public Integer reservationBookByUser(@Param("userId") Integer userId);
}