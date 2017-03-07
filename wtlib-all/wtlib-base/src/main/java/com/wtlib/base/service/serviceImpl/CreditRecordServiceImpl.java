package com.wtlib.base.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wtlib.base.dao.CreditRecordMapper;
import com.wtlib.base.pojo.CreditRecord;
import com.wtlib.base.service.CreditRecordService;

/**
 * @author zongzi
 * @date 2017年1月22日 下午2:05:48
 */
@Service("creditRecordService")
public class CreditRecordServiceImpl implements CreditRecordService {

	@Autowired
	CreditRecordMapper creditRecordMapper;
	
	@Override
	public Integer insert(CreditRecord entity) throws Exception {
		int num = creditRecordMapper.insert(entity);
		return num;
	}

	@Override
	public int insertBatch(List<CreditRecord> entityList) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CreditRecord selectById(Object id,String dataStatus) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CreditRecord> selectAll(String dataStatus) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteById(Object id,Object reviser) throws Exception {
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
