package com.wtlib.base.service.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wtlib.base.pojo.CreditInfo;
import com.wtlib.base.service.CreditInfoService;

/**
 * @author zongzi
 * @date 2017年1月22日 下午2:04:38
 */
@Service("creditInforService")
public class CreditInfoServiceImpl implements CreditInfoService {

	@Override
	public int insert(CreditInfo entity) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertBatch(List<CreditInfo> entityList) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CreditInfo selectById(Object id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CreditInfo> selectAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteById(Object id) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(CreditInfo entity) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CreditInfo find(Object str) {
		// TODO Auto-generated method stub
		return null;
	}

}
