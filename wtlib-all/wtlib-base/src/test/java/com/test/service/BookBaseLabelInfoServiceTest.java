package com.test.service;

import java.util.List;

import org.junit.Test;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringBean;

import com.test.BaseTestStarter;
import com.test.component.XlsDataSetBeanFactory;
import com.wtlib.base.pojo.BookBaseLabelInfo;
import com.wtlib.base.service.BookBaseLabelInfoService;


public class BookBaseLabelInfoServiceTest extends BaseTestStarter {
	@SpringBean(value = "bookBaseLabelInfoService")
	private BookBaseLabelInfoService bookBaseLabelInfoService;
	
//	@Test
	@DataSet("dataSetXls/BookReservationService/insertNewBookReservation.xls")
	@ExpectedDataSet("dataSetXls/BookReservationService/insertNewBookReservation.expect.xls")
	public void addLabel(){
		try {
			List<BookBaseLabelInfo> bbliS = XlsDataSetBeanFactory.createBeans(
					"/wtlib.testdatasource.xls", "t_book_base_label_info",
					BookBaseLabelInfo.class);
			for(BookBaseLabelInfo bbli : bbliS){
				bookBaseLabelInfoService.insert(bbli);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
