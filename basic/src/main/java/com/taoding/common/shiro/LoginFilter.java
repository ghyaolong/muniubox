package com.taoding.common.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 登录过滤器<br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年11月14日 下午2:39:20
 * <p>
 * Company: 淘丁集团
 * <p>
 * @author yaochenglong
 * @version 1.0.0
 */
public class LoginFilter extends AuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Subject subject = getSubject(servletRequest, servletResponse);
        String path = request.getServletPath();
        if (path.equals("/loginUser")) {
            return true;
        }
        if (subject.getPrincipals() != null) {
            return true;
        }
        return false;
    }

    /**
     * 会话超时或权限校验未通过的，统一返回401，由前端页面弹窗提示
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        ShiroUtil.writeResponse((HttpServletResponse) servletResponse, new Result(AuthorizationStatus.NOT_LOGIN));
        return false;
    }
}
