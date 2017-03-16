package com.wtlib.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Message;
import com.alibaba.fastjson.JSON;
import  com.wtlib.base.constants.Code;
import  com.wtlib.base.constants.DataStatusEnum;
import  com.wtlib.base.pojo.FeedBack;
import  com.wtlib.base.service.FeedBackService;

/**
 * @Description: 回馈信息控制层
 * @author pohoulong
 * @date 2017年1月22日 下午2:45:10
 */
@Controller
@RequestMapping("/admin")
public class FeedBackController {
	@Autowired
	private FeedBackService feedBackService;
	
	Logger log = Logger.getLogger(FeedBackController.class);

	@RequestMapping("/delete/feedback")
	@ResponseBody
	public Message deleteFeedBack(@RequestParam("id") Integer id,HttpSession session) {
		String reviser = session.getAttribute("id").toString();// 以后会改
		try {
			feedBackService.deleteById(id,reviser);
			return Message.success("删除成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(id) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法删除数据");
		}
	}


	@RequestMapping("/get/feedback")
	public Message getFeedBack() {
		try {
			List<FeedBack> feedBackList= feedBackService.selectAll(DataStatusEnum.NORMAL_USED.getCode());
			return Message.success(Code.SUCCESS, "查找成功",feedBackList);
		} catch (Exception e) {
			log.error(e.toString());
			return Message.error(Code.ERROR_CONNECTION, "找不到记录！");
		}
	}
	
	@RequestMapping("/get/user/feedback")
	public Message getFeedBackByUserId(HttpSession session) {
		String userId = session.getAttribute("id").toString();// 以后会改
		try {
			List<FeedBack> feedBackList= feedBackService.selectAllByUserId(userId,DataStatusEnum.NORMAL_USED.getCode());
			return Message.success(Code.SUCCESS, "查找成功",feedBackList);
		} catch (Exception e) {
			log.error(e.toString());
			return Message.error(Code.ERROR_CONNECTION, "找不到记录！");
		}
	}
	
}
