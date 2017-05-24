package com.wtlib.admin.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Message;
import com.alibaba.fastjson.JSON;
import com.wtlib.base.constants.Code;
import com.wtlib.base.constants.DataStatusEnum;
import com.wtlib.base.dto.UserWebDto;
import com.wtlib.base.pojo.User;
import com.wtlib.base.pojo.UserInfo;
import com.wtlib.base.pojo.UserLevel;
import com.wtlib.base.service.UserInfoService;
import com.wtlib.base.service.UserLevelService;
import com.wtlib.base.service.UserService;
import com.wtlib.common.utils.IpUtils;

@RequestMapping("/admin")
public class UserController {

	@Resource(name = "userService")
	UserService userService;
	@Resource(name = "userInfoService")
	UserInfoService userInfoService;
	@Resource(name = "userLevelService")
	private UserLevelService userLevelService;

	Logger log = Logger.getLogger(UserController.class);

	// login
	@RequestMapping("/login")
	public Message login(@RequestBody User user,HttpSession session,HttpServletRequest request) {
		String password = user.getPassword();
		String loginId = user.getLoginId();
		// 其他的null都提示的是不得为空，只有这里会记录ip的原因是防止入侵。
		if (password == null || loginId == null) {
			// 恶意侵入，记录ip，并禁止其再次登录
			String ip = IpUtils.getIp(request);
			log.error("ip:" + JSON.toJSON(ip) + "\n\t username:"
					+ user.getLoginId());
			return Message.error(Code.FATAL_ERROR, "别搞事情", ip);
		}
		if (password.matches("^.*[\\s]+.*$")) {
			return Message.error(Code.PARAMATER, "密码不能包含空格、制表符、换页符等空白字符");
		}
		if (loginId.matches("^.*[\\s]+.*$")) {
			return Message.error(Code.PARAMATER, "昵称不能包含空格、制表符、换页符等空白字符");
		}
		try {
			Integer id = userService.confirmAdmin(user);
			if (id != null) {
				session.setAttribute("user", id);// 这里不安全。肯定要改。要么用https要么就加密
				session.setMaxInactiveInterval(60 * 30);
				log.info(JSON.toJSONString(user) + "登陆了\n\t ip:"
						+ IpUtils.getIp(request));
				return Message.success("登陆成功！");
			} else
				return Message.error(Code.PARAMATER, "账号或密码错误");
		} catch (Exception e) {
			log.error(JSON.toJSONString(user) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法查询数据");
		}
	}

	// user
	@RequestMapping("/add/user")
	@ResponseBody
	public Message addUser(@RequestBody User user, HttpSession session) {
		String id = session.getAttribute("id").toString();// 以后会改
		String password = user.getPassword();
		String loginId = user.getLoginId();
		if (loginId == null || password == null) {
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
		user.setCreator(new Integer(id));
		try {
			userService.insert(user);
			return Message.success("插入成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(user) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法插入数据");
		}
	}

	@RequestMapping("/delete/user")
	@ResponseBody
	public Message deleteUser(@RequestParam("id") Integer id,
			HttpSession session) {
		String reviser = session.getAttribute("id").toString();// 以后会改
		try {
			userService.deleteById(id, reviser);
			return Message.success("删除成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(id) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法删除数据");
		}
	}

	@RequestMapping("/update/user")
	@ResponseBody
	public Message updateUser(@RequestBody User user, HttpServletRequest session) {
		String password = user.getPassword();
		String loginId = user.getLoginId();
		String id = session.getAttribute("id").toString();// 以后会改
		if (loginId == null || password == null) {
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

	@RequestMapping("/find/user")
	public Message findUser(@RequestBody User user) {
		String loginId = user.getLoginId();
		if (loginId == null) {
			return Message.error(Code.PARAMATER, "账号为空");
		}
		try {
			UserWebDto dto = userService.find(loginId,
					DataStatusEnum.NORMAL_USED.getCode());
			return Message.success(Code.SUCCESS, "查找成功", dto);
		} catch (Exception e) {
			log.error(JSON.toJSONString(user) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "找不到此用户！");
		}
	}

	//删除info同时会删除user
	@RequestMapping("/delete/info")
	@ResponseBody
	public Message deleteUserInfo(@RequestParam("id") Integer id,
			HttpSession session) {
		String reviser = session.getAttribute("id").toString();// 以后会改
		try {
			userInfoService.deleteById(id, reviser);
			return Message.success("删除成功");
		} catch (Exception e) {
			log.error(JSON.toJSONString(id) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法删除数据");
		}
	}

	@RequestMapping("/update/info")
	@ResponseBody
	public Message updateUserInfo(@RequestBody UserInfo userInfo,
			HttpSession session, HttpServletRequest request) {
		String username = userInfo.getUsername();
		// 下面是为了防止有人恶意篡改等级和积分，只要他们传过来为空即可。
		Integer level = userInfo.getUserLevelId();
		Integer value = userInfo.getCurrentCreditValue();
		String id = session.getAttribute("id").toString();// 以后会改
		if (username == null || level != null && value != null) {
			// 恶意侵入，记录ip，并禁止其再次登录
			String ip = IpUtils.getIp(request);
			log.error("ip:" + JSON.toJSON(ip) + "\n\t username:"
					+ userInfo.getUsername() + "\n\t id:" + id);
			return Message.error(Code.FATAL_ERROR, "别搞事情", ip);
		}
		try {
			userInfo.setReviser(new Integer(id));
			userInfoService.update(userInfo);
			return Message.success("更新成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(userInfo) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法更改数据");
		}
	}

	@RequestMapping("/find/info")
	@ResponseBody
	public Message findUserInfo(@RequestBody UserInfo userInfo) {
		String username = userInfo.getUsername();
		if (username == null) {
			return Message.error(Code.PARAMATER, "不得为空");
		}
		try {
			UserWebDto dto = userInfoService.find(username,
					DataStatusEnum.NORMAL_USED.getCode());
			return Message.success("查找成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(userInfo) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法查询数据");
		}
	}

	// userLevel

	@RequestMapping("/add/level")
	@ResponseBody
	public Message addLevel(@RequestBody UserLevel userLevel,
			HttpSession session, HttpServletRequest request) {
		String id = session.getAttribute("id").toString();// 以后会改
		userLevel.setCreator(new Integer(id));
		String name = userLevel.getLevelName();
		Integer value = userLevel.getLevelMinCreditValue();
		Double weight = userLevel.getLevelWeight();

		if (value != null) {
			// 恶意侵入，记录ip，并禁止其再次登录
			String ip = IpUtils.getIp(request);
			log.error("ip:" + JSON.toJSON(ip) + "\n\t id：" + id);
			return Message.error(Code.FATAL_ERROR, "别搞事情", ip);
		}
		if (name == null || weight == null) {
			return Message.error(Code.PARAMATER, "不得为空");
		}
		if (name.matches("^.*[\\s]+.*$")) {
			return Message.error(Code.PARAMATER, "昵称不能包含空格、制表符、换页符等空白字符");
		}
		try {
			value = new Integer((int) (weight * 1200));// value的只由weight算出来的。这里假定为1200
			userLevelService.insert(userLevel);
			return Message.success("插入成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(userLevel) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法增加数据");
		}
	}

	@RequestMapping("/delete/level")
	@ResponseBody
	public Message deleteLevel(@RequestParam("id") Integer id,
			HttpSession session) {
		String reviser = session.getAttribute("id").toString();// 以后会改
		try {
			userLevelService.deleteById(id, reviser);
			return Message.success("删除成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(id) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法删除数据");
		}
	}

	@RequestMapping("/update/level")
	@ResponseBody
	public Message updateLevel(@RequestBody UserLevel userLevel,
			HttpSession session, HttpServletRequest request) {
		String name = userLevel.getLevelName();
		Integer value = userLevel.getLevelMinCreditValue();
		Double weight = userLevel.getLevelWeight();
		String id = session.getAttribute("id").toString();// 以后会改

		if (value != null) {
			// 恶意侵入，记录ip，并禁止其再次登录
			String ip = IpUtils.getIp(request);
			log.error("ip:" + JSON.toJSON(ip) + "\n\t id：" + id);
			return Message.error(Code.FATAL_ERROR, "别搞事情", ip);
		}
		if (name == null || weight == null) {
			return Message.error(Code.PARAMATER, "不得为空");
		}
		if (name.matches("^.*[\\s]+.*$")) {
			return Message.error(Code.PARAMATER, "昵称不能包含空格、制表符、换页符等空白字符");
		}
		try {
			userLevel.setReviser(new Integer(id));
			userLevelService.update(userLevel);
			return Message.success("更新成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(userLevel) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法更新数据");
		}
	}

	@RequestMapping("/find/level")
	@ResponseBody
	public Message findLevel() {
		try {
			List<UserLevel> userLevelList = userLevelService
					.selectAll(DataStatusEnum.NORMAL_USED.getCode());
			return Message.success(Code.SUCCESS, "查找成功", userLevelList);
		} catch (Exception e) {
			log.error(e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法查询数据");
		}
	}

}
