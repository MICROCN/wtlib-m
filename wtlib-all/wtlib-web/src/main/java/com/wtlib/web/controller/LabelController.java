package com.wtlib.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Message;
import com.alibaba.fastjson.JSON;
import com.wtlib.base.constants.Code;
import com.wtlib.base.pojo.LabelInfo;
import com.wtlib.base.service.LabelInfoService;
import com.wtlib.base.vo.LabelPack;

@RequestMapping("/user")
public class LabelController {

	@Autowired
	private LabelInfoService labelInfoService;

	Logger log = Logger.getLogger(LabelController.class);

	// user
	@RequestMapping("/add/label")
	@ResponseBody
	public Message addLabel(@RequestBody LabelPack pack, HttpSession session) {
		String id = session.getAttribute("id").toString();// 以后会改
		LabelInfo info = pack.getInfo();
		//bookbaseid
		Integer infoId = pack.getInfoId();
		String value = info.getValue();
		if (value == null) {
			return Message.error(Code.PARAMATER, "不得为空");
		}
		info.setCreator(new Integer(id));
		info.setUserId(new Integer(id));
		try {
			labelInfoService.insert(info,infoId);
		} catch (Exception e) {
			log.error(JSON.toJSONString(info) + "\n\t" + e.toString());
			return Message.error(Code.ERROR_CONNECTION, "无法插入数据");
		}
		return Message.success("插入成功", Code.SUCCESS);
	}
}
