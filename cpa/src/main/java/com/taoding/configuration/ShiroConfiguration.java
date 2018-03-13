package com.taoding.configuration;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import com.taoding.common.shiro.JwtAuthRealm;
import com.taoding.common.shiro.JwtShiroTokenCredentialsMatcher;
import com.taoding.common.shiro.JwtSubjectFactory;
import com.taoding.common.shiro.PermissionFilter;

import org.springframework.context.annotation.Configuration;

/**
 * Shiro配置类 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年11月14日 上午11:54:02
 * <p>
 * Company: 淘丁集团
 * <p>
 * @author yaochenglong
 * @version 1.0.0
 */
@Configuration
public class ShiroConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

    @Bean(name="shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager manager) {
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
        bean.setSecurityManager(manager);
        Map<String,Filter> authFilter = bean.getFilters();
        authFilter.put("perms",permissionFilter());
        bean.setFilters(authFilter);

        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap=new LinkedHashMap<>();

        filterChainDefinitionMap.put("/**", "authc");//表示需要认证才可以访问
        filterChainDefinitionMap.put("/**", "perms");//表示需要认证才可以访问
        //讲自定义的权限拦截器配置到shiro中

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }
    
    //配置核心安全事务管理器
    @Bean(name="securityManager")
    public SecurityManager securityManager(@Qualifier("authRealm") JwtAuthRealm authRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        
        //JwtSubjectFactory会关掉session的创建
        manager.setSubjectFactory(new JwtSubjectFactory());
        DefaultSessionManager sessionManage = new DefaultSessionManager();
        
        //关掉定期清除session
        sessionManage.setSessionValidationSchedulerEnabled(false);
        manager.setSessionManager(sessionManage);
        manager.setRealm(authRealm);
        
        DefaultSubjectDAO subjectDao = (DefaultSubjectDAO) manager.getSubjectDAO();
        
        //关闭session存储
        DefaultSessionStorageEvaluator sessionStorageEv = (DefaultSessionStorageEvaluator) subjectDao.getSessionStorageEvaluator();
        sessionStorageEv.setSessionStorageEnabled(false);
        
        return manager;
    }
    
    //配置自定义的权限登录器
    @Bean(name="authRealm")
    public JwtAuthRealm authRealm(@Qualifier("credentialsMatcher") JwtShiroTokenCredentialsMatcher matcher) {
        JwtAuthRealm authRealm=new JwtAuthRealm();
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }
    
    //配置自定义的密码比较器
    @Bean(name="credentialsMatcher")
    public JwtShiroTokenCredentialsMatcher credentialsMatcher() {
        return new JwtShiroTokenCredentialsMatcher();
    }
    
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }
    
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
    
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }
    
//    @Bean
//    public LoginFilter loginFilter(){
//        return new LoginFilter();
//    }
    
    @Bean
    public PermissionFilter permissionFilter(){
        return new PermissionFilter();
    }
}
