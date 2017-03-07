package com.wtlib.base.dao;

import org.apache.ibatis.annotations.Param;

import com.wtlib.base.pojo.BookReservation;


/**
 * @author zongzi
 * @date 2017年1月21日 下午6:16:40
 */
public interface BookReservationMapper extends BaseDao<BookReservation> {
	
	public Integer reservationBookByUser(@Param("userId") Integer userId,@Param("dataStatus") String dataStatus);

}