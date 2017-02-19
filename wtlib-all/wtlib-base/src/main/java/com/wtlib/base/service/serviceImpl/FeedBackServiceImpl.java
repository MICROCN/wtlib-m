package com.wtlib.base.service.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wtlib.base.pojo.FeedBack;
import com.wtlib.base.service.FeedBackService;

/**
 * @Description: TODO
 * @author zongzi
 * @date 2017年1月22日 下午2:06:49
 */
@Service("feedBackService")
public class FeedBackServiceImpl implements FeedBackService {

	@Override
	public int insert(FeedBack entity) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertBatch(List<FeedBack> entityList) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FeedBack selectById(Object id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FeedBack> selectAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteById(Object id) throws Exception {
		// TODO Auto-generated method stub
		return 0;
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

}
