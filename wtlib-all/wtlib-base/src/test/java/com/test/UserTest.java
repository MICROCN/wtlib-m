package com.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.unitils.reflectionassert.ReflectionAssert;

import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;
import org.unitils.spring.annotation.SpringBeanByName;
import org.unitils.spring.annotation.SpringBeanByType;

import com.wtlib.base.pojo.User;
import com.wtlib.base.service.BookBaseService;
import com.wtlib.base.service.BookReservationService;
import com.wtlib.base.service.UserService;
import com.wtlib.base.start.InterfaceUrlInti;

@SpringApplicationContext({ "classpath:spring-mybatis.xml",
		"classpath:spring.xml", "classpath:spring-aop.xml" })
public class UserTest extends UnitilsJUnit4 {
	protected static ClassPathXmlApplicationContext context = null;

	static {
		InterfaceUrlInti.init();
		/*
		 * context = new ClassPathXmlApplicationContext(new String[] {
		 * "classpath:spring-mybatis.xml", "classpath:spring.xml",
		 * "classpath:spring-aop.xml" });
		 */
	}

	// @Test
	public void testReflection() {
		User user = new User();
		user.setLoginId("tom");
		User user2 = new User();
		user2.setLoginId("tom1");
		// ReflectionAssert.assertReflectionEquals(user, user2);
	}

	private static ApplicationContext applicationContext;

	@SpringBean("userService")
	private UserService userService;

	@SpringBeanByType//按照类名来创建类型
	private BookBaseService bookBaseService;

	@SpringBeanByName
	private BookReservationService bookReservationService;
	// @Test
	public void testUserService() {

		System.out.println("done");

		assertNotNull(applicationContext);
		assertNotNull(userService);
		System.out.println("done");
	}

//	@Test
	public void testBookBaseServiceInject() {
		assertNotNull(bookBaseService);
		System.out.println("done");
	}
	
//	@Test
	public void testBookReservationServiceInject(){
		assertNotNull(bookReservationService);
		System.out.println("done");
	}
	
}
