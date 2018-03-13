package com.taoding.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.taoding.common.authentication.Authentication;

@Component
@Configuration
public class CustomWebMvcConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private Authentication authentication;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authentication).addPathPatterns("/**").excludePathPatterns("/login");
	}
}