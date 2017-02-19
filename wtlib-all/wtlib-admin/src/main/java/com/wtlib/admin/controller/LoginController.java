package com.wtlib.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Message;
import com.alibaba.fastjson.JSON;
import com.wtlib.base.constants.Code;
import com.wtlib.base.pojo.User;
import com.wtlib.base.service.UserService;
import com.wtlib.base.vo.LoginVo;
import com.wtlib.common.utils.IpUtils;

/**
 * @author pohoulong
 * 
 */
@Controller
public class LoginController extends BaseController {
	
	@Resource(name= "userService")
	private UserService userService;
	Logger log = Logger.getLogger(UserController.class);
	
	@RequestMapping("/login")
	public Message login(@RequestBody LoginVo login,HttpSession session,HttpServletRequest request) {
		String password = login.getPassword();
		String loginId = login.getLoginId();
		String code = login.getCode();
		String realCode = session.getAttribute("generateCode").toString();
		if(code != realCode){
			return Message.error(Code.PARAMATER, "验证码错误！");
		}
		//其他的null都提示的是不得为空，只有这里会记录ip的原因是防止入侵。
		if(password==null|| code==null|| loginId==null){
			//恶意侵入，记录ip，并禁止其再次登录
			String ip= IpUtils.getIp(request);
			log.error("ip:"+JSON.toJSON(ip)+"\n\t username:"+login.getLoginId());
			return Message.error(Code.FATAL_ERROR, "别搞事情",ip);
		}
		if(password.matches("^.*[\\s]+.*$")){
			return Message.error(Code.PARAMATER, "密码不能包含空格、制表符、换页符等空白字符");
		}
		if(code.matches("^.*[\\s]+.*$")){
			return Message.error(Code.PARAMATER, "验证码不能包含空格、制表符、换页符等空白字符");
		}
		if(loginId.matches("^.*[\\s]+.*$")){
			return Message.error(Code.PARAMATER, "昵称不能包含空格、制表符、换页符等空白字符");
		}
		User user = new User(loginId,password);
		try {
			Integer id= userService.confirm(user);
			if(id!=null){
				session.setAttribute("user", id);//这里不安全。肯定要改。要么用https要么就加密
				session.setMaxInactiveInterval(60*30);
				log.info(JSON.toJSONString(user)+"登陆了\n\t ip:"+IpUtils.getIp(request));
				return Message.success("登陆成功！");
			}
			else
				return Message.error(Code.PARAMATER, "账号或密码错误");
		} catch (Exception e) {
			log.error(JSON.toJSONString(user) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法查询数据");
		}
	}
}
