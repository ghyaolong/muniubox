package com.taoding.common.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * JWTSubjectFactory
 * 默认情况下shiro委托DefaultWebSubjectFactory生成subject,该subject是和session相关的，
 * restful是无状态的，所以关掉创建session的过程
 * @author vincent
 *
 */
public class JwtSubjectFactory extends DefaultWebSubjectFactory{
	
	@Override
	public Subject createSubject(SubjectContext context) {
		context.setSessionCreationEnabled(false);
		return super.createSubject(context);
	}

}
