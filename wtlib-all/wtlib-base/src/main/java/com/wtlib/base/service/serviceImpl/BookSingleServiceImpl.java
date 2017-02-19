package com.wtlib.base.service.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wtlib.base.pojo.BookSingle;
import com.wtlib.base.service.BookSingleService;

/**
 * @author zongzi
 * @date 2017年1月22日 下午2:01:00
 */
@Service("bookSingleService")
public class BookSingleServiceImpl implements BookSingleService {

	@Override
	public int insert(BookSingle entity) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertBatch(List<BookSingle> entityList) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BookSingle selectById(Object id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BookSingle> selectAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteById(Object id) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(BookSingle entity) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BookSingle find(Object str) {
		// TODO Auto-generated method stub
		return null;
	}

}
