package com.taoding.common.authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;

import com.alibaba.druid.support.json.JSONParser;
import com.taoding.common.utils.Encodes;
import com.taoding.common.utils.JsonUtil;
import com.taoding.common.utils.StringUtils;

public class JwtUtil {

	private static String SECRET;

	private static String HEADER;

	static {
		Map<String, String> jsonData = new HashMap<>();
		jsonData.put("typ", "JWT");
		jsonData.put("alg", "HS256");
		HEADER = Encodes.encodeBase64(JsonUtil.objectToJson(jsonData));

		try {
			YamlMapFactoryBean yaml = new YamlMapFactoryBean();
			yaml.setResources(new ClassPathResource("application.yml"));
			Map<?, ?> map = yaml.getObject();
			map = (Map<?, ?>) map.get("authentication");
			SECRET = map.get("secret").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * JWT 签名
	 * 
	 * @param playload
	 * @return
	 */
	public static String sign(String playload) {
		return sign(HEADER, playload);
	}

	/**
	 * JWT 签名
	 * 
	 * @param header
	 * @param playload
	 * @return
	 */
	public static String sign(String header, String playload) {
		if (StringUtils.isBlank(header)) {
			throw new IllegalArgumentException("header can't be empty");

		}

		if (StringUtils.isBlank(playload)) {
			throw new IllegalArgumentException("playload can't be empty");
		}

		String sign = "";
		try {
			header = Encodes.encodeBase64(header);
			playload = Encodes.encodeBase64(playload);
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.update((header + "." + playload + "." + SECRET).getBytes());
			sign = header + "." + playload + "." + Encodes.encodeBase64(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return sign;
	}

	/**
	 * JWT 验签
	 * 
	 * @param sign
	 * @return
	 */
	public static boolean validate(String sign) {
		if (StringUtils.isEmpty(sign)) {
			return false;
		}

		String[] split = sign.split("\\.");
		if (split == null || split.length < 3) {
			return false;
		}

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.update((split[0] + "." + split[1] + "." + SECRET).getBytes());
			String signature = Encodes.encodeBase64(digest.digest());
			if (signature.equals(split[2])) {
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * 从ServletRequest中取得jwt token字符串
	 * @param request ServletRequest实例
	 * @return token String
	 */
	public static String getJwtTokenStringFromServletRequest(ServletRequest request) {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		String sign = httpRequest.getHeader("Authorization");
		if (StringUtils.isEmpty(sign)) {
			return null;
		}
		return sign.substring(sign.indexOf(" ") + 1, sign.length());
	}
	
	/**
	 * 判断token是否过期
	 * @param token token String
	 * @return 如果已过期，返回true, 否则，返回false
	 */
	public static boolean isTokenExpire(String token) {
		Map<String, Object> map = JwtUtil.getPayloadMap(token);
		Long exp = (Long) map.get("exp");
		return (exp < System.currentTimeMillis()) ? true : false;
	}
	
	/**
	 * 获取jwt token中的payload，以key, value的形式封装到map中
	 * @param token token字符串
	 * @return 以map方式封装的payload
	 */
	public static Map<String, Object> getPayloadMap(String token) {
		String playload = new String(Encodes.decodeBase64(token.split("\\.")[1]));
		JSONParser parser = new JSONParser(playload);
		return parser.parseMap();
	}
}
