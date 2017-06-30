package com.wtlib.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wtlib.wechat.pojo.SNSUserInfo;
import com.wtlib.wechat.pojo.WeixinOauth2Token;
import com.wtlib.wechat.utils.AdvancedUtil;

/**
 * 授权后的回调请求处理
 * 
 * @author pohoulong
 * 
 */
public class OAuthController extends HttpServlet {
	
	// 用户同意授权后，能获取到code
	@RequestMapping("/confirm/OAuth")
	public void doGet(@RequestParam("code") String code,ModelAndView view) throws ServletException, IOException {
		// 用户同意授权
		if (!"authdeny".equals(code)) {
			String APPID  = "请填写你自己的APPID";
			String SECRET = "请填写你自己的SECRET";
			// 获取网页授权access_token
			WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(APPID, SECRET, code);
			// 网页授权接口访问凭证
			String accessToken = weixinOauth2Token.getAccessToken();
			// 用户标识
			String openId = weixinOauth2Token.getOpenId();
			// 获取用户信息
			SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);

			// 设置要传递的参数
			view.addObject("snsUserInfo", snsUserInfo);
		}
		// 跳转到index.jsp
		view.setViewName("index");
	}
}