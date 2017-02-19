package com.wtlib.base.service.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wtlib.base.pojo.BorrowRecord;
import com.wtlib.base.service.BorrowRecordService;

/**
 * @author zongzi
 * @date 2017年1月22日 下午2:03:18
 */
@Service("borrowRecordService")
public class BorrowRecordServiceImpl implements BorrowRecordService {

	@Override
	public int insert(BorrowRecord entity) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertBatch(List<BorrowRecord> entityList) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BorrowRecord selectById(Object id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BorrowRecord> selectAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteById(Object id) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(BorrowRecord entity) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BorrowRecord find(Object str) {
		// TODO Auto-generated method stub
		return null;
	}

}
