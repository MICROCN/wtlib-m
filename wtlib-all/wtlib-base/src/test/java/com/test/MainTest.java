package com.test;
//flicts test
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wtlib.base.pojo.User;
import com.wtlib.base.service.UserService;
import com.wtlib.base.start.InterfaceUrlInti;
//ddsssssss


//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:spring.xml",ddddddddddddddddddddddddd
//		"classpath:spring-mybatis.xml", "classpath:spring-mvc.xml"})
public class MainTest{
	 
	private static ClassPathXmlApplicationContext context = null;
	
	
	static{
		InterfaceUrlInti.init();
		context = new ClassPathXmlApplicationContext(new String[] {
				"classpath:spring-mybatis.xml", "classpath:spring.xml",
				"classpath:spring-aop.xml" });
	}
	
	private static UserService userService;
	@Test
	public void test1() throws Exception {	
		userService = (UserService)context.getBean("userService");
		User user = new User();
		user.setPassword("aaaaa");
		user.setLoginId("testtesttest");
		try{
			int insert = userService.insert(user);
			System.out.println(insert);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	 }
	
}
