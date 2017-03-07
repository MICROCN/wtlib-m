package com.wtlib.admin.controller;

import java.util.List;

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
import  com.wtlib.base.pojo.CreditInfo;
import  com.wtlib.base.pojo.FeedBack;
import  com.wtlib.base.service.CreditInfoService;
import  com.wtlib.base.service.FeedBackService;

/**
 * @Description: 信用相关控制类
 * @author zongzi
 * @date 2017年1月22日 下午2:39:21
 */
@Controller
@RequestMapping("/creditinfo/")
public class CreditInfoController {
	@Autowired
	private CreditInfoService creditInfoService;
	
	Logger log = Logger.getLogger(FeedBackController.class);
	
	//user
	@RequestMapping("/add")
	@ResponseBody
	public Message addCreditInfo(@RequestBody CreditInfo info, HttpSession session) {
		String id = session.getAttribute("id").toString();// 以后会改
		Integer miniLevel = info.getMiniUserLevel();
		String isPlus = info.getIsPlus();
		String name = info.getCreditName();
		Integer value = info.getCreditValue();
		if (miniLevel == null && isPlus == null && name == null &&value == null) {
			return Message.error(Code.PARAMATER, "不得为空");
		}
		info.setCreator(new Integer(id));
		try {
			creditInfoService.insert(info);
			return Message.success("插入成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(info) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法插入数据");
		}
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Message deleteCreditInfo(@RequestParam("id") Integer id,HttpSession session) {
		String reviser = session.getAttribute("id").toString();// 以后会改
		try {
			creditInfoService.deleteById(id,reviser);
			return Message.success("删除成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(id) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法删除数据");
		}
	}

	@RequestMapping("/update")
	public Message updateCreditInfo(@RequestBody CreditInfo info, HttpSession session){
		String id = session.getAttribute("id").toString();// 以后会改
		Integer miniLevel = info.getMiniUserLevel();
		String isPlus = info.getIsPlus();
		String name = info.getCreditName();
		Integer value = info.getCreditValue();
		if (miniLevel == null && isPlus == null && name == null &&value == null) {
			return Message.error(Code.PARAMATER, "不得为空");
		}
		info.setCreator(new Integer(id));
		try {
			creditInfoService.update(info);
			return Message.success("修改成功", Code.SUCCESS);
		} catch (Exception e) {
			log.error(JSON.toJSONString(info) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法修改数据");
		}
	}

	@RequestMapping("/get")
	public Message getCreditInfo() {
		try {
			List<CreditInfo> creditInfoList= creditInfoService.selectAll(DataStatusEnum.NORMAL_USED.getCode());
			return Message.success(Code.SUCCESS, "查找成功",creditInfoList);
		} catch (Exception e) {
			log.error(e.toString());
			return Message.error(Code.ERROR_CONNECTION, "找不到记录！");
		}
	}
}
