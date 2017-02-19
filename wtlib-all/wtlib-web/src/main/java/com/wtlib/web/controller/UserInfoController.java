package com.wtlib.web.controller;

import java.util.Date;

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
import com.wtlib.base.pojo.UserInfo;
import com.wtlib.base.service.UserInfoService;
import com.wtlib.common.utils.IpUtils;


/**
 * @Description: TODO
 * @author pohoulong
 * @date 2017年1月22日 下午2:46:21
 */
@Controller
@RequestMapping("/userinfo")
public class UserInfoController {
	
	@Resource(name= "userInfoService") UserInfoService userInfoService;
	
	Logger log = Logger.getLogger(UserController.class);
	
	@RequestMapping("/add")
	@ResponseBody
	public Message addUserInfo(@RequestBody UserInfo userInfo,HttpSession session){
		String id= session.getAttribute("id").toString();//以后会改
		userInfo.setCreator(new Integer(id));
		String username = userInfo.getUsername();
		if(username==null){
			return Message.error(Code.PARAMATER, "不得为空");
		}
		if(username.matches("^.*[\\s]+.*$")){
			return Message.error(Code.PARAMATER, "用户名不能包含空格、制表符、换页符等空白字符");
		}
		try {
			userInfoService.insert(userInfo);
			return Message.success("插入成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(userInfo)+"\n\t"+e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法插入数据");
		}
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Message deleteUserInfo(@RequestParam("id") Integer id){
		try {
			userInfoService.deleteById(id);
			return Message.success("删除成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(id)+"\n\t"+e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法删除数据");
		}
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public Message updateUserInfo(@RequestBody UserInfo userInfo,HttpSession session,HttpServletRequest request){
		String username = userInfo.getUsername();
		//下面是为了防止有人恶意篡改等级和积分，只要他们传过来为空即可。
		Integer level= userInfo.getCurrentCreditLevel();
		Integer value= userInfo.getCurrentCreditValue();
		String id= session.getAttribute("id").toString();//以后会改
		if(username==null||level!=null&&value!=null){
			//恶意侵入，记录ip，并禁止其再次登录
			String ip= IpUtils.getIp(request);
			log.error("ip:"+JSON.toJSON(ip)+"\n\t username:"+userInfo.getUsername()+"\n\t id:"+id);
			return Message.error(Code.FATAL_ERROR, "别搞事情",ip);
		}
		try {
			userInfo.setReviser(new Integer(id));
			userInfoService.update(userInfo);
			return Message.success("更新成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(userInfo)+"\n\t"+e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法更改数据");
		}
	}
	
	@RequestMapping("/find")
	@ResponseBody
	public Message findUserInfo(@RequestBody UserInfo userInfo){
		String username = userInfo.getUsername();
		if(username== null){
			return Message.error(Code.PARAMATER, "不得为空");
		}
		try {
			UserWebDto dto = userInfoService.find(username);
			return Message.success("查找成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(userInfo)+"\n\t"+e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法查询数据");
		} 
	}
	
	
	
}
