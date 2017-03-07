package com.wtlib.admin.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Message;
import com.wtlib.base.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final Log logger = LogFactory.getLog(UserController.class);

	@Autowired
	UserService userService;

	@RequestMapping("/test")
	@ResponseBody
	public Message testProject() {
		logger.info("sart Test");
		try {
			return Message.success(userService.selectById(11));
		} catch (Exception e) {

			logger.error("error ending-->" + e.toString(), e);
			return Message.error("error", e);
		}
	}
}
