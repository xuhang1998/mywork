package com.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.dto.Token;
import com.service.TokenManager;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.constants.UserConstants;
/**
 * redis实现的Token
 * 
 * 
 * @author 小威老师 xiaoweijiagou@163.com
 *
 *         2017年8月13日
 */
@Service
public class RedisTokenManager implements TokenManager {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	private static final String TOKEN_PREFIX = "tokens:";


	@Override
	public Token saveToken(UsernamePasswordToken usernamePasswordToken) {
		/*String key = UUID.randomUUID().toString();*/
		redisTemplate.opsForValue().set(UserConstants.LOGIN_TOKEN, JSONObject.toJSONString(usernamePasswordToken),
				86400, TimeUnit.SECONDS);

		return new Token(UserConstants.LOGIN_TOKEN, DateUtils.addSeconds(new Date(), 86400));
	}

	@Override
	public UsernamePasswordToken getToken(String key) {
		String json = redisTemplate.opsForValue().get(UserConstants.LOGIN_TOKEN);
		if (!StringUtils.isEmpty(json)) {
			UsernamePasswordToken usernamePasswordToken = JSONObject.parseObject(json, UsernamePasswordToken.class);

			return usernamePasswordToken;
		}
		return null;
	}

	@Override
	public boolean deleteToken(String key) {
		key = UserConstants.LOGIN_TOKEN;
		if (redisTemplate.hasKey(key)) {
			redisTemplate.delete(key);

			return true;
		}

		return false;
	}
}
