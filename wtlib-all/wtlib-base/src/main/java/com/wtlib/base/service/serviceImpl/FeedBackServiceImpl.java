package com.wtlib.base.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wtlib.base.constants.DataStatusEnum;
import com.wtlib.base.dao.FeedBackMapper;
import com.wtlib.base.pojo.FeedBack;
import com.wtlib.base.service.FeedBackService;

/**
 * @Description: 反馈类实现类
 * @author zongzi
 * @date 2017年1月22日 下午2:06:49
 */
@Service("feedBackService")
public class FeedBackServiceImpl implements FeedBackService {

	@Autowired
	FeedBackMapper feedBackMapper;
	@Override
	public Integer insert(FeedBack entity) throws Exception {
		int num = feedBackMapper.insert(entity);
		return num;
	}

	@Override
	public int insertBatch(List<FeedBack> entityList) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FeedBack selectById(Object id,String dataStatus) throws Exception {
		return null;
	}

	@Override
	public List<FeedBack> selectAll(String dataStatus) throws Exception {
		List<FeedBack> list = feedBackMapper.selectAll(DataStatusEnum.NORMAL_USED.getCode());
		return list;
	}

	@Override
	public int deleteById(Object id,Object reviser) throws Exception {
		int num = feedBackMapper.deleteById(id,reviser);
		return num;
	}

	@Override
	public int update(FeedBack entity) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FeedBack find(Object str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FeedBack> selectAllByUserId(String userId, String dataStatus) {
		List<FeedBack> feedBackList = feedBackMapper.selectAllByUserId(userId,dataStatus);
		return feedBackList;
	}

}
