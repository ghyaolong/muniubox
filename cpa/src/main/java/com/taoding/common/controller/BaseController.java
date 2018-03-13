package com.taoding.common.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	 
	protected Map<String, Object> getRequestParams(HttpServletRequest request) {
	    Map<String, Object> params = new HashMap<>();
	    
	    Enumeration<String> keys = request.getParameterNames();
	    while (keys.hasMoreElements()) {
	      String key = (String) keys.nextElement();
	      params.put(key, request.getParameter(key));
	    }
	    
	    return params;
	  }
}
