package com.wtlib.base.dao;

import com.wtlib.base.dto.UserWebDto;
import com.wtlib.base.pojo.UserInfo;

/**
 * @author pohoulong
 * @date 2017年1月21日 下午6:15:14
 */
public interface UserInfoMapper extends BaseDao<UserInfo> {
	
	public UserWebDto selectByUsername(String username);
	
}