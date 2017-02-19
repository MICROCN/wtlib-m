package com.wtlib.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wtlib.base.service.FeedBackService;

/**
 * @Description: TODO
 * @author zongzi
 * @date 2017年1月22日 下午2:45:10
 */
@Controller
@RequestMapping("/feedback/")
public class FeedBackController {
	@Autowired
	private FeedBackService feedBackService;
}
