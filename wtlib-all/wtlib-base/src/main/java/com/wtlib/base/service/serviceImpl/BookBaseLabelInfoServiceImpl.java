package com.wtlib.base.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wtlib.base.dao.BookBaseLabelInfoMapper;
import com.wtlib.base.pojo.BookBaseLabelInfo;
import com.wtlib.base.service.BookBaseLabelInfoService;

@Service("bookBaseLabelInfoService")
public class BookBaseLabelInfoServiceImpl implements BookBaseLabelInfoService {

	@Autowired
	private BookBaseLabelInfoMapper bookBaseLabelInfoMapper;

	@Override
	public Integer insert(BookBaseLabelInfo entity) throws Exception {
		System.out.println(entity);
		Integer num = bookBaseLabelInfoMapper.insert(entity);
		return num;
	}

	@Override
	public int deleteById(Object id,Object reviser) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertBatch(List<BookBaseLabelInfo> entityList) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BookBaseLabelInfo selectById(Object id,String dataStatus) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BookBaseLabelInfo> selectAll(String dataStatus) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(BookBaseLabelInfo entity) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BookBaseLabelInfo find(Object str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByLabelId(Object labelId,Object reviser) {
		bookBaseLabelInfoMapper.deleteByLabelId(labelId,reviser);
	}

}
