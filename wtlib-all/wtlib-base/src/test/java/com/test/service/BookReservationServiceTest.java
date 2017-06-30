package com.test.service;

import java.util.List;

import org.junit.Test;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringBean;

import com.test.BaseTestStarter;
import com.test.component.XlsDataSetBeanFactory;
import com.wtlib.base.pojo.BookBaseLabelInfo;
import com.wtlib.base.pojo.BookReservation;
import com.wtlib.base.service.BookReservationService;

/**
 * ClassName: BookBaseServiceTest
 * 
 * @Description: 测试基本图书测试
 * @author zongzi
 * @date 2017年2月13日 下午2:06:09
 */

public class BookReservationServiceTest extends BaseTestStarter {

	@SpringBean(value = "bookReservationService")
	private BookReservationService bookReservationService;

//	@Test
	@DataSet("dataSetXls/BookReservationService/insertNewBookReservation.xls")
	@ExpectedDataSet("dataSetXls/BookReservationService/insertNewBookReservation.expect.xls")
	public void testInsertNewBookReservation() {
		try {
			List<BookReservation> brS = XlsDataSetBeanFactory.createBeans(
					"/wtlib.testdatasource.xls", "t_book_reservate_source",
					BookReservation.class);

			for (BookReservation br : brS) {
				int bookId = br.getBookBaseId();
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

	@Test
	public void testStart() {
		 try {
			BookBaseLabelInfo newInstance = BookBaseLabelInfo.class.newInstance();
			System.out.println(newInstance);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testStart1() {
	}
}
