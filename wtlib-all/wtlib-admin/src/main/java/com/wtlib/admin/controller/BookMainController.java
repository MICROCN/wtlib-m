package com.wtlib.admin.controller;

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
import com.wtlib.base.dto.TotalInfo;
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
@RequestMapping("/admin")
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

	@RequestMapping("/add/book")
	public Message addBook(@RequestBody BookBase book, HttpSession session) {
		// 这里只列举了几个重要的进行判断。比如图书描述、图书价格等没有强制要求（当当网也是没有强制要求）
		String title = book.getBookTitle();
		String url = book.getBookCoverUrl();
		String writer = book.getBookWriter();
		String publicsher = book.getBookPublisher();
		Integer num = book.getBookNum();
		if (num == 0 || num == null) {
			return Message.error(Code.PARAMATER, "数目不得为空！");
		}
		if (title == null) {
			return Message.error(Code.PARAMATER, "书名不得为空！");
		}
		if (url == null) {
			return Message.error(Code.PARAMATER, "图片地址不得为空！");
		}
		if (writer == null) {
			return Message.error(Code.PARAMATER, "作者不得为空！");
		}
		if (publicsher == null) {
			return Message.error(Code.PARAMATER, "出版商不得为空！");
		}
		String id = session.getAttribute("id").toString();// 以后会改
		book.setCreator(new Integer(id));
		try {
			baseService.insert(book);
		} catch (Exception e) {
			log.error(JSON.toJSONString(book) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法添加数据");
		}
		return Message.success("添加成功");
	}

	@RequestMapping("/delete/book")
	public Message deleteBook(@RequestParam("id") Integer id,
			HttpSession session) {
		// 传入的是singleid。
		String reviser = session.getAttribute("id").toString();// 以后会改
		try {
			baseService.deleteById(id, reviser);
			return Message.success("删除成功");
		} catch (Exception e) {
			log.error(JSON.toJSONString(id) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法删除数据");
		}
	}

	@RequestMapping("/delete/allbook")
	public Message deleteAllBook(@RequestParam("id") Integer id,
			HttpSession session) {
		// 传入的是base_id。
		String reviser = session.getAttribute("id").toString();// 以后会改
		try {
			baseService.deleteByBaseId(id, reviser);
			return Message.success("删除成功");
		} catch (Exception e) {
			log.error(JSON.toJSONString(id) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法删除数据");
		}
	}

	@RequestMapping("/update/book")
	public Message updateBook(@RequestBody BookBase book, HttpSession session) {
		String title = book.getBookTitle();
		String url = book.getBookCoverUrl();
		String writer = book.getBookWriter();
		String publicsher = book.getBookPublisher();
		if (title == null) {
			return Message.error(Code.PARAMATER, "书名不得为空！");
		}
		if (url == null) {
			return Message.error(Code.PARAMATER, "图片地址不得为空！");
		}
		if (writer == null) {
			return Message.error(Code.PARAMATER, "作者不得为空！");
		}
		if (publicsher == null) {
			return Message.error(Code.PARAMATER, "出版商不得为空！");
		}
		String id = session.getAttribute("id").toString();// 以后会改
		book.setReviser(new Integer(id));
		try {
			baseService.update(book);
			return Message.success("更新成功");
		} catch (Exception e) {
			log.error(JSON.toJSONString(book) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法更新数据");
		}
	}

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

	/**
	 * 获取所有的图书信息，包含图书基本信息加support类的可借数量，预约人数
	 * @return
	 */
	@RequestMapping("/get/total/")
	public Message getTotal() {
		// 传入的是baseid
		// 应该传回书籍信息
		try {
			TotalInfo book = baseService.selectTotal();
			return Message.success(Code.SUCCESS, "查找成功", book);
		} catch (Exception e) {
			log.error(e.toString());
			return Message.error(Code.ERROR_CONNECTION, "找不到书籍！");
		}
	}
}
