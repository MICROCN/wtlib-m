package com.wtlib.base.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wtlib.wechat.pojo.AccessToken;
import com.wtlib.wechat.utils.WeChatUtil;

public class TokenJob {
	private static Logger log = LoggerFactory.getLogger(TokenJob.class);
	// 第三方用户唯一凭证
	public static String appid = "";
	// 第三方用户唯一凭证密钥
	public static String appsecret = "";
	public static AccessToken accessToken = null;
	
	public void work() throws Exception {
		while (true) {
			accessToken = WeChatUtil.getAccessToken(appid, appsecret);
			if (null != accessToken) {
				log.info("获取access_token成功，有效时长{}秒 token:{}",
						accessToken.getExpiresIn(), accessToken.getToken());
			}
		}
	}
}
