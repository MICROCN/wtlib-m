package com.wtlib.admin.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Message;
import com.alibaba.fastjson.JSON;
import com.wtlib.base.constants.Code;
import com.wtlib.base.dto.UserWebDto;
import com.wtlib.base.pojo.User;
import com.wtlib.base.service.UserService;

/**
 * ClassName: UserController
 * 
 * @Description: 简单的User的controller..实现了增删改查..
 * @author pohoulong
 * 
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Resource(name = "userService")
	UserService userService;

	Logger log = Logger.getLogger(UserController.class);

	@RequestMapping("/add")
	@ResponseBody
	public Message addUser(@RequestBody User user, HttpSession session) {
		String id = session.getAttribute("id").toString();// 以后会改
		user.setCreator(new Integer(id));
		String password = user.getPassword();
		if (password == null) {
			return Message.error(Code.PARAMATER, "不得为空");
		}
		if (password.matches("^.*[\\s]+.*$")) {
			return Message.error(Code.PARAMATER, "密码不能包含空格、制表符、换页符等空白字符");
		}
		if ((!password.contains("@"))
				&& (!password.matches("^[1]([0-9][0-9])[0-9]{8}$"))) {
			return Message.error(Code.PARAMATER, "手机号码错误");
		}
		if (password.contains("@")
				&& password
						.matches("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
			return Message.error(Code.PARAMATER, "邮箱不正确");
		}
		try {
			userService.insert(user);
			return Message.success("插入成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(user) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法插入数据");
		}
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Message deleteUser(@RequestParam("id") Integer id) {
		try {
			userService.deleteById(id);
			return Message.success("删除成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(id) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法删除数据");
		}
	}

	@RequestMapping("/update")
	@ResponseBody
	public Message updateUser(@RequestBody User user, HttpServletRequest session) {
		String password = user.getPassword();
		String loginId = user.getLoginId();
		String id = session.getAttribute("id").toString();// 以后会改
		if (loginId == null||password == null) {
			return Message.error(Code.PARAMATER, "不得为空");
		}
		if (password.matches("^.*[\\s]+.*$")) {
			return Message.error(Code.PARAMATER, "密码不能包含空格、制表符、换页符等空白字符");
		}
		if ((!password.contains("@"))
				&& (!password.matches("^[1]([0-9][0-9])[0-9]{8}$"))) {
			return Message.error(Code.PARAMATER, "手机号码错误");
		}
		if (password.contains("@")
				&& password
						.matches("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
			return Message.error(Code.PARAMATER, "邮箱不正确");
		}
		if (loginId.matches("^.*[\\s]+.*$")) {
			return Message.error(Code.PARAMATER, "账号不能包含空格、制表符、换页符等空白字符");
		}
		try {
			user.setReviser(new Integer(id));
			userService.update(user);
			return Message.success("更新成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(user) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法更新数据");
		}
	}

	@RequestMapping("/find")
	public Message findUser(@RequestBody User user) {
		String loginId = user.getLoginId();
		if (loginId == null) {
			return Message.error(Code.PARAMATER, "账号为空");
		}
		try {
			UserWebDto dto = userService.find(loginId);
			return Message.success(Code.SUCCESS, "查找成功", dto);
		} catch (Exception e) {
			log.error(JSON.toJSONString(user) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "找不到此用户！");
		}
	}
	
}