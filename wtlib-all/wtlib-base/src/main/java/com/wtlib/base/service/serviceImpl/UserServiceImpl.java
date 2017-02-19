package com.wtlib.base.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wtlib.base.dao.UserMapper;
import com.wtlib.base.dto.UserWebDto;
import com.wtlib.base.pojo.User;
import com.wtlib.base.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	UserMapper userMapper;

	public int update(User user)  throws Exception{
		int num = userMapper.update(user);
		Assert.isTrue(num!=0,"修改失败");
		return num;
	}

	public UserWebDto find(String loginId) {
		UserWebDto user= userMapper.selectByLoginId(loginId);
		Assert.isTrue(user!=null,"查无此人！");
		return user;
	}

	
	@Override
	public int deleteById(Object id) throws Exception {
		//delete不仅要把userInfo表的status设为000还要把user表的status设为000
		int num= userMapper.deleteById(id);
		Assert.isTrue(num!=0,"删除失败！");
		return num;
	}
	
	@Override
	public int insert(User entity) throws Exception {
		int num= userMapper.insert(entity);
		Assert.isTrue(num!=0,"插入失败！");
		return num;
	}

	@Override
	public int insertBatch(List<User> entityList) throws Exception {
		return 0;
	}

	@Override
	public User selectById(Object id) throws Exception {
		User user= userMapper.selectById(id);
		Assert.isTrue(user!=null,"查无此人！");
		return user;
	}

	@Override
	public Integer confirm(User user) {
		Integer id= userMapper.confirm(user);
		Assert.isTrue(id!=null,"查无此人！");
		return id;
	}
	
	@Override
	public List<User> selectAll() throws Exception {
		return userMapper.selectAll();
	}

	@Override
	public User find(Object str) {
		return null;
	}

	@Override
	public void save(User user) throws Exception {
	}

}
