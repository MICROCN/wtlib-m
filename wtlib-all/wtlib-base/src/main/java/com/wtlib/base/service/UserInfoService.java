package com.wtlib.base.service;

import com.wtlib.base.dto.UserWebDto;
import com.wtlib.base.pojo.UserInfo;

/**
 * @Description: TODO
 * @author zongzi
 * @date 2017年1月22日 下午1:49:47
 */
public interface UserInfoService extends BaseService<UserInfo> {
	
	public UserWebDto find(String username);
	
	
}
