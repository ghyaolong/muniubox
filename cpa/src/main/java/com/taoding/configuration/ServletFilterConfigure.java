package com.taoding.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.taoding.common.authentication.JwtAuthenticationFilter;
import com.taoding.common.exception.MyFilterErrorHandler;

@Configuration
public class ServletFilterConfigure {
	
	@Autowired
	JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	MyFilterErrorHandler MyFilterErrorHandler;
	
	@Bean
	public FilterRegistrationBean doRegistration() {
		
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(MyFilterErrorHandler);
		filterRegistrationBean.setFilter(jwtAuthenticationFilter);
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.setName("jwtAuthenticationFilter");
		return filterRegistrationBean;
	}

}
