package com.taoding.common.shiro;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.taoding.common.utils.PlatformBasicInfoUtil;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.log.LogEvent;
import com.taoding.domain.menu.Menu;
import com.taoding.service.log.LogService;
import com.taoding.service.menu.MenuService;

/**
 * 自定义Shrio的验证域，
 *  <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年11月14日 上午11:58:15
 * <p>
 * Company: 淘丁集团
 * <p>
 * @author yaochenglong
 * @version 1.0.0
 */
public class JwtAuthRealm extends AuthorizingRealm {
	
//	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	PlatformBasicInfoUtil platformBasicInfoUtil;
	
	@Autowired
	LogService logService;
	
	
	@Override
	public boolean supports(AuthenticationToken token) {
		return token.getClass().equals(JwtShiroAuthenticationToken.class);
	}

    /**
     *在Controller调用Subject.login()方法的时候，会进入此验证，进行用户名和密码的验证。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	JwtShiroAuthenticationToken jwtShiroToken = (JwtShiroAuthenticationToken)token;
        return new SimpleAuthenticationInfo(jwtShiroToken.getPrincipal(), jwtShiroToken.getCredentials(), this.getClass().getName());
    }
    
    /**
     *在访问需要权限的资源的时候，
     *此配置在ShrioConfiguration.java的配置类里配置哪些资源需要权限，哪些资源需要匿名访问;
     *需要将用户的用户和用户的权限set到SimpleAuthorizationInfo对象里，这样子Shrio在做权限控制的时候，
     *可以拿到用户拥有的角色和权限信息。
     *会进入此验证方法进行权限的验证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
    	
    	List<Menu> menuList = menuService.listMenuByUserId(UserUtils.getCurrentUserId());
//    	List<Menu> menuList = platformBasicInfoUtil.getMenuByUserId(UserUtils.getCurrentUserId(), UserUtils.getCurrentUserToken());
    	Set<String> permissions = menuList.stream().map(menu -> menu.getHref() + "_" + menu.getRemarks()).collect(Collectors.toSet());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permissions);
        return info;
    }

    /**
     * 清空当前用户权限信息
     */
    public  void clearCachedAuthorizationInfo() {
        PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
        SimplePrincipalCollection principals = new SimplePrincipalCollection(
                principalCollection, getName());
        super.clearCachedAuthorizationInfo(principals);
    }
    /**
     * 指定principalCollection 清除
     */
    public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(
                principalCollection, getName());
        super.clearCachedAuthorizationInfo(principals);
    }
    
    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
    	AuthorizationInfo authorizationInfo = getAuthorizationInfo(principals);
    	Set<String> permistionSet = (Set<String>)authorizationInfo.getStringPermissions();
    	String permis = permission.toString().replaceAll("\\[", "").replaceAll("]", "");
    	boolean isPermit = true;
    	String content = "";
    	String url = "";
    	for (String permistionStr : permistionSet) {
    		content = permistionStr.split("_")[1];
    		url = permistionStr.split("_")[0];
    		String replacedPattern = url.replace("{id}", "[a-zA-Z0-9]+").toLowerCase();
    		Pattern p = Pattern.compile(replacedPattern);
    		Matcher m = p.matcher(permis.toLowerCase());
    		if (m.matches()) {
    			isPermit = true;
    			break;
    		}
    	}
    	doLog(isPermit, content);
    	return isPermit;
    }
    
    private void doLog(boolean isPermit, String content) {
    	if (isPermit) {
    		LogEvent event = new LogEvent.Builder()
    				.content(content)
    				.date(new Date())
    				.ipAddress(UserUtils.getCurrentUserRemoteAddress())
    				.operator(UserUtils.getCurrentLoginName())
    				.operatorId(UserUtils.getCurrentUserId())
    				.customerId((String)UserUtils.getCurrentUserInfo().getOtherInfo().get("customerId"))
    				.build();
    		logService.fireLogEvent(event);
    	}
    }
}
