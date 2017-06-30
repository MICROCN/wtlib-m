package com.test.service;

import java.util.List;

import org.junit.Test;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringBean;

import com.test.BaseTestStarter;
import com.test.component.XlsDataSetBeanFactory;
import com.wtlib.base.constants.DataStatusEnum;
import com.wtlib.base.pojo.BookBase;
import com.wtlib.base.service.BookBaseService;

public class BookBaseServiceTest extends BaseTestStarter{
	@SpringBean(value = "bookBaseService")
	private BookBaseService bookBaseService;
	
//	@Test
	public void addBook(){
		try {
			List<BookBase> bbS = XlsDataSetBeanFactory.createBeans(
					"/wtlib.testdatasource.xls", "t_book_base",
					BookBase.class);
			for(BookBase bb : bbS){
				bookBaseService.insert(bb);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Test
	public void deleteBook(){
		try {
			List<BookBase> bbS = XlsDataSetBeanFactory.createBeans(
					"/wtlib.testdatasource.xls", "t_book_base",
					BookBase.class);
			for(BookBase bb : bbS){
				bookBaseService.deleteById("31",1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Test
	public void updateBook(){
		try {
			List<BookBase> bbS = XlsDataSetBeanFactory.createBeans(
					"/wtlib.testdatasource.xls", "t_book_base",
					BookBase.class);
			for(BookBase bb : bbS){
				bookBaseService.update(bb);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void selectBook(){
		try {
//			List<BookBase> bbS = XlsDataSetBeanFactory.createBeans(
//					"/wtlib.testdatasource.xls", "t_book_base",
//					BookBase.class);
//			for(BookBase bb : bbS){
				System.out.println(bookBaseService.selectAll(DataStatusEnum.NORMAL_USED.getCode()));
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
