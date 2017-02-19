package com.wtlib.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wtlib.base.service.BookSingleService;

/**
 * @author zongzi
 * @date 2017年1月22日 下午2:29:58
 */
@Controller
@RequestMapping("/booksingle/")
public class BookSingleController {
	@Autowired
	private BookSingleService booService;
}
