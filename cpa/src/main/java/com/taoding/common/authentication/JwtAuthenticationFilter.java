package com.taoding.common.authentication;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter implements Filter{
	
	@Value("${authentication.expDate}")
	private long expDate;
	
	private final static String TOKEN_INVALID = "{\"status\":50008,\"message\":\"TOKEN_INVALID\"}";
	private final static String TOKEN_EXPIRE = "{\"status\":50014,\"message\":\"TOKEN_EXPIRE\"}";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String token = JwtUtil.getJwtTokenStringFromServletRequest(request);
		
		System.out.println("********************************");
		System.out.println("**************isPrecheckRequest" + isPrecheckRequest(request, response) + "******************");
		System.out.println("********************************");
		
		if (!isPrecheckRequest(request, response)) {
			// 验证身份签名
			if (!JwtUtil.validate(token)) {
				PrintWriter writer = response.getWriter();
				writer.println(TOKEN_INVALID);
				return;
			}

			// 验证签名是否过期
			if (JwtUtil.isTokenExpire(token)) {
				PrintWriter writer = response.getWriter();
				writer.print(TOKEN_EXPIRE);
				return;
			}
		}
		chain.doFilter(request, response);
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		httpResponse.addHeader("Access-Control-Expose-Headers", "Authorization");
	}

	@Override
	public void destroy() {
		
	}
	
	/**
	 * 判断某个请求是不是预检查请求。有些前端请求框架，比如axios，针对每一个url的请求都会发送两次请求到后台，
	 * 第一次为预检查请求，判断有没有权限，如果有，才会发送第二会真实的请求，预检查的请求方法统一为OPTIONS，
	 * 这类请求并不会携带token，也不会对资源的状态造成任何影响，所以在检查Token时将其过滤掉
	 * @param request
	 * @param response
	 * @return true or false
	 */
	private boolean isPrecheckRequest(ServletRequest request, ServletResponse response) {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		System.out.println("-----------------------------");
		System.out.print("httpRequest.getMethod()\t" + httpRequest.getMethod());
		System.out.print("httpRequest.getMethod().equals(\"OPTIONS\")\t" + httpRequest.getMethod().equals("OPTIONS"));
		System.out.println("-----------------------------");
		
		if (httpRequest.getMethod().equals("OPTIONS")) {
			//HttpStatus.SC_NO_CONTENT = 204
			httpResponse.setStatus(HttpStatus.SC_NO_CONTENT);
			//当判定为预检请求后，设定允许请求的方法
			httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS, DELETE");
			//当判定为预检请求后，设定允许请求的头部类型
			httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, Token"); 
			httpResponse.addHeader("Access-Control-Max-Age", "1"); 
			return true;
		}
		return false;
	}

}
