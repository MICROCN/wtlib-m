package com.wtlib.base.service.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wtlib.base.pojo.CreditRecord;
import com.wtlib.base.service.CreditRecordSrevice;

/**
 * @author zongzi
 * @date 2017年1月22日 下午2:05:48
 */
@Service("creditRecordService")
public class CreditRecordServiceImpl implements CreditRecordSrevice {

	@Override
	public int insert(CreditRecord entity) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertBatch(List<CreditRecord> entityList) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CreditRecord selectById(Object id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CreditRecord> selectAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteById(Object id) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(CreditRecord entity) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CreditRecord find(Object str) {
		// TODO Auto-generated method stub
		return null;
	}

}
