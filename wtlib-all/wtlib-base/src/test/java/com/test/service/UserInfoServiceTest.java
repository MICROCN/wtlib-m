package com.test.service;

import org.junit.Test;
import org.unitils.spring.annotation.SpringBean;

import com.test.BaseTestStarter;
import com.wtlib.base.pojo.User;
import com.wtlib.base.service.UserService;


public class UserInfoServiceTest extends BaseTestStarter{
	@SpringBean(value = "userService")
	private UserService userService;
	
	@Test
	public void confirm(){
		User user = new User();
		user.setLoginId("admin");
		user.setPassword("cbjcl0204");
		try {
			System.out.println(userService.confirm(user));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
