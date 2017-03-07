package com.wtlib.base.service.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wtlib.base.constants.DataStatusEnum;
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
	public Integer insert(UserInfo entity) throws Exception {
		int num= userInfoMapper.insert(entity);
		return num;
	}

	@Override
	public UserWebDto find(String username,String dataStatus) {	
		UserWebDto user= userInfoMapper.selectByUsername(username,dataStatus);
		return user;
	}
	
	@Override
	public int update(UserInfo entity) throws Exception {
		int num= userInfoMapper.update(entity);
		return num;
	}


	@Override
	public int deleteById(Object id,Object reviser) throws Exception {
		//delete不仅要把userInfo表的status设为000还要把user表的status设为000
		int num= userInfoMapper.deleteById(id,reviser);
		return num;
	}
	
	@Override
	public int insertBatch(List<UserInfo> entityList) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UserInfo selectById(Object id,String dataStatus) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfo> selectAll(String dataStatus) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public UserInfo find(Object str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInfo selectByUserId(Integer nowReviser,String dataStatus) {
		UserInfo info = userInfoMapper.selectByUserId(nowReviser,dataStatus);
		return info;
	}


}
