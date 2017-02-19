package com.wtlib.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wtlib.base.service.BorrowRecordService;

/**
 * @author zongzi
 * @date 2017年1月22日 下午2:37:57
 */
@Controller
@RequestMapping("/borrowrecord/")
public class BorrowRecordController {
	@Autowired
	private BorrowRecordService borrowRecordService;
}
