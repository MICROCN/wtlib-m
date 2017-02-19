package com.test.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

import com.alibaba.fastjson.JSON;
import com.test.component.XlsDataSetBeanFactory;
import com.test.dao.BaseDaoTest;
import com.wtlib.base.constants.DataStatusEnum;
import com.wtlib.base.dao.BookBaseMapper;
import com.wtlib.base.dao.BookBaseSupportMapper;
import com.wtlib.base.dao.BookReservationMapper;
import com.wtlib.base.pojo.BookBaseSupport;
import com.wtlib.base.pojo.BookReservation;
import com.wtlib.base.service.BookReservationService;
import com.wtlib.base.service.serviceImpl.BookReservationServiceImpl;

/**
 * ClassName: BookBaseServiceTest
 * 
 * @Description: 测试基本图书测试
 * @author zongzi
 * @date 2017年2月13日 下午2:06:09
 */
@SpringApplicationContext({ "test-spring.xml", "test-spring-mybatis.xml",
		"test-spring-aop.xml" })
public class BookReservationServiceTest extends BaseDaoTest {

	// private BookReservationMapper bookReservationMapper;
	//
	// private BookBaseSupportMapper bookBaseSupportMapper;

	@SpringBean(value = "bookReservationService")
	private BookReservationService bookReservationService;

	@Before
	public void init() {
		// bookReservationMapper = mock(BookReservationMapper.class);
		// bookBaseSupportMapper = mock(BookBaseSupportMapper.class);
	}

	// @Test
	public void reservationABookByUser() {
		// List<BookReservation> br = new ArrayList<BookReservation>();
		// List<BookBaseSupport> bbs = new ArrayList<BookBaseSupport>();
		// try {
		// br = XlsDataSetBeanFactory.createBeans("/wtlib.testdatasource.xls",
		// "t_book_reservation", BookReservation.class);
		//
		// bbs = XlsDataSetBeanFactory.createBeans("wtlib.testdatasource.xls",
		// "t_book_base_support", BookBaseSupport.class);
		// BookReservation bookReservationA = br.get(0);
		// BookBaseSupport bookBaseSupportA = bbs.get(0);
		//
		// doReturn(bookBaseSupportA).when(bookBaseSupportMapper)
		// .selectBookBaseSupportByBookId(
		// bookReservationA.getBookId(),
		// DataStatusEnum.NORMAL_USED.getCode());
		//
		// BookBaseSupport bookBaseSupportC = new BookBaseSupport();
		//
		// bookBaseSupportC.setBookId(bookReservationA.getBookId());
		// bookBaseSupportC.setCurrentReservateNumber(bookBaseSupportA
		// .getCurrentReservateNumber() + 1);
		// bookBaseSupportC.setReviser(bookReservationA.getUserId());
		//
		// doReturn(new Integer(1)).when(bookBaseSupportMapper)
		// .updateByBookId(bookBaseSupportC);
		//
		// System.out.println(JSON.toJSONString(bookBaseSupportC));
		// doReturn(new Integer(1)).when(bookReservationMapper).insert(
		// bookReservationA);
		//
		// BookReservationServiceImpl bookReservationService = new
		// BookReservationServiceImpl();
		//
		// ReflectionTestUtils.setField(bookReservationService,
		// "bookReservationMapper", bookReservationMapper);
		// ReflectionTestUtils.setField(bookReservationService,
		// "bookBaseSupportMapper", bookBaseSupportMapper);
		// // 预约一本新的书籍
		// Boolean insertNewBookReservation = bookReservationService
		// .insertNewBookReservation(bookReservationA.getBookId(),
		// bookReservationA.getUserId());
		//
		// assertEquals(true, insertNewBookReservation);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	@Test
	@DataSet("/dataSetXls/BookReservationService/insertNewBookReservation.xls")
//	@ExpectedDataSet("/dataSetXls/BookReservationService/insertNewBookReservation.expect.xls")、、、冲突测试
	public void testInsertNewBookReservation() {
		try {
			List<BookReservation> brS = XlsDataSetBeanFactory.createBeans(
					"/wtlib.testdatasource.xls", "t_book_reservate_source",
					BookReservation.class);
			
			for (BookReservation br : brS) {
				
				int bookId = br.getBookId();
				int userId = br.getUserId();
				try {
					bookReservationService.insertNewBookReservation(bookId,
							userId);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
