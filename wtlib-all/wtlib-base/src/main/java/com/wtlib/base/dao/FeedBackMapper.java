package com.wtlib.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wtlib.base.pojo.FeedBack;

public interface FeedBackMapper extends BaseDao<FeedBack> {

	List<FeedBack> selectAllByUserId(@Param("userId")String userId,@Param("dataStatus") String dataStatus);
}