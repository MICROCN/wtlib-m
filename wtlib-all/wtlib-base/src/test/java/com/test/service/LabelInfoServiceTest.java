package com.test.service;

import java.util.List;

import org.junit.Test;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringBean;

import com.test.BaseTestStarter;
import com.test.component.XlsDataSetBeanFactory;
import com.wtlib.base.pojo.LabelInfo;
import com.wtlib.base.service.LabelInfoService;

public class LabelInfoServiceTest  extends BaseTestStarter {
	@SpringBean(value = "labelInfoService")
	private LabelInfoService labelInfoService;
	
	@Test
	@DataSet("dataSetXls/BookReservationService/insertNewBookReservation.xls")
	@ExpectedDataSet("dataSetXls/BookReservationService/insertNewBookReservation.expect.xls")
	public void addLabel(){
		try {
			List<LabelInfo> liS = XlsDataSetBeanFactory.createBeans(
					"/wtlib.testdatasource.xls", "t_label_Info",
					LabelInfo.class);
			for(LabelInfo bbli : liS){
				labelInfoService.insert(bbli);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Test
	@DataSet("dataSetXls/BookReservationService/insertNewBookReservation.xls")
	@ExpectedDataSet("dataSetXls/BookReservationService/insertNewBookReservation.expect.xls")
	public void deleteLabel(){
		try {
			List<LabelInfo> liS = XlsDataSetBeanFactory.createBeans(
					"/wtlib.testdatasource.xls", "t_label_Info",
					LabelInfo.class);
			for(LabelInfo bbli : liS){
//				labelInfoService.deleteById(bbli.getId(),bbli.getReviser());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
