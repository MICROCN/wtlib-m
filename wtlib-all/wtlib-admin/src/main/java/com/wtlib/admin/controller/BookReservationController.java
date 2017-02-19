package com.wtlib.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wtlib.base.service.BookReservationService;

/**
 * @Description: TODO
 * @author zongzi
 * @date 2017年1月22日 下午2:28:23
 */
@Controller
@RequestMapping("/bookreservation/")
public class BookReservationController {
	@Autowired
	private BookReservationService bookReservationService;

	@RequestMapping("/test")
	public String testController(String userName, String password) {
		return "aaa";

	}
}
