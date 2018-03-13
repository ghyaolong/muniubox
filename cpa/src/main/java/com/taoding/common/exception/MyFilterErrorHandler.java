package com.taoding.common.exception;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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
	
	ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();
    
    private static final Logger logger = LoggerFactory.getLogger(MyFilterErrorHandler.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
			response.addHeader("Access-Control-Expose-Headers", "Authorization");
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
