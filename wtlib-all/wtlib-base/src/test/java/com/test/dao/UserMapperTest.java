package com.test.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.unitils.spring.annotation.SpringBean;

import com.test.BaseTestStarter;
import com.wtlib.base.constants.DataStatusEnum;
import com.wtlib.base.dao.UserMapper;
import com.wtlib.base.dto.UserWebDto;

//数据访问层测试类
public class UserMapperTest extends BaseTestStarter {

	public final static String excelFilePath = "../exceldataset";

	@SpringBean("userMapper")
	UserMapper userMapper;

	@Test
	public void findUserById() throws Exception {
		UserWebDto user = (UserWebDto) userMapper.selectByLoginId("jc",DataStatusEnum.NORMAL_USED.toString());
		assertNotNull(user);
	}
}
