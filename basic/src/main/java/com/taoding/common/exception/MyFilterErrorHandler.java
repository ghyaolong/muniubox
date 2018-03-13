package com.taoding.common.exception;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.taoding.common.response.Response;
import com.taoding.common.response.ResponseInterceptor;
import com.taoding.common.utils.JsonUtil;

@Component
public class MyFilterErrorHandler extends OncePerRequestFilter{
	
	private static final Logger logger = LoggerFactory.getLogger(MyFilterErrorHandler.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e)  {
			logger.error(e.getMessage());
			response.setStatus(HttpStatus.OK.value());
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			Throwable exception = e.getCause();
			Response resp = new Response();
			ResponseInterceptor.handleException(exception, resp);
			response.getWriter().write(JsonUtil.convertResponseToJson(resp));
		}
	}

	
}
