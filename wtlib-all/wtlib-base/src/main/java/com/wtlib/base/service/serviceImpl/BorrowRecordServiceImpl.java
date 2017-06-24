package com.wtlib.base.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wtlib.base.constants.DataStatusEnum;
import com.wtlib.base.dao.BorrowRecordMapper;
import com.wtlib.base.dto.RecordDto;
import com.wtlib.base.pojo.BorrowRecord;
import com.wtlib.base.service.BorrowRecordService;

/**
 * @author pohoulong
 * @date 2017年1月22日 下午2:03:18
 */
@Service("borrowRecordService")
public class BorrowRecordServiceImpl implements BorrowRecordService {

	@Autowired
	BorrowRecordMapper borrowRecordMapper;
	

	@Override
	public List<BorrowRecord> selectAllByUserId(String id,String borrowCode,String dataStatus) {
		List<BorrowRecord> record = borrowRecordMapper.selectAllByUserId(id,borrowCode,dataStatus);
		return record;
	}
	
	@Override
	public Integer insert(BorrowRecord entity) throws Exception {
		int num = borrowRecordMapper.insert(entity);
		return num;
	}

	@Override
	public int insertBatch(List<BorrowRecord> entityList) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BorrowRecord selectById(Object id,String dataStatus) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BorrowRecord> selectAll(String dataStatus) throws Exception {
		return borrowRecordMapper.selectAll(dataStatus);
	}

	@Override
	public int deleteById(Object id,Object reviser) throws Exception {
		return borrowRecordMapper.deleteById(id, reviser);
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

	@Override
	public BorrowRecord selectBySingleId(Integer singleId) {
		BorrowRecord record = borrowRecordMapper.selectBySingleId(singleId,DataStatusEnum.NORMAL_USED.getCode());
		return record;
	}

	@Override
	public RecordDto selectAll() throws Exception {
		return borrowRecordMapper.selectRecord();
	}

}
