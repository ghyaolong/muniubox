package com.taoding.common.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

import com.taoding.common.authentication.JwtUtil;


/**
 * 
 * 密码验证器,指定已何种方式去验证
 * <p>
 * Copyright: Copyright (c) 2017年11月14日 下午2:55:15
 * <p>
 * Company: 淘丁集团
 * <p>
 * @author yaochenglong
 * @version 1.0.0
 */
public class JwtShiroTokenCredentialsMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return JwtUtil.validate((String)info.getCredentials());
    }
}
