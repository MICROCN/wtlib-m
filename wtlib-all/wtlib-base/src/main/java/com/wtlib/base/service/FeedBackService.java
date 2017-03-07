package com.wtlib.base.service;

import java.util.List;

import com.wtlib.base.pojo.FeedBack;

/**
 * @Description: 回馈类接口
 * @author zongzi
 * @date 2017年1月22日 下午1:49:06
 */
public interface FeedBackService extends BaseService<FeedBack> {

	List<FeedBack> selectAllByUserId(String userId, String dataStatus);

}
