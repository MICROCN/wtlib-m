package com.wtlib.base.service.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wtlib.base.constants.CreditEnum;
import com.wtlib.base.constants.DataStatusEnum;
import com.wtlib.base.dao.BookSingleMapper;
import com.wtlib.base.pojo.BookBaseSupport;
import com.wtlib.base.pojo.BookSingle;
import com.wtlib.base.pojo.BorrowRecord;
import com.wtlib.base.pojo.CreditInfo;
import com.wtlib.base.pojo.CreditRecord;
import com.wtlib.base.pojo.UserInfo;
import com.wtlib.base.pojo.UserLevel;
import com.wtlib.base.service.BookBaseSupportService;
import com.wtlib.base.service.BookSingleService;
import com.wtlib.base.service.BorrowRecordService;
import com.wtlib.base.service.CreditInfoService;
import com.wtlib.base.service.CreditRecordService;
import com.wtlib.base.service.UserInfoService;
import com.wtlib.base.service.UserLevelService;

/**
 * @author pohoulong
 * @date 2017年1月22日 下午2:01:00
 */
@Service("bookSingleService")
public class BookSingleServiceImpl implements BookSingleService {

	SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	BookSingleMapper bookSingleMapper;
	@Resource(name = "bookBaseSupportService")
	BookBaseSupportService bookBaseSupportService;
	@Resource(name = "borrowRecordService")
	BorrowRecordService borrowRecordService;
	@Resource(name = "creditRecordService")
	CreditRecordService creditRecordService;
	@Resource(name = "creditInfoService")
	CreditInfoService creditInfoService;
	@Resource(name = "userInfoService")
	UserInfoService userInfoService;
	@Resource(name = "userLevelService")
	UserLevelService userLevelService;

	@Override
	public Integer insert(BookSingle entity) throws Exception {
		Integer num = bookSingleMapper.insert(entity);
		return num;
	}

	@Override
	public int deleteById(Object id,Object reviser) throws Exception {
		Integer num = bookSingleMapper.deleteById(id,reviser);
		return num;
	}

