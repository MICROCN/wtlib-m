package com.test.service;

import java.util.List;

import org.junit.Test;
import org.unitils.spring.annotation.SpringBean;

import com.test.BaseTestStarter;
import com.test.component.XlsDataSetBeanFactory;
import com.wtlib.base.pojo.BookSingle;
import com.wtlib.base.service.BookSingleService;

public class BookSingleServiceTest extends BaseTestStarter{
	@SpringBean(value = "bookSingleService")
	private BookSingleService bookSingleService;
	
	@Test
	public void selectSupport(){
		try {
			List<BookSingle> bbS = XlsDataSetBeanFactory.createBeans(
					"/wtlib.testdatasource.xls", "t_book_single",
					BookSingle.class);
			for(BookSingle bb : bbS){
				bookSingleService.editReturnBack(bb);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
