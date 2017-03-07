package com.wtlib.base.dao;

import org.apache.ibatis.annotations.Param;

import com.wtlib.base.pojo.UserLevel;

/**
 * @author zongzi
 * @date 2017年1月21日 下午6:16:11
 */
public interface UserLevelMapper extends BaseDao<UserLevel> {

	Double selectByUserId(@Param("reviser")Integer nowReviser,@Param("dataStatus") String dataStatus);
}