package com.wtlib.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Message;
import com.alibaba.fastjson.JSON;
import com.wtlib.base.constants.BorrowStatusEnum;
import com.wtlib.base.constants.Code;
import com.wtlib.base.constants.DataStatusEnum;
import com.wtlib.base.dto.SupportWebDto;
import com.wtlib.base.pojo.BookBase;
import com.wtlib.base.pojo.BookSingle;
import com.wtlib.base.pojo.BorrowRecord;
import com.wtlib.base.service.BookBaseService;
import com.wtlib.base.service.BookBaseSupportService;
import com.wtlib.base.service.BookReservationService;
import com.wtlib.base.service.BookSingleService;
import com.wtlib.base.service.BorrowRecordService;
import com.wtlib.base.service.serviceImpl.BookReservationServiceImpl;
import com.wtlib.common.utils.IpUtils;

@Controller
public class BookMainController {

	@Resource(name = "bookBaseSupportService")
	BookBaseSupportService baseSupportService;

	@Resource(name = "bookBaseService")
	private BookBaseService baseService;

	@Resource(name = "bookReservationService")
	private BookReservationService reservationService;

	@Resource(name = "bookSingleService")
	private BookSingleService singleService;

	@Resource(name = "borrowRecordService")
	BorrowRecordService borrowRecordService;

	private static final Log log = LogFactory.getLog(BookMainController.class);
	
	@RequestMapping("/find/book")
	public Message findBook(@RequestBody BookBase book) {
		String title = book.getBookTitle();
		if (title == null) {
			return Message.error(Code.PARAMATER, "书名为空");
		}
		try {
			List<BookBase> bookList = baseService.find(title);
			return Message.success(Code.SUCCESS, "查找成功", bookList);
		} catch (Exception e) {
			log.error(JSON.toJSONString(book) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "找不到此书籍！");
		}
	}

	@RequestMapping("/get/book")
	public Message getBook() {
		try {
			List<BookBase> bookList = baseService
					.selectAll(DataStatusEnum.NORMAL_USED.getCode());
			return Message.success(Code.SUCCESS, "查找成功", bookList);
		} catch (Exception e) {
			log.error(e.toString());
			return Message.error(Code.ERROR_CONNECTION, "找不到书籍！");
		}
	}

	// 这个是需要评价的标签
	@RequestMapping("/get/back")
	public Message getBackRecoder(HttpSession session) {
		String id = session.getAttribute("id").toString();// 以后会改
		try {
			List<BorrowRecord> borrowRecordList = borrowRecordService
					.selectAllByUserId(id,
							BorrowStatusEnum.TICK_LABEL.getCode(),
							DataStatusEnum.NORMAL_USED.getCode());
			return Message.success(Code.SUCCESS, "查找成功", borrowRecordList);
		} catch (Exception e) {
			log.error(e.toString());
			return Message.error(Code.ERROR_CONNECTION, "找不到记录！");
		}
	}

	// 这个是已经评价过的标签
	@RequestMapping("/get/labelRecord")
	public Message getLabel(HttpSession session) {
		String id = session.getAttribute("id").toString();// 以后会改
		try {
			List<BorrowRecord> borrowRecordList = borrowRecordService
					.selectAllByUserId(id,
							BorrowStatusEnum.GIVE_BACK.getCode(),
							DataStatusEnum.NORMAL_USED.getCode());
			return Message.success(Code.SUCCESS, "查找成功", borrowRecordList);
		} catch (Exception e) {
			log.error(e.toString());
			return Message.error(Code.ERROR_CONNECTION, "找不到记录！");
		}
	}

	@RequestMapping("/get/support")
	public Message getBook(@RequestParam("id") Integer id) {
		// 传入的是baseid
		// 应该传回书籍信息，书籍借阅预约信息和评价信息
		try {
			SupportWebDto book = baseSupportService.selectByBaseId(id);
			return Message.success(Code.SUCCESS, "查找成功", book);
		} catch (Exception e) {
			log.error("id:" + id + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "找不到书籍！");
		}
	}

	// BookBorrow
	@RequestMapping("/update/borrow")
	public Message borrow(List<BookSingle> bookList, HttpSession session,
			HttpServletRequest request) {
		String id = session.getAttribute("id").toString();// 以后会改
		for (BookSingle book : bookList) {
			book.setReviser(new Integer(id));
			String hash = book.getBookHash();
			book.setCurrentOwner(new Integer(id));
			book.setReviser(new Integer(id));
			if (hash != null) {
				// 恶意侵入，记录ip，并禁止其再次登录
				String ip = IpUtils.getIp(request);
				log.error("ip:" + JSON.toJSON(ip) + "\n\t");
				return Message.error(Code.FATAL_ERROR, "别搞事情", ip);
			}
			try {
				singleService.update(book);
			} catch (Exception e) {
				log.error("book:" + JSON.toJSONString(book) + "\n\t"
						+ e.toString());
				return Message.error(Code.ERROR_CONNECTION, "找不到书籍！");
			}
		}
		return Message.success("借阅成功");
	}

	@RequestMapping("/update/back/{hash}")
	public Message back(@PathVariable String hash, HttpSession session) {
		String id = session.getAttribute("id").toString();// 以后会改
		BookSingle single = new BookSingle(hash, new Integer(id));
		try {
			singleService.editReturnBack(single);
			return Message.success("归还成功");
		} catch (Exception e) {
			log.error("book:" + JSON.toJSONString(hash) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "找不到书籍！");
		}
	}

	@RequestMapping("/update/reservation")
	public Message reservation(@RequestParam("id") Integer id,
			HttpSession session) {
		String userId = session.getAttribute("id").toString();// 以后会改
		try {
			reservationService
					.insertNewBookReservation(id, new Integer(userId));
			return Message.success("借阅成功");
		} catch (Exception e) {
			log.error("book:" + JSON.toJSONString(id) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "借阅失败！");
		}
	}
}
