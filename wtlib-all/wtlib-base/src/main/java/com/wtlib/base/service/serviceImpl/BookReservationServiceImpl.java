package com.wtlib.base.service.serviceImpl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.wtlib.base.constants.DataStatusEnum;
import com.wtlib.base.constants.OptionStatusEnum;
import com.wtlib.base.dao.BookBaseSupportMapper;
import com.wtlib.base.dao.BookReservationMapper;
import com.wtlib.base.pojo.BookBaseSupport;
import com.wtlib.base.pojo.BookReservation;
import com.wtlib.base.service.BookReservationService;

/**
 * @Description: 图书预约类
 * @author zongzi
 * @date 2017年1月22日 下午1:56:32
 */
@Service("bookReservationService")
public class BookReservationServiceImpl implements BookReservationService {

	@Autowired
	private BookReservationMapper bookReservationMapper;

	@Autowired
	private BookBaseSupportMapper bookBaseSupportMapper;

	@Override
	public int insert(BookReservation entity) throws Exception {
		return 0;
	}

	@Override
	public int insertBatch(List<BookReservation> entityList) throws Exception {
		return 0;
	}

	@Override
	public BookReservation selectById(Object id) throws Exception {
		return null;
	}

	@Override
	public List<BookReservation> selectAll() throws Exception {
		return null;
	}

	@Override
	public int deleteById(Object id) throws Exception {
		return 0;
	}

	@Override
	public int update(BookReservation entity) throws Exception {
		return 0;
	}

	@Override
	public Boolean insertNewBookReservation(Integer bookId, Integer userId)
			throws Exception {

		// 检查书本是否可以预约
		BookBaseSupport bookBaseSupport = bookBaseSupportMapper
				.selectBookBaseSupportByBookId(bookId,
						DataStatusEnum.NORMAL_USED.getCode());

		Assert.isTrue(null != bookBaseSupport, "null bookId");

		String isReservateAble = bookBaseSupport.getIsReservateAble();

		Assert.isTrue(StringUtils.equals(OptionStatusEnum.OPENT_TRUE.getCode(),
				isReservateAble), "reservation of book:" + bookId
				+ " isn't avaliable");

		Integer currentReservateNumber = bookBaseSupport
				.getCurrentReservateNumber();
		Integer singleBookNumber = bookBaseSupport.getSingleBookNumber();

		Assert.isTrue(currentReservateNumber + 1 <= singleBookNumber * 2,
				"current Reservate Number is more than double of bookNumbers -->["
						+ currentReservateNumber + "," + singleBookNumber + "]");// 如果预约了这一本书就超过两倍

		BookBaseSupport bookBaseSupportTemp = new BookBaseSupport();

		bookBaseSupportTemp.setBookId(bookId);

		bookBaseSupportTemp
				.setCurrentReservateNumber(currentReservateNumber + 1);

		bookBaseSupportTemp.setReviser(userId);
		if (currentReservateNumber + 1 == singleBookNumber * 2) {
			bookBaseSupportTemp
					.setIsReservateAble(OptionStatusEnum.OPTION_FALSE.getCode());// 设置不可预约
		}

		// 更新图书基础信息
		Integer updateBookBaseSupport = bookBaseSupportMapper
				.updateByBookId(bookBaseSupportTemp);

		Assert.isTrue(
				updateBookBaseSupport == 1,
				"update book base support false"
						+ JSON.toJSONString(bookBaseSupportTemp));

		// 添加预约记录
		BookReservation bookReservation = new BookReservation();
		bookReservation.setBookId(bookId);
		bookReservation.setUserId(userId);
		bookReservation.setCreator(userId);
		int insertBookReservation = bookReservationMapper
				.insert(bookReservation);
		
		Assert.isTrue(insertBookReservation == 1,
				"insert book reservation record faild");

		return true;
	}

	@Override
	public BookReservation find(Object str) {
		// TODO Auto-generated method stub
		return null;
	}

}
