package com.test.service;

import org.junit.Test;
import org.unitils.spring.annotation.SpringBean;

import com.test.BaseTestStarter;
import com.wtlib.base.dto.SupportWebDto;
import com.wtlib.base.service.BookBaseSupportService;

public class BookBaseSupportServiceImpl extends BaseTestStarter {
	@SpringBean(value = "bookBaseSupportService")
	private BookBaseSupportService bookBaseSupportService;
	
	@Test
	public void selectSupport(){
		try {
			SupportWebDto support = bookBaseSupportService.selectByBaseId(13);
			System.out.println(support);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
