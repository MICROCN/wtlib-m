package com.wtlib.admin.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Message;
import com.alibaba.fastjson.JSON;
import com.wtlib.base.constants.Code;
import com.wtlib.base.constants.DataStatusEnum;
import com.wtlib.base.dto.RecordDto;
import com.wtlib.base.pojo.BorrowRecord;
import com.wtlib.base.service.BorrowRecordService;

@RequestMapping("/admin")
public class BorrowRecordController {

	@Autowired
	private BorrowRecordService borrowRecordService;
	
	Logger log = Logger.getLogger(BorrowRecordController.class);

	@RequestMapping("/get/record")
	@ResponseBody
	public Message getRecord() {
		try {
			RecordDto dto = borrowRecordService.selectAll();
			return Message.success("查找成功",dto);
		} catch (Exception e) {
			log.error(e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法查看数据");
		}
	}
	
	@RequestMapping("/delete/label")
	@ResponseBody
	public Message deleteLabel(@RequestParam("id") Integer id,HttpSession session) {
		String reviser = session.getAttribute("id").toString();// 以后会改
		try {
			borrowRecordService.deleteById(id,reviser);
			return Message.success("删除成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(id) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法删除数据");
		}
	}
	
}
