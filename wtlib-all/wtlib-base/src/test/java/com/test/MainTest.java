package com.test;

//flicts test
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Test;
import org.unitils.spring.annotation.SpringBean;

import com.alibaba.fastjson.JSON;
import com.wtlib.base.pojo.User;
import com.wtlib.base.service.UserService;
public class MainTest extends BaseTestStarter {

	@SpringBean(value = "userService")
	private static UserService userService;

	@Test
	public void test1() throws Exception {
		
	}

	/**
	 * @param 练习java反射
	 */
	public static void main(String[] args) {
		User user = new User("jc", "sdfa");
		Class<? extends User> class1 = user.getClass();
		System.out.println(JSON.toJSONString(class1.getDeclaredMethods()));
		String[] methodNames = new String[] { "getLoginId" };
		for (String methodName : methodNames) {

			try {
				Method method = class1.getDeclaredMethod(methodName);
				Object invoke = method.invoke(user, null);
				System.out.println(JSON.toJSONString(invoke));
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		String[] filedNames = new String[] { "loginId", "password" };
		for (String filedName : filedNames) {
			Field field;
			try {
				field = class1.getDeclaredField(filedName);
				field.setAccessible(true);
				try {
					System.out.println(field.get(user));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
