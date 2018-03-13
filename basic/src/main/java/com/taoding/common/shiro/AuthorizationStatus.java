package com.taoding.common.shiro;

/**
 * Created by Administrator on 2017/11/13.
 */
public enum AuthorizationStatus {
    NOT_LOGIN(401,"没有登录"),
    NOT_AUTHORIZATION(403,"没有授权");
    private Integer code;

    private String msg;
    AuthorizationStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
