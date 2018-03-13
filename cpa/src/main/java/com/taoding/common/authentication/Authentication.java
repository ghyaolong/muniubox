package com.taoding.common.authentication;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.support.json.JSONParser;
import com.taoding.common.utils.Encodes;
import com.taoding.common.utils.JsonUtil;

/**
 * @author LX-PC
 *
 */
@Component
public class Authentication implements HandlerInterceptor {
	
	@Value("${authentication.expDate}")
	private long expDate;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String sign = JwtUtil.getJwtTokenStringFromServletRequest(request);

		String playload = new String(Encodes.decodeBase64(sign.split("\\.")[1]));
		JSONParser parser = new JSONParser(playload);
		Map<String, Object> map = parser.parseMap();
		
		map.put("exp", System.currentTimeMillis() + expDate);
		playload = JsonUtil.objectToJson(map);
		sign = JwtUtil.sign(playload);
		response.addHeader("Authorization", "Bearer " + sign);
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
	
}
