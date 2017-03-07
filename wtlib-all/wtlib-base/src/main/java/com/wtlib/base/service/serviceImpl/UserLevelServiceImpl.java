package com.wtlib.base.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wtlib.base.constants.DataStatusEnum;
import com.wtlib.base.dao.UserLevelMapper;
import com.wtlib.base.pojo.UserLevel;
import com.wtlib.base.service.UserLevelService;

/**
 * @author pohoulong
 * @date 2017年1月22日 下午2:08:48
 */
@Service("userLevelService")
public class UserLevelServiceImpl implements UserLevelService {

	@Autowired
	UserLevelMapper userLevelMapper;
	
	@Override
	public Integer insert(UserLevel entity) throws Exception {
		int num= userLevelMapper.insert(entity);
		return num;
	}

	@Override
	public int update(UserLevel entity) throws Exception {
		int num= userLevelMapper.update(entity);
		return num;
	}

	@Override
	public List<UserLevel> selectAll(String dataStatus) throws Exception {
		List<UserLevel> levelList= userLevelMapper.selectAll(DataStatusEnum.NORMAL_USED.getCode());
		return levelList;
	}
	
	@Override
	public int deleteById(Object id,Object reviser) throws Exception {
		int num= userLevelMapper.deleteById(id,reviser);
		return num;
	}
	
	@Override
	public UserLevel selectById(Object id,String dataStatus) throws Exception {
		UserLevel level = userLevelMapper.selectById(id,DataStatusEnum.NORMAL_USED.getCode());
		return level;
	}
	
	@Override
	public int insertBatch(List<UserLevel> entityList) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UserLevel find(Object str) {
		// TODO Auto-generated method stub
		return null;
	}

}
