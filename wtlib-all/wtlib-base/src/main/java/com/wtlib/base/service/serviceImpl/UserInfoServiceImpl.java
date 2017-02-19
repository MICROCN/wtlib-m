package com.wtlib.base.service.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wtlib.base.dao.UserInfoMapper;
import com.wtlib.base.dto.UserWebDto;
import com.wtlib.base.pojo.UserInfo;
import com.wtlib.base.service.UserInfoService;

/**
 * @author pohoulong
 * @date 2017年1月22日 下午2:07:56
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	UserInfoMapper userInfoMapper;
	
	@Override
	public int insert(UserInfo entity) throws Exception {
		int num= userInfoMapper.insert(entity);
		Assert.isTrue(num!=0,"插入失败");
		return num;
	}

	@Override
	public UserWebDto find(String username) {	
		UserWebDto user= userInfoMapper.selectByUsername(username);
		Assert.isTrue(user!=null,"查无此人！");
		return user;
	}
	
	@Override
	public int update(UserInfo entity) throws Exception {
		int num= userInfoMapper.update(entity);
		Assert.isTrue(num!=0,"更新失败");
		return num;
	}


	@Override
	public int deleteById(Object id) throws Exception {
		//delete不仅要把userInfo表的status设为000还要把user表的status设为000
		int num= userInfoMapper.deleteById(id);
		Assert.isTrue(num!=0,"删除失败");
		return num;
	}
	
	@Override
	public int insertBatch(List<UserInfo> entityList) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UserInfo selectById(Object id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfo> selectAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public UserInfo find(Object str) {
		// TODO Auto-generated method stub
		return null;
	}


}
