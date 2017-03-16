package com.wtlib.base.service;


import com.wtlib.base.dto.UserWebDto;
import com.wtlib.base.pojo.User;

/**
 * ClassName: UserService
 * 
 * @author pohoulong
 * @date 2017年1月22日 下午1:36:22
 */
public interface UserService extends BaseService<User> {

	public void save(User user) throws Exception;

	public int update(User user) throws Exception;

	public UserWebDto find(String user,String dataStatus) throws Exception;
	
	public Integer confirm(User user) throws Exception;

	public UserWebDto selectAllById(Integer userid,String dataStatus);

	public Integer confirmAdmin(User user);


}
