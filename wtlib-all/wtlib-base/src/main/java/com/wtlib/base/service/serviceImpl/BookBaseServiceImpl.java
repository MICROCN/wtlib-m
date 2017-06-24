package com.wtlib.base.service.serviceImpl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wtlib.base.constants.DataStatusEnum;
import com.wtlib.base.dao.BookBaseMapper;
import com.wtlib.base.dao.BookBaseSupportMapper;
import com.wtlib.base.dao.BookSingleMapper;
import com.wtlib.base.dto.TotalInfo;
import com.wtlib.base.pojo.BookBase;
import com.wtlib.base.pojo.BookBaseSupport;
import com.wtlib.base.pojo.BookSingle;
import com.wtlib.base.service.BookBaseService;
import com.wtlib.base.service.BookBaseSupportService;
import com.wtlib.base.service.BookSingleService;

/**
 * @Description: 基础图书处理类
 * @author pohoulong
 * @date 2017年1月22日 下午1:55:48
 */
@Service("bookBaseService")
public class BookBaseServiceImpl implements BookBaseService {

	@Autowired
	BookBaseMapper bookBaseMapper;
	@Autowired
	BookBaseSupportService bookBaseSupportService;
	@Autowired
	BookSingleService bookSingleService;

	@Override
	public Integer insert(BookBase entity) throws Exception {
		// 插入bookBase表返回bookBase对象，如果没有同类书籍的话自然String是null，所以不用判断他是否存在
		BookBase book = bookBaseMapper.find(entity,DataStatusEnum.NORMAL_USED.getCode());
		Integer num = entity.getBookNum();
		System.out.println(book);
		Integer person = entity.getCreator();
		BookBaseSupport support;
		Integer id ;
		if (book != null) {
			id= book.getId();
			Integer currentNum = book.getBookNum() + num;
			book.setBookNum(currentNum);
			book.setReviser(person);
			bookBaseMapper.update(book);
			// 判断,插入bookBaseSupport
			support = bookBaseSupportService.selectBookBaseSupportByBookBaseId(
					id, DataStatusEnum.NORMAL_USED.getCode());
			support.setSingleBookNumber(currentNum);
			Integer currentLeftBook = support.getCurrentLeftBookNumber() + num;
			support.setCurrentLeftBookNumber(currentLeftBook);
			support.setReviser(person);
			Integer reservation = support.getCurrentReservateNumber();
			support.setIsBorrowAble("1");
			// 如果里面还有人预约，即图书原来现存量是0，则通知他们可以借书了。
			if (reservation != null) {
				// TODO 通知预约者可以借书了
			}
			bookBaseSupportService.update(support);
		} else {
			// 如果查找不到即没有此类书籍。
			bookBaseMapper.insert(entity);
			id=entity.getId();
			support = new BookBaseSupport(id, "0", "1", num, 0, num);
			support.setCreator(person);
			bookBaseSupportService.insert(support);
		}
		for (int i = 0; i < num; i++) {
			String uuid = UUID.randomUUID().toString();
			uuid = uuid.replace("-", "");
			BookSingle bookSingle = new BookSingle(id,uuid);
			bookSingle.setCreator(person);
			bookSingleService.insert(bookSingle);
		}
		return id;
	}

	@Override
	public int deleteById(Object id,Object reviser) throws Exception {
		// update booksingleMapper通过singleMap找到bookBaseid
		BookSingle single = bookSingleService.selectById(id,DataStatusEnum.NORMAL_USED.getCode());
		Assert.isTrue(single!=null,"查无此书");
		int num = bookSingleService.deleteById(id,reviser);
		Integer baseId = single.getBookBaseId();
		BookBaseSupport support = bookBaseSupportService
				.selectBookBaseSupportByBookBaseId(baseId,
						DataStatusEnum.NORMAL_USED.getCode());
		Integer singleBookNum = support.getSingleBookNumber()-1;
		if (singleBookNum == 0){
			deleteByBaseId(baseId,reviser);
			return num;
		}
		else
			support.setSingleBookNumber(singleBookNum);
		Integer currentBookNum = support.getCurrentLeftBookNumber() - 1;
		if (currentBookNum == 0) {
			support.setIsReservateAble("1");
			support.setIsBorrowAble("0");
		}
		support.setCurrentLeftBookNumber(currentBookNum);
		bookBaseSupportService.update(support);
		BookBase base = new BookBase();
		base.setBookNum(singleBookNum);
		base.setId(baseId);
		bookBaseMapper.update(base);
		return num;
	}

	@Override
	public void deleteByBaseId(Integer id,Object reviser) throws Exception {
		//将basesupport和base和single中的baseid=id的记录删除
		bookBaseMapper.deleteById(id,reviser);
	}

	@Override
	public int update(BookBase entity) throws Exception {
		Integer id = bookBaseMapper.update(entity);
		return id;
	}

	@Override
	public List<BookBase> find(String title) {
		List<BookBase> bookBaseList = bookBaseMapper.findByTitle(title,DataStatusEnum.NORMAL_USED.getCode());
		return bookBaseList;
	}

	@Override
	public List<BookBase> selectAll(String dataStatus) throws Exception {
		List<BookBase> bookBaseList = bookBaseMapper.selectAll(dataStatus);
		return bookBaseList;
	}

	@Override
	public BookBase selectById(Object id,String dataStatus) throws Exception {
		BookBase base = bookBaseMapper.selectById(id, dataStatus);
		return base;
	}

	@Override
	public int insertBatch(List<BookBase> entityList) throws Exception {
		return 0;
	}

	@Override
	public BookBase find(Object str) {
		return null;
	}

	@Override
	public TotalInfo selectTotal() {
		// TODO Auto-generated method stub
		return bookBaseMapper.selectTotal();
	}

}
