package com.taoding.common.shiro;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.taoding.common.authentication.JwtUtil;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * 自定义权限过滤器. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年11月14日 下午2:38:48
 * <p>
 * Company: 淘丁集团
 * <p>
 * @author yaochenglong
 * @version 1.0.0
 */
public class PermissionFilter extends PermissionsAuthorizationFilter {

	@Override
    public boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws IOException {
		
		String requestUri = WebUtils.getRequestUri((HttpServletRequest)servletRequest);
		if (StringUtils.isEmpty(requestUri) || requestUri.equals("/login")) {
			return true;
		}
    	
    	String token = JwtUtil.getJwtTokenStringFromServletRequest(servletRequest);
    	
    	JwtShiroAuthenticationToken jwtShiroAuthenticationToken =  getJwtShiroTokenFromToken(token);
        
        initSubject(jwtShiroAuthenticationToken);
        
        Subject subject = SecurityUtils.getSubject();
    	
        String path1 = WebUtils.getRequestUri((HttpServletRequest)servletRequest);
        String permission = path1;
        if(!subject.isPermitted(permission)) {
            return false;
        }
        return true;

    }

    /**
     * 会话超时或权限校验未通过的，统一返回401，由前端页面弹窗提示
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        
        Subject subject = SecurityUtils.getSubject();
        
        if (subject.getPrincipal() == null) {
            ShiroUtil.writeResponse((HttpServletResponse) response, new Result(AuthorizationStatus.NOT_LOGIN));
        } else {
            ShiroUtil.writeResponse((HttpServletResponse) response, new Result(AuthorizationStatus.NOT_AUTHORIZATION));
        }
        return false;
    }
    
    private JwtShiroAuthenticationToken getJwtShiroTokenFromToken(String token) {
    	Map<String, Object> payloadMap = JwtUtil.getPayloadMap(token);
    	String userId = (String)payloadMap.get("userId");
    	String userName = (String) payloadMap.get("userName");
    	
    	String loginName = null;
    	if (null != payloadMap.get("loginName")) {
    		loginName = (String)payloadMap.get("loginName");
    	}
    	
    	String enterpriseId = null;
    	if (null != payloadMap.get("enterpriseId")) {
    		enterpriseId = (String)payloadMap.get("enterpriseId");
    	}
    	
    	return new JwtShiroAuthenticationToken.Builder()
    			.jwtToken(token)
    			.userId(userId)
    			.loginName(loginName)
    			.userName(userName)
    			.enterpriseId(enterpriseId)
    			.build();
    }
    
    private void initSubject(JwtShiroAuthenticationToken jwtShiroAuthenticationToken) {
    	Subject subject = SecurityUtils.getSubject();
        subject.login(jwtShiroAuthenticationToken);
    }
}