	@Override
	public int update(BookSingle entity) throws Exception {
		Integer baseId = entity.getBookBaseId();
		Integer reviser = entity.getReviser();
		BookBaseSupport support = bookBaseSupportService
				.selectBookBaseSupportByBookBaseId(baseId,
						DataStatusEnum.NORMAL_USED.getCode());
		support.setReviser(reviser);
		Integer book = support.getCurrentLeftBookNumber() - 1;
		Assert.isTrue(book >= 0, "无法借阅书！");
		Integer reservation = support.getCurrentReservateNumber();
		// 如果有预定的人，则就提示所有预定的人书已被借。
		if (reservation != 0) {
			// TODO 通知所有预定的人书已经被借走，是否预定。
			System.out.println("书已经被借走了");
		}
		if (book == 0) {
			support.setCurrentLeftBookNumber(0);
			support.setIsBorrowAble("0");
			support.setIsReservateAble("1");
		} else {
			support.setCurrentLeftBookNumber(book);
		}
		bookSingleMapper.update(entity);
		entity = bookSingleMapper.findByHash(entity.getBookHash(), DataStatusEnum.NORMAL_USED.getCode());
		Integer id = entity.getId();
		bookBaseSupportService.update(support);
		// 添加一条借阅记录
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 30);
		Date date = cal.getTime();
		BorrowRecord record = new BorrowRecord(id, reviser, date);
		int num = borrowRecordService.insert(record);
		return num;
	}

	@Override
	public void editReturnBack(BookSingle entity) throws Exception {
		// 用book_hash作为查询属性查询到book_single对象
		// 先要保存现在的修改者的id
		Integer nowReviser = entity.getReviser();
		String hash = entity.getBookHash();
		entity = bookSingleMapper.findByHash(hash,DataStatusEnum.NORMAL_USED.getCode());
		Integer baseId = entity.getBookBaseId();
		Date oldUpdateTime = entity.getUpdateTime();
		Integer oldReviser = entity.getReviser();
		Integer singleId = entity.getId();
		// 将原来的修改者和修改时间赋值为上一次借阅时间和上一个借阅人
		entity.setLastLender(oldReviser);
		entity.setLastLendTime(oldUpdateTime);
		entity.setReviser(nowReviser);
		// 通过book_base_id查询book_base_support对象
		BookBaseSupport support = bookBaseSupportService
				.selectBookBaseSupportByBookBaseId(baseId,
						DataStatusEnum.NORMAL_USED.getCode());
		// 将图书设为可借阅，并把剩余可借人数+1，判断是否有预约的人。
		support.setIsBorrowAble("1");
		Integer borrowNum = support.getCurrentLeftBookNumber();
		support.setCurrentLeftBookNumber(borrowNum++);
		Integer reservation = support.getCurrentReservateNumber();
		if (reservation != 0) {
			// TODO 发邮件通知所有的人可预约。
		}
		// 借阅记录
		BorrowRecord record = borrowRecordService.selectBySingleId(singleId);
		String borrowStatus = record.getBorrowStatus();
		// 用userid查userlevelid与userlevel表匹配
		UserInfo userInfo = userInfoService.selectByUserId(nowReviser,DataStatusEnum.NORMAL_USED.getCode());
		Integer levelId = userInfo.getUserLevelId();
		UserLevel level = userLevelService.selectById(levelId,DataStatusEnum.NORMAL_USED.getCode());
		Double levelWeight = level.getLevelWeight();
		double levelValue;
		CreditRecord creditRecord = new CreditRecord();
		creditRecord.setCreator(nowReviser);
		creditRecord.setUserId(nowReviser);
		if (borrowStatus.equals("002")) {
			// 就是说他超时未还。
			CreditInfo CreditInfo = creditInfoService
					.selectById(CreditEnum.overtime.getId(),DataStatusEnum.NORMAL_USED.getCode());
			String plus = CreditInfo.getIsPlus();
			creditRecord.setCreditIsPlus(plus);
			creditRecord.setCreditInfoId(CreditEnum.overtime.getId());
			creditRecord.setCreditName(CreditEnum.overtime.getName());
			Integer value = CreditInfo.getCreditValue();
			creditRecord.setCreditValue(value);
			if (plus.equals("1")) {
				levelValue = value * levelWeight;
			} else {
				levelValue = -value * levelWeight;
			}
		} else {
			// 就是说他还书成功
			CreditInfo CreditInfo = creditInfoService
					.selectById(CreditEnum.successReturn.getId(),DataStatusEnum.NORMAL_USED.getCode());
			String plus = CreditInfo.getIsPlus();
			creditRecord.setCreditIsPlus(plus);
			creditRecord.setCreditIsPlus(plus);
			creditRecord.setCreditInfoId(CreditEnum.overtime.getId());
			creditRecord.setCreditName(CreditEnum.overtime.getName());
			Integer value = CreditInfo.getCreditValue();
			creditRecord.setCreditValue(value);
			if (plus.equals("1")) {
				levelValue = value * levelWeight;
			} else {
				levelValue = -value * levelWeight;
			}
		}
		// 修改信用值。
		Integer oldValue = userInfo.getCurrentCreditValue();
		Integer currentValue = (int) (oldValue + levelValue);
		userInfo.setCurrentCreditValue(currentValue);
		userInfo.setReviser(nowReviser);
		// 这里要update user一下,修改信用等级或者信用积分
		// 更新level用spring计时器
		// userInfo = userInfoMapper.updateLevel(userInfo);
		userInfoService.update(userInfo);
		// 这里修改borrowRecord记录，将未归还变成归还，然后归还日期设为现在。
		record.setReviser(nowReviser);
		record.setReturnTime(new Date());
		record.setBorrowStatus("003");
		borrowRecordService.update(record);
		// 记录一下信用记录
		creditRecord.setCreator(nowReviser);
		creditRecord.setCreditBeforeValue(oldValue);
		creditRecord.setCreditAfterValue(currentValue);
		creditRecordService.insert(creditRecord);
	}

	@Override
	public int insertBatch(List<BookSingle> entityList) throws Exception {
		return 0;
	}

	@Override
	public List<BookSingle> selectAll(String dataStatus) throws Exception {
		return null;
	}

	@Override
	public BookSingle find(Object str) {
		return null;
	}

	@Override
	public BookSingle selectById(Object id, String dataStatus) throws Exception {
		BookSingle single = bookSingleMapper.selectById(id, dataStatus);
		return single;
	}

}
