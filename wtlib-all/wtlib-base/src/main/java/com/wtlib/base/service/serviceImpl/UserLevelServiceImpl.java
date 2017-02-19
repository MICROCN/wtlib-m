package com.wtlib.base.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
	public int insert(UserLevel entity) throws Exception {
		int num= userLevelMapper.insert(entity);
		Assert.isTrue(num!=0,"插入失败");
		return num;
	}

	@Override
	public int update(UserLevel entity) throws Exception {
		int num= userLevelMapper.update(entity);
		Assert.isTrue(num!=0,"更新失败");
		return num;
	}

	@Override
	public List<UserLevel> selectAll() throws Exception {
		List<UserLevel> level= userLevelMapper.selectAll();
		Assert.isTrue(level!=null,"查找不到数据！");
		return level;
	}
	
	@Override
	public int deleteById(Object id) throws Exception {
		int num= userLevelMapper.deleteById(id);
		Assert.isTrue(num!=0,"删除失败");
		return num;
	}
	
	@Override
	public int insertBatch(List<UserLevel> entityList) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UserLevel selectById(Object id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserLevel find(Object str) {
		// TODO Auto-generated method stub
		return null;
	}

}
