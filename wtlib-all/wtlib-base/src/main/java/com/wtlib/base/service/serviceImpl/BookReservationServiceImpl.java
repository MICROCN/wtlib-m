package com.wtlib.base.service.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

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
import com.wtlib.base.service.BookBaseSupportService;
import com.wtlib.base.service.BookReservationService;

/**
 * @Description: 图书预约类
 * @author pohoulong
 * @date 2017年1月22日 下午1:56:32
 */
@Service("bookReservationService")
public class BookReservationServiceImpl implements BookReservationService {

	@Autowired
	private BookReservationMapper bookReservationMapper;

	@Resource(name="bookBaseSupportService")
	private BookBaseSupportService bookBaseSupportService;

	@Override
	public Integer insert(BookReservation entity) throws Exception {
		return bookReservationMapper
				.insert(entity);
	}

	@Override
	public int insertBatch(List<BookReservation> entityList) throws Exception {
		return 0;
	}

	@Override
	public BookReservation selectById(Object id,String dataStatus) throws Exception {
		return null;
	}

	@Override
	public List<BookReservation> selectAll(String dataStatus) throws Exception {
		return null;
	}

	@Override
	public int deleteById(Object id,Object reviser) throws Exception {
		return 0;
	}

	@Override
	public int update(BookReservation entity) throws Exception {
		return 0;
	}

	@Override
	public Boolean insertNewBookReservation(Integer bookBaseId, Integer userId)
			throws Exception {

		// 检查书本是否可以预约
		BookBaseSupport bookBaseSupport = bookBaseSupportService
				.selectBookBaseSupportByBookBaseId(bookBaseId,
						DataStatusEnum.NORMAL_USED.getCode());

		Assert.isTrue(null != bookBaseSupport, "null bookId");

		String isReservateAble = bookBaseSupport.getIsReservateAble();

		Assert.isTrue(StringUtils.equals(OptionStatusEnum.OPENT_TRUE.getCode(),
				isReservateAble), "reservation of book:" + bookBaseId
				+ " isn't avaliable");

		Integer currentReservateNumber = bookBaseSupport
				.getCurrentReservateNumber();
		Integer singleBookNumber = bookBaseSupport.getSingleBookNumber();

		Assert.isTrue(currentReservateNumber + 1 <= singleBookNumber * 2,
				"current Reservate Number is more than double of bookNumbers -->["
						+ currentReservateNumber + "," + singleBookNumber + "]");// 如果预约了这一本书就超过两倍

		BookBaseSupport bookBaseSupportTemp = new BookBaseSupport();

		bookBaseSupportTemp.setBookBaseId(bookBaseId);

		bookBaseSupportTemp
				.setCurrentReservateNumber(currentReservateNumber + 1);

		bookBaseSupportTemp.setReviser(userId);
		if (currentReservateNumber + 1 == singleBookNumber * 2) {
			bookBaseSupportTemp
					.setIsReservateAble(OptionStatusEnum.OPTION_FALSE.getCode());// 设置不可预约
		}

		// 更新图书基础信息
		Integer updateBookBaseSupport = bookBaseSupportService
				.updateByBookId(bookBaseSupportTemp);

		Assert.isTrue(
				updateBookBaseSupport == 1,
				"update book base support false"
						+ JSON.toJSONString(bookBaseSupportTemp));

		// 添加预约记录
		BookReservation bookReservation = new BookReservation();
		bookReservation.setBookId(bookBaseId);
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
		return null;
	}

}
