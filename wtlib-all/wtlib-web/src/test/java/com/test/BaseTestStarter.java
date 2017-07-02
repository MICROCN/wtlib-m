package com.test;

import static org.junit.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.*;
import static org.unitils.reflectionassert.ReflectionComparatorMode.*;
import static org.mockito.Mockito.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;


@SpringApplicationContext({ "test-spring.xml", "test-spring-mybatis.xml",
		"test-spring-aop.xml", "test-spring-mvc.xml" ,"test-spring-redis.xml" })
public class BaseTestStarter extends UnitilsJUnit4 {
	public static final Log logger = LogFactory.getLog(BaseTestStarter.class);

	@SpringApplicationContext
	public ApplicationContext applicationContext;

	@Test
	public void testServiceStart() {
		logger.info("\n\n--------------------------------- << SPRING-TEST START >> ---------------------------------\n");
	}
}
