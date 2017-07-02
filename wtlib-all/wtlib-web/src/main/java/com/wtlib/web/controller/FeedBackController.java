package com.wtlib.web.controller;

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
public class FeedBackController {
	@Autowired
	private FeedBackService feedBackService;
	
	Logger log = Logger.getLogger(FeedBackController.class);
	
	//user
	@RequestMapping("/add/feedback")
	@ResponseBody
	public Message addFeedBack(@RequestBody FeedBack page, HttpSession session) {
		String id = session.getAttribute("id").toString();// 以后会改
	    String desc = page.getDesc();
		if (desc == null) {
			return Message.error(Code.PARAMATER, "不得为空");
		}
		page.setCreator(new Integer(id));
		page.setUserId(new Integer(id));
		try {
			feedBackService.insert(page);
			return Message.success("插入成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(page) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法插入数据");
		}
	}
	
}
