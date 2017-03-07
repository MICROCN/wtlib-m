package com.wtlib.base.service;

import com.wtlib.base.pojo.BookReservation;

/**
 * ClassName: BookReservationService
 * 
 * @Description: 图书预约类接口
 * @author zongzi
 * @date 2017年1月22日 下午1:44:41
 */
public interface BookReservationService extends BaseService<BookReservation> {

	/**
	 * user 预约书籍
	 * 
	 * @param @param bookId
	 * @param @param userId
	 * @param @return
	 * @author zongzi
	 * @date 2017年2月13日 下午5:49:15
	 */
	public Boolean insertNewBookReservation(Integer bookId, Integer userId)
			throws Exception;
}
